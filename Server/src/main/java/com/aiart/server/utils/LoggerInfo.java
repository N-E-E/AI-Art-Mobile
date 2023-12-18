package com.aiart.server.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.aiart.server.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerInfo {
    private boolean outputToConsole;//true-输出到控制台，false输出到journal日志
    public LoggerInfo(boolean outputToConsole){
        this.outputToConsole=outputToConsole;
    }
    public void configureLogger(LoggerContext context, String configFile) {
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);

        try {
            context.reset();
            // 使用类路径路径加载配置文件
            if ("console-logback.xml".equals(configFile)) {
                configurator.doConfigure("src/main/resources/logback-console.xml");
            } else if ("journal-logback.xml".equals(configFile)) {
                configurator.doConfigure("src/main/resources/logback-journal.xml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 切换日志输出目标
    public void toggleLogOutput() {
        // 清除所有以前的配置
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        // 根据 outputToConsole 的值选择要加载的配置文件
        if (outputToConsole) {
            configureLogger(context, "console-logback.xml");
        } else {
            configureLogger(context, "journal-logback.xml");
        }
    }
}
