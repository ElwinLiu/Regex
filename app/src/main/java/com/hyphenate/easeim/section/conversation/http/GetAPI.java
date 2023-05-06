package com.hyphenate.easeim.section.conversation.http;

import com.hyphenate.easeim.section.conversation.response.BaseInfoResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetAPI {

    @GET("user/basicInfo")
    Call<BaseInfoResp> getBaseInfo(@Query("id")int id);

}
