package com.aiart.server.controller;

import com.aiart.server.service.ApplicationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ApplicationService applicationService;

    @Test
    public void testExecuteApplicationSuccess() throws Exception {
        // 创建 JSON 请求数据
        ObjectNode requestdata = objectMapper.createObjectNode().put("applicationId", 1);
        // 模拟应用服务返回部署地址
        requestdata.put("prompt","dog");
        JsonNode requestData=(JsonNode)requestdata;
//        when(applicationService.getDeploymentAddressById(1L)).thenReturn(new URI("http://example.com"));

        // 发起 POST 请求，调用 executeApplication 方法
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/application/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 从响应中获取内容
        String responseBody = result.getResponse().getContentAsString();

        // 验证响应内容是否符合预期
        // 根据您的应用程序需要进行相应的断言
        // 例如，您可以检查返回的 JSON 中是否包含特定字段
         JsonNode responseJson = objectMapper.readTree(responseBody);
//         assertEquals("success", responseJson.get("result").asText());
        int statusCode = result.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), statusCode);

        // 输出images内容
        JsonNode imagesNode = responseJson.get("images");
        if (imagesNode != null) {
            System.out.println("Images:");
            for (JsonNode image : imagesNode) {
                System.out.println(image.toString());
            }
        } else {
            System.out.println("Images node not found in the response.");
        }
    }
    @Test
    public void testExecuteApplicationFailure() throws Exception {
        // 创建 JSON 请求数据
        ObjectNode requestdata = objectMapper.createObjectNode().put("applicationId", 2);
        // 模拟应用服务返回部署地址
        requestdata.put("prompt","dog");
        JsonNode requestData=(JsonNode)requestdata;
        // 模拟应用服务返回 null，表示应用不存在或未找到部署地址
//        when(applicationService.getDeploymentAddressById(2L)).thenReturn(null);

        // 发起 POST 请求，调用 executeApplication 方法
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/application/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestData.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        // 获取HTTP状态码
        int statusCode = result.getResponse().getStatus();

// 获取响应内容
        String responseBody = result.getResponse().getContentAsString();

// 验证HTTP状态码是否为NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND.value(), statusCode);

//        assertNotNull("not null",responseBody);

    }
}
