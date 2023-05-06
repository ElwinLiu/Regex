package com.hyphenate.easeim.section.moment.activity.moment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.moment.http.httpHelper;
import com.hyphenate.easeim.section.moment.request.LikeRequest;
import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.CommentListResp;
import com.hyphenate.easeim.section.moment.response.FollowResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.google.gson.Gson;
import com.hyphenate.easeim.section.moment.response.MomentResp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MomentViewModel extends ViewModel {
    private MutableLiveData<MomentListResp.Moment> moment;
    private List<String> selected_img;
    private final List<CommentListResp.Comment> commentList;
    private String token;  //token
    private int id;  //用户id
    private int page = 0;


    public MomentViewModel(){
        moment = new MutableLiveData<>();
        selected_img = new ArrayList<>();
        commentList = new ArrayList<>();
    }


    /**
     * 测试数据
     */
    public void TestData(FinishListener finishListener){
        /*Call<Comment> call = httpHelper.getGetAPI().getComment();
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                for(int i=0;i<5;i++){
                    commentList.add(new Comment(0, 0, null, "ynmtdj",
                            "cool!", null, new Date(System.currentTimeMillis())));
                    commentList.add(new Comment(1, 1, null, "hunan",
                            "很棒!", null, new Date(System.currentTimeMillis())));
                }
                finishListener.onSuccess();
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                for(int i=0;i<5;i++){
                    commentList.add(new Comment(0, 0, null, "ynmtdj",
                            "cool!", null, new Date(System.currentTimeMillis())));
                    commentList.add(new Comment(1, 1, null, "hunan",
                            "很棒!", null, new Date(System.currentTimeMillis())));
                }
                finishListener.onFailure("成功");
            }
        });*/
    }


    /**
     * 获取最新评论
     */
    public void RequestLatestComment(int moment_id, FinishListener finishListener){
        page = 0;
        commentList.clear();
        RequestMoreComment(moment_id, finishListener);
    }


    /**
     * 请求更多的评论
     */
    public void RequestMoreComment(int moment_id, FinishListener finishListener){
        //TODO: Request More Comment
        Call<CommentListResp> call = httpHelper.getGetAPI().getCommentList(page+1,moment_id);
        call.enqueue(new Callback<CommentListResp>() {
            @Override
            public void onResponse(Call<CommentListResp> call, Response<CommentListResp> response) {
                if(response.body().success){
                    List<CommentListResp.Comment> commentList = response.body().content;
                    if(commentList!=null){
                        MomentViewModel.this.commentList.addAll(commentList);
                        page += 1;
                    }
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<CommentListResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    /**
     * 关注动态的发布者
     */
    public void Follow(FinishListener finishListener){
        MomentListResp.Moment moment = this.moment.getValue();
        //关注
        Call<BaseResp> call = httpHelper.getGetAPI().Follow(moment.sender_id,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success) {
                    moment.is_follower = true;
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    /**
     * 点赞或取消点赞
     */
    public void Like(boolean like,FinishListener finishListener){
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.like_status = !like;
        likeRequest.moment_id = moment.getValue().moment_id;
        Gson gson = new Gson();
        String postInfoStr = gson.toJson(likeRequest);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postInfoStr);
        Call<BaseResp> call = httpHelper.getPostAPI().likeMoment(requestBody,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success){
                    moment.getValue().is_liked = like;
                    if(like) moment.getValue().like_num += 1;
                    else moment.getValue().like_num -= 1;
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    /**
     * 发送评论
     */
    public void SendComment(String content,String path,FinishListener finishListener){
        MultipartBody.Part part;
        if(path!=null){  //上传图片
            File file = new File(path);
            RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"),file);
            part = MultipartBody.Part.createFormData("file",file.getName(),fileRQ);
        }
        else {  //不上传图片
            part = MultipartBody.Part.createFormData("","");
        }
        Call<BaseResp> call = httpHelper.getPostAPI().sendComment(0,moment.getValue().moment_id,0,content,part,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success){
                    moment.getValue().comment_num += 1;
                    selected_img.clear();
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    /**
     * 请求动态详情
     */
    public void RequestMoment(int moment_id, FinishListener finishListener){
        Call<MomentResp> call = httpHelper.getGetAPI().getMoment(moment_id,token);
        call.enqueue(new Callback<MomentResp>() {
            @Override
            public void onResponse(Call<MomentResp> call, Response<MomentResp> response) {
                if(response.body().success){
                    MomentListResp.Moment  moment = response.body().content;
                    MomentViewModel.this.moment.setValue(moment);
                    finishListener.onSuccess();
                }
                else {
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<MomentResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    //Getter, Setter
    public MutableLiveData<MomentListResp.Moment> getMoment(){ return moment; }
    public void setMoment(MomentListResp.Moment moment){ this.moment.setValue(moment); }
    public List<String> getSelectedImg(){ return selected_img;}
    public List<CommentListResp.Comment> getCommentList(){ return commentList; }
    public void setToken(String token){ this.token = token; }
    public void setId(int id){ this.id = id; }
    public int getId(){ return id; }


    /**
     * ViewModel业务功能处理完毕监听
     */
    public interface FinishListener{
        void onSuccess();
        void onFailure(String message);
    }



}
