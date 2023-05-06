package com.hyphenate.easeim.section.conversation.http;

import com.hyphenate.easeim.section.conversation.http.GetAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class httpHelper {
    static private String baseURL;
    static private Retrofit retrofit;


    static {
        baseURL = "http://106.14.78.79:40010";  //TODO
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).
                retryOnConnectionFailure(true).build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())  //json数据解析器
                .baseUrl(baseURL)
                .client(client)
                .build();
    }

    /**
     * 获取GetAPI
     * @return
     */
    static public com.hyphenate.easeim.section.conversation.http.GetAPI getGetAPI(){
        return retrofit.create(GetAPI.class);
    }


}
