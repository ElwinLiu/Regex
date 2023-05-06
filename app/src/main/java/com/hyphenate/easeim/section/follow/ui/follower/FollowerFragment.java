package com.hyphenate.easeim.section.follow.ui.follower;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeim.databinding.FragmentFollowerBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.databinding.FragmentFollowBinding;
import com.hyphenate.easeim.section.follow.adapter.UserAdapter;
import com.hyphenate.easeim.section.follow.response.UserListResp;
import com.hyphenate.easeim.section.me.PersonalPage.OthersPageActivity;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.ui.follow.FollowViewModel;
import com.hyphenate.easeim.section.moment.ui.recommend.RecommendViewModel;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

public class FollowerFragment extends Fragment {

    private FollowerViewModel mViewModel;
    private FragmentFollowerBinding binding;
    private UserAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFollowerBinding.inflate(inflater, container, false);
        InitVar();  //初始化变量
        InitUI();  //初始化UI控件
        LoadInitData();
        return binding.getRoot();
    }


    private void LoadInitData(){
        if(mViewModel.getUserList().size()==0){
            mViewModel.RequestLatestUser(new FollowerViewModel.FinishListener() {
                @Override
                public void onSuccess() {
                    if(mViewModel.getUserList().size()==0){
                        binding.followerIcon.setVisibility(View.VISIBLE);
                        binding.followerBackground.setVisibility(View.VISIBLE);
                    }
                    else{
                        adapter.notifyItemRangeChanged(0,mViewModel.getUserList().size());
                        binding.followerIcon.setVisibility(View.GONE);
                        binding.followerBackground.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.followerRecycle.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(mViewModel.getLastPosition(), mViewModel.getLastOffset());
            binding.followerBackground.setVisibility(View.GONE);
            binding.followerIcon.setVisibility(View.GONE);
        }
    }


    private void InitVar(){
        //初始化ViewModel
        mViewModel = new ViewModelProvider(this).get(FollowerViewModel.class);
        String token = SharedPreferencesUtils.getToken(getContext());
        int id = SharedPreferencesUtils.getId(getContext());
        mViewModel.setToken(token);
        mViewModel.setId(id);

        //初始化adapter
        adapter = new UserAdapter(mViewModel.getUserList());
        adapter.setItemClickListener(new UserAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {  //TODO: goto homepage
                UserListResp.User user = mViewModel.getUserList().get(position);
                OthersPageActivity.actionStart(getContext(), user.id);
            }

            @Override
            public void onFollowClick(int position) {
                mViewModel.Follow(position, new FollowerViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void InitUI(){
        //初始化SmartRefresh
        SmartRefreshLayout refreshLayout = binding.followerRefresh;
        refreshLayout.setOnMultiListener(new SimpleMultiListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout){  //刷新获取最新关注
                super.onRefresh(refreshLayout);
                refreshLayout.finishRefresh(2000);
                int num = mViewModel.getUserList().size();
                mViewModel.getUserList().clear();
                adapter.notifyItemRangeRemoved(0,num);
                mViewModel.RequestLatestUser(new FollowerViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        if(mViewModel.getUserList().size()==0){  //暂无关注
                            binding.followerBackground.setVisibility(View.VISIBLE);
                            binding.followerIcon.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.followerBackground.setVisibility(View.GONE);
                            binding.followerIcon.setVisibility(View.GONE);
                            adapter.notifyItemRangeChanged(0,mViewModel.getUserList().size());
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout){  //下拉加载更多
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadMore(2000);
                int num = mViewModel.getUserList().size();
                mViewModel.RequestMoreUser(new FollowerViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyItemRangeChanged(num,mViewModel.getUserList().size()-num);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //初始化Recyclerview
        RecyclerView recyclerView = binding.followerRecycle;
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时处理
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取当前滚动到的条目位置
                    LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                    int firstPosition = layoutManager.findFirstVisibleItemPosition();
                    View firstView = layoutManager.findViewByPosition(firstPosition);
                    int offset = firstView.getTop();
                    mViewModel.setLastPosition(firstPosition);
                    mViewModel.setLastOffset(offset);
                }
            }
        });
    }

}