package com.hyphenate.easeim.section.moment.ui.follow;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.chat.activity.ChatActivity;
import com.hyphenate.easeim.section.me.PersonalPage.OthersPageActivity;
import com.hyphenate.easeim.section.moment.activity.moment.MomentActivity;
import com.hyphenate.easeim.section.moment.activity.moment.MomentViewModel;
import com.hyphenate.easeim.section.moment.adapter.MomentAdapter;

import com.hyphenate.easeim.databinding.FragmentFollowBinding;
import com.hyphenate.easeim.databinding.FragmentRecommendBinding;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.ui.recommend.RecommendViewModel;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.hyphenate.easeui.constants.EaseConstant;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import java.util.List;

public class FollowFragment extends Fragment {

    private FollowViewModel mViewModel;
    private FragmentFollowBinding binding;
    private MomentAdapter adapter;

    public static FollowFragment newInstance() {
        return new FollowFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentFollowBinding.inflate(inflater, container, false);
        InitVar();  //初始化变量
        InitUI();  //初始化UI控件
        LoadInitData();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        MomentListResp.Moment moment = MomentListResp.Moment.updated_moment;
        if(moment!=null){  //更新动态
            List<MomentListResp.Moment> momentList = mViewModel.getMomentList();
            for(int i=0;i<momentList.size();i++){
                if(momentList.get(i).moment_id.equals(moment.moment_id)){
                    momentList.set(i,moment);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }


    /**
     * 加载初始数据
     */
    public void LoadInitData(){
        if(mViewModel.getMomentList().size()==0){  //向后端请求数据
            mViewModel.RequestLatestMoment(new FollowViewModel.FinishListener() {
                @Override
                public void onSuccess() {
                    if(mViewModel.getMomentList().size()==0){  //关注暂无动态
                        binding.followBackground.setVisibility(View.VISIBLE);
                        binding.followIcon.setVisibility(View.VISIBLE);
                    }
                    else {
                        binding.followBackground.setVisibility(View.GONE);
                        binding.followIcon.setVisibility(View.GONE);
                        adapter.notifyItemRangeChanged(0,mViewModel.getMomentList().size());
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                }
            });
        }
        else{  //回到原先位置
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.followRecycle.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(mViewModel.getLastPosition(), mViewModel.getLastOffset());
            binding.followBackground.setVisibility(View.GONE);
            binding.followIcon.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化变量
     */
    private void InitVar(){
        //初始化mViewModel
        mViewModel = new ViewModelProvider(getParentFragment()).get(FollowViewModel.class);
        String token = SharedPreferencesUtils.getToken(getContext());
        mViewModel.setToken(token);

        //初始化adapter
        int id = SharedPreferencesUtils.getId(getContext());
        adapter = new MomentAdapter(mViewModel.getMomentList(),id);
        //设置item点击监听
        adapter.setOnItemClickListener(new MomentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(getContext(), MomentActivity.class);
                intent.putExtra("moment",mViewModel.getMomentList().get(position));
                startActivity(intent);
            }

            @Override
            public void onFollowClick(int position) {//TODO
                //TODO: goto chat
                MomentListResp.Moment moment = mViewModel.getMomentList().get(position);
                ChatActivity.actionStart(getContext(),moment.sender_id.toString(), EaseConstant.CHATTYPE_SINGLE);
            }

            @Override
            public void onAvatarClick(int position) {//TODO
                MomentListResp.Moment moment = mViewModel.getMomentList().get(position);
                OthersPageActivity.actionStart(getContext(), moment.sender_id);
            }
        });
    }


    /**
     * 初始化UI控件
     */
    private void InitUI(){
        //初始化SmartRefreshLayout
        RefreshLayout refreshLayout = binding.followRefresh;
        refreshLayout.setOnMultiListener(new SimpleMultiListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout){  //刷新获取最新动态
                super.onRefresh(refreshLayout);
                refreshLayout.finishRefresh(2000);
                mViewModel.RequestLatestMoment(new FollowViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        if(mViewModel.getMomentList().size()==0){  //关注暂无动态
                            binding.followBackground.setVisibility(View.VISIBLE);
                            binding.followIcon.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.followBackground.setVisibility(View.GONE);
                            binding.followIcon.setVisibility(View.GONE);
                            adapter.notifyItemRangeChanged(0,mViewModel.getMomentList().size());
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
                int num = mViewModel.getMomentList().size();
                mViewModel.RequestMoreMoment(new FollowViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyItemRangeChanged(num,mViewModel.getMomentList().size()-num);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //初始化RecycleView
        RecyclerView recyclerView = binding.followRecycle;
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
                    int offset = 0;
                    if(firstView!=null) offset = firstView.getTop();
                    mViewModel.setLastPosition(firstPosition);
                    mViewModel.setLastOffset(offset);
                }
            }
        });
    }

}