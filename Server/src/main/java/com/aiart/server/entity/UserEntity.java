package com.aiart.server.entity;

import lombok.Data;

// User.java
@Data
public class UserEntity {
    private Long userId;
    private String account; // 账号/手机号
    private String password;
    private String nickname;
    private String gender;
    private String modelLibrary; // 存储模型库的 JSON 字段
    private String avater;

    // 省略 getter 和 setter 方法
    // 为 userId 字段单独添加 setter 方法

}
