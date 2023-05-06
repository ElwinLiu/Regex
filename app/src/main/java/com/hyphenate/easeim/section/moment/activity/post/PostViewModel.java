package com.hyphenate.easeim.section.moment.activity.post;

import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.moment.http.httpHelper;
import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.PublishResp;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {

    private List<String> selected_img;
    private String token;


    public PostViewModel(){
        selected_img = new ArrayList<>();
    }


    /**
     * 发布动态
     */
    public void PostMoment(String content,String path,FinishListener finishListener){
        MultipartBody.Part part;
        if(path!=null){  //上传图片
            File file = new File(path);
            RequestBody fileRQ = RequestBody.create(MediaType.parse("image/*"),file);
            part = MultipartBody.Part.createFormData("file",file.getName(),fileRQ);
        }
        else {  //不上传图片
            part = MultipartBody.Part.createFormData("","");
        }
        Call<BaseResp> call = httpHelper.getPostAPI().publishMoment(content,part,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success) finishListener.onSuccess();
                else finishListener.onFailure(response.body().message);
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    //Getter, Setter
    public List<String> getSelected_img() {
        return selected_img;
    }
    public void setToken(String token){ this.token = token; }


    /**
     * ViewModel业务功能处理完毕监听
     */
    public interface FinishListener{
        void onSuccess();
        void onFailure(String message);
    }

}
