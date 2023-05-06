package com.hyphenate.easeim.section.moment.http;

import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.CommentListResp;
import com.hyphenate.easeim.section.moment.response.FollowResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.response.MomentResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GetAPI {

    @GET("user/follow")
    Call<BaseResp> Follow(@Query("id") int id, @Header("Authorization") String token);

    @GET("moment/squareList")
    Call<MomentListResp> getMomentList(@Query("page")int page,@Header("Authorization") String token);

    @GET("moment/commentList")
    Call<CommentListResp> getCommentList(@Query("page") int page,@Query("moment_id") int moment_id);

    @GET("user/momentList")
    Call<MomentListResp> getUserMomentList(@Query("id") int id,@Query("page") int page,@Header("Authorization") String token);

    @GET("moment/followedList")
    Call<MomentListResp> getFollowerMomentList(@Query("page") int page,@Header("Authorization") String token);

    @GET("moment/getDetail")
    Call<MomentResp> getMoment(@Query("moment_id") int moment_id, @Header("Authorization") String token);

}
