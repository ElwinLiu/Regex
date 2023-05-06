package com.hyphenate.easeim.section.moment.ui.personal;

import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.moment.http.httpHelper;
import com.hyphenate.easeim.section.moment.response.BaseResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalViewModel extends ViewModel {
    private List<MomentListResp.Moment> momentList;
    private int page;
    private int lastPosition;
    private int lastOffset;
    private String token;
    private int id;



    public PersonalViewModel(){
        momentList = new ArrayList<>();
        page = 0;
    }


    /**
     * 请求加载更多的动态
     */
    public void RequestMoreMoment(FinishListener finishListener){
        Call<MomentListResp> call = httpHelper.getGetAPI().getUserMomentList(id,page+1,token);
        call.enqueue(new Callback<MomentListResp>() {
            @Override
            public void onResponse(Call<MomentListResp> call, Response<MomentListResp> response) {
                if(response.body().success) {
                    List<MomentListResp.Moment> momentList = response.body().content;
                    if(momentList!=null){
                        PersonalViewModel.this.momentList.addAll(momentList);
                        page += 1;
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
     * 请求最新的动态
     */
    public void RequestLatestMoment(FinishListener finishListener){
        momentList.clear();
        page = 0;
        RequestMoreMoment(finishListener);
    }


    /**
     * 删除动态
     */
    public void DeleteMoment(int position,FinishListener finishListener){
        MomentListResp.Moment moment = momentList.get(position);
        Call<BaseResp> call = httpHelper.getPostAPI().deleteMoment(moment.moment_id,token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success){
                    momentList.remove(position);
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
    public List<MomentListResp.Moment> getMomentList(){ return momentList; }
    public void setToken(String token){ this.token = token; }
    public void setId(int id){ this.id = id; }
    public int getId(){ return id; }
    public int getLastPosition() { return lastPosition; }
    public void setLastPosition(int lastPosition) { this.lastPosition = lastPosition; }
    public int getLastOffset() { return lastOffset; }
    public void setLastOffset(int lastOffset) { this.lastOffset = lastOffset; }

    /**
     * ViewModel业务功能处理完毕监听
     */
    public interface FinishListener{
        void onSuccess();
        void onFailure(String message);
    }




}
