package com.hyphenate.easeim.section.moment.ui.recommend;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.moment.adapter.MomentAdapter;

import com.hyphenate.easeim.section.moment.http.httpHelper;
import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.FollowResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final List<MomentListResp.Moment> momentList;
    private int cur_page;
    private String token;
    private int lastPosition;  //用于记录recycleviewr的位置
    private int lastOffset;

    public RecommendViewModel(){
        momentList = new ArrayList<>();
        cur_page = 0;
    }


    /**
     * 测试数据
     */
    public void TestData(FinishListener finishListener){
        /*        Call<MomentListResp.Moment> call = httpHelper.getGetAPI().getMoment();
        call.enqueue(new Callback<MomentListResp.Moment>() {
            @Override
            public void onResponse(Call<MomentListResp.Moment> call, Response<MomentListResp.Moment> response) {
                momentList.add(new Moment(0,0,null,"ynmtdj",true,new Date(System.currentTimeMillis()),
                        "大家好我是三年半的练习生，喜欢唱跳、rap、篮球",null,0,0,false,0));
                momentList.add(new Moment(1,1,null,"hunan",false,new Date(System.currentTimeMillis()),
                        "CUGer, 在线征婚！！！\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n2",null,0,0,true,0));
                finishListener.onSuccess();
            }

            @Override
            public void onFailure(Call<Moment> call, Throwable t) {
                momentList.add(new Moment(0,0,null,"ynmtdj",true,new Date(System.currentTimeMillis()),
                        "大家好我是三年半的练习生，喜欢唱跳、rap、篮球",null,0,0,false,0));
                momentList.add(new Moment(1,1,null,"hunan",false,new Date(System.currentTimeMillis()),
                        "CUGer, 在线征婚！！！\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n2",null,0,0,true,0));
                finishListener.onFailure("成功");
            }
        });*/
    }


    /**
     * 获取最新的动态
     */
    public void RequestLatestMoment(FinishListener finishListener){
        //TODO: request latest Moment
        cur_page = 0;
        momentList.clear();
        RequestMoreMoment(finishListener);
    }


    /**
     * 向后端请求更多的动态
     */
    public void RequestMoreMoment(FinishListener finishListener){
        //TODO: request more Moment
        Call<MomentListResp> call = httpHelper.getGetAPI().getMomentList(cur_page+1,token);
        call.enqueue(new Callback<MomentListResp>() {
            @Override
            public void onResponse(Call<MomentListResp> call, Response<MomentListResp> response) {
                if(response.body().success){
                    List<MomentListResp.Moment> momentList = response.body().content;
                    if(momentList!=null){
                        RecommendViewModel.this.momentList.addAll(momentList);
                        cur_page += 1;
                    }
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<MomentListResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });
    }


    /**
     * 关注发布动态的用户
     */
    public void Follow(int position,FinishListener finishListener){
        MomentListResp.Moment moment = momentList.get(position);
        //关注
        Call<BaseResp> call = httpHelper.getGetAPI().Follow(moment.sender_id,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success) {
                    for(MomentListResp.Moment moment1:momentList){
                        if(moment1.sender_id.equals(moment.sender_id)){
                            moment1.is_follower = true;
                        }
                    }
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


    //Getter, Setter
    public List<MomentListResp.Moment> getMomentList() {
        return momentList;
    }
    public void setToken(String token){
        this.token = token;
    }
    public int getLastPosition() {
        return lastPosition;
    }
    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }
    public int getLastOffset() {
        return lastOffset;
    }
    public void setLastOffset(int lastOffset) {
        this.lastOffset = lastOffset;
    }

    /**
     * ViewModel业务功能处理完毕监听
     */
    public interface FinishListener{
        void onSuccess();
        void onFailure(String message);
    }



}