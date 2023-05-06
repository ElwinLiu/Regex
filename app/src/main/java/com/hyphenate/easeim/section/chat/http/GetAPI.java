package com.hyphenate.easeim.section.chat.http;

import com.hyphenate.easeim.section.chat.response.UserDetailResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetAPI {

    @GET("user/detail")
    Call<UserDetailResp> getUserDetail(@Query("id")int id, @Header("Authorization")String token);


}
