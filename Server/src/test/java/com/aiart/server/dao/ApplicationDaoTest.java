package com.aiart.server.dao;

import com.aiart.server.dao.ApplicationDao;
import com.aiart.server.entity.ApplicationEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 使用事务，确保测试数据的回滚
public class ApplicationDaoTest {

    @Autowired
    private ApplicationDao applicationDao;

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testInsertApplication() {
        // 创建一个新的 ApplicationEntity 对象
        ApplicationEntity application = new ApplicationEntity();
        application.setApplicationName("test");
        application.setPublisher("Test Publisher");
        application.setApplicationField("Test Field");
//        application.setServerAddr("10.21.203.222");
//        application.setServerPort(8000);
//        application.setServerurl("http://127.0.0.1:7860/sdapi/v1/txt2img");
        application.setServerurl("http://127.0.0.1:7860/sdapi/v1/img2img");
        // 插入应用程序
        int result = applicationDao.insertApplication(application);
        System.out.println("Insert result: " + result); // 输出插入结果
        System.out.println("User ID: " + application.getApplicationId()); // 输出用户 ID
        assertEquals(1, result); // 插入成功应返回1
        assertNotNull(application.getApplicationId()); // 检查用户ID是否已分配
    }

    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testUpdateApplication() {
//        // 插入一个应用程序以便后续更新
//        ApplicationEntity application = new ApplicationEntity();
//        application.setApplicationName("Test App");
//        application.setPublisher("Test Publisher");
//        application.setApplicationField("Test Field");
//        application.setServerAddr("localhost");
//        application.setServerPort(8080);
//        applicationDao.insertApplication(application);

        Long applicationId =1L;

        // 创建一个新的 ApplicationEntity 对象，用于更新
        ApplicationEntity updatedApplication = new ApplicationEntity();
        updatedApplication.setApplicationId(applicationId);
        updatedApplication.setApplicationName("Updated App");
        updatedApplication.setPublisher("Updated Publisher");
        updatedApplication.setApplicationField("Updated Field");
        updatedApplication.setServerAddr("updatedServerAddr");
        updatedApplication.setServerPort(9090);

        // 更新应用程序信息
        int result = applicationDao.updateApplication(updatedApplication);

        assertEquals(1, result); // 更新成功应返回1

        // 验证更新后的应用程序信息
        ApplicationEntity retrievedApplication = applicationDao.getApplicationById(applicationId);
        assertNotNull(retrievedApplication);
        assertEquals(updatedApplication.getApplicationName(), retrievedApplication.getApplicationName());
        assertEquals(updatedApplication.getPublisher(), retrievedApplication.getPublisher());
    }
    @Test
    @Rollback(value = false)
    public void testGetApplicationById() {
        Long applicationId = 1L;

        // 调用getApplicationById方法获取应用
        ApplicationEntity retrievedApplication = applicationDao.getApplicationById(applicationId);

        // 验证获取的应用是否不为空
        assertNotNull(retrievedApplication);

        // 验证获取的应用的属性是否与预期相符
        assertEquals("Updated App", retrievedApplication.getApplicationName());
        // 验证其他属性
    }
    @Test
    @Rollback(value = false)  // FIXME: add this annotation!
    public void testDeleteApplicationById() {
        // 插入一个应用程序以便后续删除
        ApplicationEntity application = new ApplicationEntity();
        application.setApplicationName("Test App");
        application.setPublisher("Test Publisher");
        application.setApplicationField("Test Field");
        application.setServerAddr("localhost");
        application.setServerPort(8080);
        applicationDao.insertApplication(application);

        Long applicationId = application.getApplicationId();

        // 删除应用程序
        int result = applicationDao.deleteApplicationById(applicationId);

        assertEquals(1, result); // 删除成功应返回1

        // 验证应用程序是否已被删除
        ApplicationEntity deletedApplication = applicationDao.getApplicationById(applicationId);
        assertNull(deletedApplication);
    }
}
