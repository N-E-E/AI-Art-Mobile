package com.aiart.server.service;

import com.aiart.server.dao.UserApplicationDao;
import com.aiart.server.entity.UserApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserApplicationService {

    @Autowired
    private UserApplicationDao userApplicationDao;

    public void addUserApplication(Long userId, Long applicationId) {
        UserApplicationEntity userApplication = new UserApplicationEntity();
        userApplication.setUserId(userId);
        userApplication.setApplicationId(applicationId);
        userApplicationDao.insertUserApplication(userApplication);
    }

    public void removeUserApplication(Long userId, Long applicationId) {
        userApplicationDao.deleteUserApplication(userId, applicationId);
    }

    public List<Long> getApplicationsByUserId(Long userId) {
        return userApplicationDao.getApplicationsByUserId(userId);
    }

    public List<Long> getUsersByApplicationId(Long applicationId) {
        return userApplicationDao.getUsersByApplicationId(applicationId);
    }
}

