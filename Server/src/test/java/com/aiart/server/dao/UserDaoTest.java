package com.aiart.server.dao;

import com.aiart.server.dao.UserDao;
import com.aiart.server.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserDaoTest {

    private static final Logger logger = Logger.getLogger(UserDaoTest.class.getName());

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        // 在测试之前可以执行一些初始化操作，例如插入测试数据
    }

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testInsertUser() {
        UserEntity user = new UserEntity();
        user.setAccount("111");
        user.setPassword("111");
        user.setNickname("bill");
        user.setGender("Male");
        user.setModelLibrary("{}");

        int result = userDao.insertUser(user);

        System.out.println("Insert result: " + result); // 输出插入结果
        System.out.println("User ID: " + user.getUserId()); // 输出用户 ID

        assertEquals(1, result);// 插入成功应返回1
        assertNotNull(user.getUserId()); // 检查用户ID是否已分配
    }

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testGetUserById() {
        // 假设已存在用户，通过用户ID获取用户信息
        Long userId = 1L;
        UserEntity user = userDao.getUserById(userId);
        assertNotNull(user); // 用户信息不应为空
        assertEquals(userId, user.getUserId()); // 检查用户ID是否匹配
        System.out.println(user.getAccount());
    }

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testUpdateUserInfo() {
        // 假设已存在用户
        Long userId = 1L;
        String newNickname = "Updated Nickname";
        String newGender = "Female";

        boolean result = userDao.updateUserInfo(userId, newNickname, newGender);

        assertTrue(result); // 更新成功应返回true

        // 验证更新后的用户信息
        UserEntity updatedUser = userDao.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(newNickname, updatedUser.getNickname());
        assertEquals(newGender, updatedUser.getGender());
    }

    // 可以继续编写其他测试方法
    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testDeleteUserById() {
        Long userId=1L;
        // 删除用户
        int result = userDao.deleteUserById(userId);

        assertEquals(1, result); // 删除成功应返回1

        // 验证用户是否已被删除
        UserEntity deletedUser = userDao.getUserById(userId);
        assertNull(deletedUser);
    }

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testGetUserByAccount() {
        // 插入一个用户以便后续查询
//        UserEntity user = new UserEntity();
//        user.setAccount("testUser");
//        user.setPassword("password");
//        user.setNickname("Test User");
//        user.setGender("Male");
//        userDao.insertUser(user);

        String account = "testUser";
        String password="password";
        // 查询用户
        UserEntity queriedUser = userDao.getUserByAccount(account);
//        System.out.println(queriedUser.toString());
        assertNotNull(queriedUser);
        assertEquals(account, queriedUser.getAccount());
        assertEquals(password, queriedUser.getPassword());
    }


    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testUpdateUser() {
//        // 插入一个用户以便后续更新
//        UserEntity user = new UserEntity();
//        user.setAccount("testUser");
//        user.setPassword("password");
//        user.setNickname("Test User");
//        user.setGender("Male");
//        user.setModelLibrary("Library");
//        userDao.insertUser(user);

        Long userId = 2L;

        // 创建一个新的 UserEntity 对象，用于更新
        UserEntity updatedUser = new UserEntity();
        updatedUser.setUserId(userId);
        updatedUser.setAccount("18186139236");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setNickname("Updated User");
        updatedUser.setGender("Female");
        updatedUser.setModelLibrary("{}");

        // 更新用户信息
        int result = userDao.updateUser(updatedUser);

        assertEquals(1, result); // 更新成功应返回1

        // 验证更新后的用户信息
        UserEntity retrievedUser = userDao.getUserById(userId);
        assertNotNull(retrievedUser);
        assertEquals(updatedUser.getAccount(), retrievedUser.getAccount());
        assertEquals(updatedUser.getPassword(), retrievedUser.getPassword());
        assertEquals(updatedUser.getNickname(), retrievedUser.getNickname());
        assertEquals(updatedUser.getGender(), retrievedUser.getGender());
//        assertEquals(null, retrievedUser.getModelLibrary());
    }
}

