package com.hyphenate.easeim.section.moment.http;

import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.PublishResp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostAPI {

    @Multipart
    @POST("/moment/publish")
    Call<BaseResp> publishMoment(@Query("text_content") String content, @Part MultipartBody.Part file,
                                 @Header("Authorization") String token);

    @POST("/moment/like")
    Call<BaseResp> likeMoment(@Body RequestBody likeRequest, @Header("Authorization") String token);

    @Multipart
    @POST("/moment/comment")
    Call<BaseResp> sendComment(@Part("belonging_id") int belonging_id,@Part("moment_id") int moment_id,
                                 @Part("receiver_id") int receiver_id,@Part("text_content") String text_content,
                                 @Part MultipartBody.Part file,@Header("Authorization") String token);

    @POST("/moment/delete")
    Call<BaseResp> deleteMoment(@Query("moment_id") int moment_id,@Header("Authorization") String token);

}
