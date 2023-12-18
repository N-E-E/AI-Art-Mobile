package com.aiart.server.service;

import com.aiart.server.dao.ApplicationDao;
import com.aiart.server.entity.ApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ApplicationService {
    private final ApplicationDao applicationDao; // 使用 ApplicationDao 进行数据访问

    @Autowired
    public ApplicationService(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    public int insertApplication(ApplicationEntity application) {
        return applicationDao.insertApplication(application);
    }

    public ApplicationEntity getApplicationById(Long applicationId) {
        return applicationDao.getApplicationById(applicationId);
    }

    public int updateApplication(ApplicationEntity application) {
        return applicationDao.updateApplication(application);
    }

    public int deleteApplicationById(Long applicationId) {
        return applicationDao.deleteApplicationById(applicationId);
    }

    public String getUrlById(Long applicationId) {
        return applicationDao.getUrlById(applicationId);
    }
    // 在 ApplicationService 中添加一个方法
    public URI getDeploymentAddressById(Long applicationId) {
        // 根据应用 ID 查询应用信息,返回类型URI类型，包括addr和port信息
        ApplicationEntity application = applicationDao.getApplicationById(applicationId);

        if (application != null) {
            // 构建部署地址的 URI
            try {
                String serverAddr = application.getServerAddr();
                int serverPort = application.getServerPort();
                String serverUrl=application.getServerurl();
                // 使用 URI 构建部署地址
                URI deploymentAddress = new URI("http", null, serverAddr, serverPort, null, null, null);

                return deploymentAddress;
            } catch (Exception e) {
                // 处理 URI 构建异常
                e.printStackTrace();
                return null;
            }
        } else {
            // 应用不存在
            return null;
        }
    }
}

