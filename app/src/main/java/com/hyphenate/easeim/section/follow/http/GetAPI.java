package com.hyphenate.easeim.section.follow.http;

import com.hyphenate.easeim.section.follow.response.UserListResp;
import com.hyphenate.easeim.section.moment.response.BaseResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetAPI {
    @GET("user/follow")
    Call<BaseResp> Follow(@Query("id") int id, @Header("Authorization") String token);

    @GET("user/followerList")
    Call<UserListResp> getFollowerList(@Query("id") int id, @Query("page") int page, @Header("Authorization") String token);

    @GET("user/subscribedList")
    Call<UserListResp> getSubscribedList(@Query("id") int id, @Query("page") int page, @Header("Authorization") String token);

}
