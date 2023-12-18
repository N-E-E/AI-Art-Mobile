package com.aiart.server.controller;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aiart.server.entity.UserEntity;
import com.aiart.server.service.UserApplicationService;
import com.aiart.server.service.UserService;
import com.aiart.server.utils.SmsCodeManager;
import com.aiart.server.utils.UserResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsCodeManager smsCodeManager;

    @Autowired
    private UserApplicationService userApplicationService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
//    @Rollback(value = false)
    public void testLogin() throws Exception {
        // 实际的登录请求，这将与数据库交互
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .param("account", "testUser")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(UserResult.SUCCESS.ordinal())));
                .andExpect(status().isNotFound());
    }

    // 其他测试方法也要改成实际的测试
    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .param("account", "registerUser1")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(UserResult.SUCCESS.ordinal())));
    }
    @Test
    public void testRegisterFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .param("account", "testUser")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(UserResult.ACCOUNT_EXISTS.ordinal())));
    }
    @Test
    public void testSmsLogin() throws Exception {//TODO：未测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/sms-login")
                        .param("phoneNumber", "testUser")
                        .param("code", "123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(UserResult.SUCCESS.ordinal())));
    }

    @Test
    public void testSendSmsCode() throws Exception {//TODO：未测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/send-sms-code")
                        .param("phoneNumber", "18186139236")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("\"SUCCESS\""));
    }

    @Test
    public void testGetUserApplications() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/applications/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").value(1L));
    }

    @Test
    public void testUpdateUserInfoSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/userinfo", 1L)
                        .param("newNickname", "NewNickname")
                        .param("newGender", "Male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User information updated successfully."));
    }
    @Test
    public void testUpdateUserInfoFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/{userId}/userinfo", 4L)
                        .param("newNickname", "NewNickname")
                        .param("newGender", "Male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("User not found."));
    }
    @Test
    public void testGetUserProfile() throws Exception {
        // 假设存在一个具有userId为1的用户
        Long userId = 1L;

        // 发起 GET 请求，调用 getUserProfile 方法
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/{userId}/get-profile", userId) // 替换成实际的基础URL
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // 获取响应体中的JSON字符串
        String responseBody = result.getResponse().getContentAsString();

        // 将响应体解析为 UserEntity 对象
        UserEntity user = objectMapper.readValue(responseBody, UserEntity.class);

        // 进行断言，确保用户信息正确
        // 根据实际情况编写适当的断言
        Assertions.assertEquals("testUser", user.getAccount());
    }

}


