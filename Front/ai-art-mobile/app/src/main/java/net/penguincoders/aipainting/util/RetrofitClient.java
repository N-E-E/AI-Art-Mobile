package net.penguincoders.aipainting.util;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

//    public static Retrofit getClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(ServerConfig.getServerAddress())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                    .readTimeout(30, TimeUnit.SECONDS)    // 设置读取超时时间
                    .writeTimeout(30, TimeUnit.SECONDS)   // 设置写入超时时间
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ServerConfig.getServerAddress())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)  // 将自定义的 OkHttpClient 实例传递给 Retrofit
                    .build();
        }
        return retrofit;
    }


}

