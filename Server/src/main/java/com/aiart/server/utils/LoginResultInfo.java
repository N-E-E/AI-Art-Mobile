package com.aiart.server.utils;

public class LoginResultInfo {
    private UserResult userResult;
    private Long userId;

    // 构造方法
    public LoginResultInfo(UserResult userResult, Long userId) {
        this.userResult = userResult;
        this.userId = userId;
    }

    // getter 和 setter 方法
    public UserResult getUserResult() {
        return userResult;
    }

    public void setUserResult(UserResult userResult) {
        this.userResult = userResult;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
