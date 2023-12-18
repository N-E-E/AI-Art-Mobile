package com.aiart.server.service;

import com.aiart.server.dao.UserDao;
import com.aiart.server.entity.UserEntity;
import com.aiart.server.utils.LoginResultInfo;
import com.aiart.server.utils.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    private final UserDao userDao; // 使用 UserDao 进行数据访问

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int insertUser(UserEntity user) {
        return userDao.insertUser(user);
    }

    public UserEntity getUserById(Long userId) {
        return userDao.getUserById(userId);
    }

    public int updateUser(UserEntity user) {
        return userDao.updateUser(user);
    }

    public int deleteUserById(Long userId) {
        return userDao.deleteUserById(userId);
    }
    public LoginResultInfo login(String account, String password) {  //根据账号密码登录
        // First, check if the account exists in the database.
        UserEntity user = userDao.getUserByAccount(account);

        if (user == null) {
            // Account not found in the database.
            return new LoginResultInfo(UserResult.ACCOUNT_NOT_FOUND, null);
        }

        // Compare the provided password with the stored password hash.
        if (user.getPassword().equals(password)) {
            // Passwords match, login successful.
            return new LoginResultInfo(UserResult.SUCCESS, user.getUserId());
        } else {
            // Password is incorrect.
            return new LoginResultInfo(UserResult.INVALID_CREDENTIALS, null);
        }
    }

    public UserResult register(String account, String password) {//根据账号和密码注册
        // First, check if the account already exists in the database.
        UserEntity existingUser = userDao.getUserByAccount(account);

        if (existingUser != null) {
            // Account already exists in the database.
            return UserResult.ACCOUNT_EXISTS;
        } else {
            // Create a new UserEntity with the provided account and password.
            UserEntity newUser = new UserEntity();
            newUser.setAccount(account);
            newUser.setPassword(password);

            // Insert the new user into the database.
            int rowsInserted = userDao.insertUser(newUser);

            if (rowsInserted > 0) {
                // Registration successful.
                return UserResult.SUCCESS;
            } else {
                // Registration failed for some reason (e.g., database error).
                return UserResult.REGISTER_ERROR; // You can define an appropriate result for this case.
            }
        }
    }

    public UserResult smsLogin(String phoneNumber) {  //已经验证过验证码，直接手机号登录
        UserEntity user = userDao.getUserByAccount(phoneNumber);
        if (user == null) {
            // Account not found in the database.

            return UserResult.ACCOUNT_NOT_FOUND;
        }
        // TODO:You may perform additional checks if needed for phone number login.

        return UserResult.SUCCESS;
    }

    public UserResult isPhoneNumberRegistered(String phoneNumber) {//判断该手机号是否已经注册
        UserEntity user = userDao.getUserByAccount(phoneNumber);
        if (user == null) {
            // Account not found in the database.
            return UserResult.ACCOUNT_NOT_FOUND;
        }
        return UserResult.SUCCESS;
    }

    public boolean updateUserInfo(Long userId, String newNickname, String newGender) {//更新user信息
            return userDao.updateUserInfo(userId,newNickname,newGender);
    }
}

