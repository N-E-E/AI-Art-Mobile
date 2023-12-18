create database if not exists aiart;
use aiart;

CREATE TABLE if not exists `user` (
                      userId INT AUTO_INCREMENT PRIMARY KEY,
                      `account` VARCHAR(255) NOT NULL,
                      `password` VARCHAR(255) NOT NULL,
                      nickname VARCHAR(255) DEFAULT 'NewUser', -- 设置默认昵称为 'NewUser'
                      gender VARCHAR(255) DEFAULT NULL,
                      model_library JSON DEFAULT NULL,
                      avater VARCHAR(255)
);

CREATE TABLE if not exists application (
                             applicationId INT AUTO_INCREMENT PRIMARY KEY,
                             applicationName VARCHAR(255) NOT NULL,
                             publisher VARCHAR(255) NOT NULL,
                             applicationField VARCHAR(255) NOT NULL,
                             server_addr VARCHAR(255) ,
                             server_port INT ,
                             server_url VARCHAR(255)
);

CREATE TABLE if not exists user_application (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  user_id INT NOT NULL,
                                  application_id INT NOT NULL
);


