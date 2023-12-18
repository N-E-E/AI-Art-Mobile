package com.aiart.server.controller;

import com.aiart.server.service.ApplicationService;
import com.aiart.server.utils.LoggerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.protocol.HTTP;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/api/application")
@Api(tags = "Application Controller", description = "Controller for application-related operations")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService; // ApplicationService 用于管理应用信息
    private final LoggerInfo LoggerInfoTo;
    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);
    private static final boolean outputToConsole=false;//true-输出到控制台，false输出到journal日志

    public ApplicationController(){
        this.LoggerInfoTo=new LoggerInfo(outputToConsole);
    }


    public void outputJsonNode(JsonNode rootNode){// 遍历 JsonNode 的键值对并输出
        logger.info("JSON key-value pairs:");
        if (rootNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();
                logger.info("Key: " + key + ", Value: " + value);
            }
        }
    }

    public JsonNode deletekey( String keyToDelete, JsonNode rootNode) throws JsonProcessingException {//删除JsonNode中的指定键值对
        ObjectMapper objectMapper = new ObjectMapper();
        // 创建一个新的 ObjectNode
        ObjectNode newNode = objectMapper.createObjectNode();
        if (rootNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();
                if (!key.equals(keyToDelete)) {
                    newNode.put(key, value);
                }
            }
        }
        JsonNode jsonNode=newNode;
        logger.info("delete key "+keyToDelete+" success!");
        return jsonNode;
    }
    @ApiOperation("Execute Application")
    @PostMapping("/execute")  // 中转前端发来的数据包
    public ResponseEntity<JsonNode> executeApplication(@ApiParam(value = "Request data in JSON format", required = true) @RequestBody JsonNode requestData) {
        try {
            // 从 requestData 中获取 applicationId
            Long applicationId = Long.valueOf(requestData.get("applicationId").asText());
//            logger.info(applicationId.toString());
//            Long applicationId = 1L;
            String url = applicationService.getUrlById(applicationId);
            if(applicationId==1)    logger.info("txt2img");
            else if(applicationId==2)   logger.info("img2img");
//            outputJsonNode(requestData);
            requestData=deletekey("applicationId",requestData);
//            outputJsonNode(requestData);
            if (url != null) {  // access by url directly
                logger.info("request url success!");
                ResponseEntity<JsonNode> result = sendRequestToDeploymentServer(url, requestData);
                if (result == null) {
                    return ResponseEntity.ok(null);
                }
                return result;
            }

            // 根据应用 ID 获取应用的部署地址
            URI deploymentAddress = applicationService.getDeploymentAddressById(applicationId);

            if (deploymentAddress != null) {
                logger.info("request URI Success!");
                return sendRequestToDeploymentServer(deploymentAddress.toString(), requestData);
            } else {
                // 应用不存在或未找到部署地址
                logger.warn("Application not found or deployment address not available for application ID: "+ applicationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            // 处理异常
            logger.error("Error while executing application: " + e.getMessage());
//            throw new RuntimeException("Error while executing application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }



        // offline test
        // 创建 ObjectMapper 对象
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 创建一个 JSON 对象
//        JsonNode jsonNode = objectMapper.createObjectNode();
//        // 添加 "text" 字段
//        //        ((ObjectNode) jsonNode).put("text", "test");
//        // 添加 "image" 字段
//        ((ObjectNode) jsonNode).put("img", "sfjlsdjgslkfhfslkfn");
//        ResponseEntity<JsonNode> responseEntity = new ResponseEntity<>(jsonNode, HttpStatus.OK);
//        return responseEntity;
    }

    // 发送到gpu服务器并获取返回结果
    private ResponseEntity<JsonNode> sendRequestToDeploymentServer(String deploymentUrl, JsonNode requestData) {
        try {
            // 使用 RestTemplate 或其他 HTTP 客户端库发送 JSON 数据到部署的服务器
            RestTemplate restTemplate = new RestTemplate();
//            outputJsonNode(requestData);
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(deploymentUrl, requestData, JsonNode.class);
//            ResponseEntity<JsonNode> response = restTemplate.postForEntity(deploymentUrl + "/sdapi/v1/img2img", requestData, JsonNode.class);
            logger.info(String.valueOf(requestData.get("prompt")));
            if (response.getStatusCode() == HttpStatus.OK) {
                // 如果请求成功，直接返回 GPU 服务器返回的 JSON 数据
                logger.info("Request GPU service Success!");
                return response;
            } else {
                // 处理请求失败的情况
                logger.error("Request to GPU server failed with status code: "+ response.getStatusCodeValue());
//                throw new RuntimeException("Failed to send request to deployment server.");
                return null;
            }
        } catch (Exception e) {
            // 处理异常
            logger.error("Error while sending request to deployment server: " + e.getMessage());
//            throw new RuntimeException("Error while sending request to deployment server: " + e.getMessage());
            return null;
        }

    }
}
