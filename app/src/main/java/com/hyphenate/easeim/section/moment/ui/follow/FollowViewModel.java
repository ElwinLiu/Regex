package com.hyphenate.easeim.section.moment.ui.follow;

import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.moment.adapter.MomentAdapter;
import com.hyphenate.easeim.section.moment.http.httpHelper;
import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final List<MomentListResp.Moment> momentList;
    private int cur_page;
    private int lastPosition;
    private int lastOffset;
    private String token;

    public FollowViewModel(){
        momentList = new ArrayList<>();
        cur_page = 0;
    }


    /**
     * 测试数据
     */
    public void TestData(FinishListener finishListener){
        /*Call<Moment> call = httpHelper.getGetAPI().getMoment();
        call.enqueue(new Callback<Moment>() {
            @Override
            public void onResponse(Call<Moment> call, Response<Moment> response) {
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
     * 获取关注的最新动态
     */
    public void RequestLatestMoment(FinishListener finishListener){
        cur_page = 0;
        momentList.clear();
        RequestMoreMoment(finishListener);
    }


    /**
     * 加载更多关注的动态
     */
    public void RequestMoreMoment(FinishListener finishListener){
        Call<MomentListResp> call = httpHelper.getGetAPI().getFollowerMomentList(cur_page+1,token);
        call.enqueue(new Callback<MomentListResp>() {
            @Override
            public void onResponse(Call<MomentListResp> call, Response<MomentListResp> response) {
                if(response.body().success){
                    List<MomentListResp.Moment> momentList = response.body().content;
                    if(momentList!=null){
                        FollowViewModel.this.momentList.addAll(momentList);
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


    //Getter, Setter
    public List<MomentListResp.Moment> getMomentList() {
        return momentList;
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
    public void setToken(String token){
        this.token = token;
    }


    /**
     * ViewModel业务功能处理完毕监听
     */
    public interface FinishListener{
        void onSuccess();
        void onFailure(String message);
    }



}