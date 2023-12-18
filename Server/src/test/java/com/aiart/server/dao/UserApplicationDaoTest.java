package com.aiart.server.dao;

import com.aiart.server.dao.UserApplicationDao;
import com.aiart.server.entity.UserApplicationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserApplicationDaoTest {

    @Autowired
    private UserApplicationDao userApplicationDao;

    @BeforeEach
    public void setUp() {
        // 在每个测试方法之前执行的设置代码
        // 这里可以插入一些测试数据，以便后续的测试
    }

    @Test
    @Rollback(value = false)
    public void testInsertUserApplication() {
        // 插入一个用户应用关联记录
        UserApplicationEntity userApplication = new UserApplicationEntity();
        userApplication.setUserId(1L);
        userApplication.setApplicationId(1L);
        userApplicationDao.insertUserApplication(userApplication);

        // 添加断言以验证插入是否成功
        // 可以根据返回值、查询等方式进行验证
        // 这里以查询用户应用关联记录为例
        List<Long> applications = userApplicationDao.getApplicationsByUserId(1L);
        assertTrue(applications.contains(1L));
    }

    @Test
    @Rollback(value = false)
    public void testDeleteUserApplication() {
//        // 插入一个用户应用关联记录，然后再删除它
//        UserApplicationEntity userApplication = new UserApplicationEntity();
//        userApplication.setUserId(1L);
//        userApplication.setApplicationId(1L);
//        userApplicationDao.insertUserApplication(userApplication);

        // 删除用户应用关联记录
        userApplicationDao.deleteUserApplication(2L, 1L);

        // 添加断言以验证删除成功
        // 可以根据返回值、查询等方式进行验证
        // 这里以查询用户应用关联记录为例
        List<Long> applications = userApplicationDao.getApplicationsByUserId(1L);
        assertTrue(applications.isEmpty());
    }

    @Test
    @Rollback(value = false)
    public void testGetApplicationsByUserId() {
        // 查询用户1的应用列表
        List<Long> applicationsForUser1 = userApplicationDao.getApplicationsByUserId(1L);

        // 验证用户1是否有1个应用
        assertEquals(1, applicationsForUser1.size());
        // 验证用户1是否有应用1
        assertTrue(applicationsForUser1.contains(1L));

        // 查询用户2的应用列表
        List<Long> applicationsForUser2 = userApplicationDao.getApplicationsByUserId(2L);

        // 验证用户2是否有0个应用
        assertEquals(0, applicationsForUser2.size());
        // 验证用户2是否有应用1
        assertFalse(applicationsForUser2.contains(1L));
    }

    @Test
    @Rollback(value = false)
    public void testGetUsersByApplicationId() {
        // 查询应用1的用户列表
        List<Long> usersForApp1 = userApplicationDao.getUsersByApplicationId(1L);

        // 验证应用1是否有2个用户
        assertEquals(2, usersForApp1.size());
        // 验证应用1是否有用户1和用户2
        assertTrue(usersForApp1.contains(1L));
        assertTrue(usersForApp1.contains(2L));

        // 查询应用2的用户列表
        List<Long> usersForApp2 = userApplicationDao.getUsersByApplicationId(2L);

        // 验证应用2是否有1个用户
        assertEquals(1, usersForApp2.size());
        // 验证应用2是否有用户2
        assertFalse(usersForApp2.contains(2L));
    }
}