package com.aiart.server.service;

import com.aiart.server.dao.UserDao;
import com.aiart.server.entity.UserEntity;
import com.aiart.server.utils.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        // 初始化Mockito注解
        MockitoAnnotations.openMocks(this);
    }

    @Test
//    @Rollback(value = false)
    public void testLoginSuccess() {
        // 调用 login 方法
        UserResult result = userService.login("testUser", "password").getUserResult();

        // 验证登录结果是否为成功
        assertEquals(UserResult.SUCCESS, result);
    }

    @Test
    public void testLoginWithInvalidAccount() {

        // 调用 login 方法
        UserResult result = userService.login("nonExistentUser", "password").getUserResult();

        // 验证登录结果是否为账号不存在
        assertEquals(UserResult.ACCOUNT_NOT_FOUND, result);
    }

    @Test
    public void testLoginWithInvalidPassword() {

        // 调用 login 方法，提供错误的密码
        UserResult result = userService.login("testUser", "wrongPassword").getUserResult();

        // 验证登录结果是否为密码错误
        assertEquals(UserResult.INVALID_CREDENTIALS, result);
    }
}
