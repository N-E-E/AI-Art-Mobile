package net.penguincoders.aipainting.util;

public class ServerConfig {
    public static final String SERVER_URL = "http://10.21.207.15"; // 服务器URL
    public static final int SERVER_PORT = 8001; // 服务器端口

    public static String getServerAddress() {
        return SERVER_URL + ":" + SERVER_PORT;
    }
}
