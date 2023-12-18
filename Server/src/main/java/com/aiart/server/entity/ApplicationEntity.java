package com.aiart.server.entity;

import lombok.Data;

@Data//自动生成getter和setter方法
public class ApplicationEntity {
    private Long applicationId; // 应用 ID
    private String applicationName; // 应用名称
    private String publisher; // 发布者
    private String applicationField; // 应用领域

    private String serverAddr;  // addr
    private int serverPort;  // port
    private String serverurl;
}
