package com.hyphenate.easeim.section.follow.ui.subscribed;

import androidx.lifecycle.ViewModel;

import com.hyphenate.easeim.section.follow.http.httpHelper;
import com.hyphenate.easeim.section.follow.response.UserListResp;
import com.hyphenate.easeim.section.follow.ui.follower.FollowerViewModel;
import com.hyphenate.easeim.section.moment.response.BaseResp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribedViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private String token;
    private int id;
    private List<UserListResp.User> userList;
    private int page;
    private int lastPosition;
    private int lastOffset;

    public SubscribedViewModel(){
        userList = new ArrayList<>();
        page = 0;
    }


    public void RequestLatestUser(FinishListener finishListener){
        userList.clear();
        page = 0;
        RequestMoreUser(finishListener);
    }


    public void RequestMoreUser(FinishListener finishListener){
        Call<UserListResp> call = httpHelper.getGetAPI().getSubscribedList(id,page+1,token);
        call.enqueue(new Callback<UserListResp>() {
            @Override
            public void onResponse(Call<UserListResp> call, Response<UserListResp> response) {
                if(response.body().success){
                    List<UserListResp.User> userList = response.body().content;
                    if(userList!=null){
                        SubscribedViewModel.this.userList.addAll(userList);
                        page += 1;
                    }
                    finishListener.onSuccess();
                }
                else{
                    finishListener.onFailure(response.body().message);
                }
            }

            @Override
            public void onFailure(Call<UserListResp> call, Throwable t) {
                finishListener.onFailure(t.toString());
            }
        });

    }


    public void Follow(int position, FollowerViewModel.FinishListener finishListener){
        UserListResp.User user = userList.get(position);
        Call<BaseResp> call = httpHelper.getGetAPI().Follow(user.id, token);
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if(response.body().success){
                    userList.get(position).subscribe_status = !userList.get(position).subscribe_status;
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
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<UserListResp.User> getUserList() { return userList; }
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