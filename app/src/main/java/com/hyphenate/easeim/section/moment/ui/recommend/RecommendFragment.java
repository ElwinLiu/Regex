package com.hyphenate.easeim.section.moment.ui.recommend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeim.section.chat.activity.ChatActivity;
import com.hyphenate.easeim.section.me.PersonalPage.OthersPageActivity;
import com.hyphenate.easeim.section.moment.activity.moment.MomentActivity;
import com.hyphenate.easeim.section.moment.adapter.MomentAdapter;

import com.hyphenate.easeim.databinding.FragmentRecommendBinding;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.hyphenate.easeui.constants.EaseConstant;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import java.util.List;


public class RecommendFragment extends Fragment {

    private RecommendViewModel mViewModel;
    private FragmentRecommendBinding binding;
    private MomentAdapter adapter;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecommendBinding.inflate(inflater, container, false);
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
                    continue;
                }
                if(momentList.get(i).sender_id.equals(moment.sender_id)&&
                        !momentList.get(i).is_follower.equals(moment.is_follower)){
                    momentList.get(i).is_follower = moment.is_follower;
                    adapter.notifyItemChanged(i);
                }
            }
        }
    }


    /**
     * 加载初始数据
     */
    private void LoadInitData(){
        if(mViewModel.getMomentList().size()==0){  //向后端请求数据
            mViewModel.RequestLatestMoment(new RecommendViewModel.FinishListener() {
                @Override
                public void onSuccess() {
                    adapter.notifyItemRangeChanged(0,mViewModel.getMomentList().size());
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                }
            });
        }else{  //返回原先位置
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.recommendRecycle.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(mViewModel.getLastPosition(), mViewModel.getLastOffset());
        }
    }


    /**
     * 初始化变量
     */
    private void InitVar(){
        //初始化ViewModel
        mViewModel = new ViewModelProvider(getParentFragment()).get(RecommendViewModel.class);
        String token = SharedPreferencesUtils.getToken(getContext());
        mViewModel.setToken(token);

        //初始化adapter
        int id = SharedPreferencesUtils.getId(getContext());
        adapter = new MomentAdapter(mViewModel.getMomentList(), id);
        //设置item点击监听
        adapter.setOnItemClickListener(new MomentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), MomentActivity.class);
                intent.putExtra("moment", mViewModel.getMomentList().get(position));
                startActivity(intent);
            }

            @Override
            public void onFollowClick(int position) {//TODO
                MomentListResp.Moment moment = mViewModel.getMomentList().get(position);
                if (!moment.is_follower) {  //关注
                    mViewModel.Follow(position, new RecommendViewModel.FinishListener() {
                        @Override
                        public void onSuccess() {
                            int sender_id = mViewModel.getMomentList().get(position).sender_id;
                            for (int i = 0; i < mViewModel.getMomentList().size(); i++) {
                                if (mViewModel.getMomentList().get(i).sender_id.equals(sender_id)) {
                                    adapter.notifyItemChanged(i);
                                }
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {  //私聊
                    //TODO: goto chat
                    ChatActivity.actionStart(getContext(),moment.sender_id.toString(), EaseConstant.CHATTYPE_SINGLE);
                }
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
        RefreshLayout refreshLayout = binding.recommendRefresh;
        refreshLayout.setOnMultiListener(new SimpleMultiListener(){
           @Override
           public void onRefresh(@NonNull RefreshLayout refreshLayout){  //刷新获取最新动态
               super.onRefresh(refreshLayout);
               refreshLayout.finishRefresh(2000);
               //TODO: get request
               int num = mViewModel.getMomentList().size();
               mViewModel.getMomentList().clear();
               adapter.notifyItemRangeRemoved(0,num);
               mViewModel.RequestLatestMoment(new RecommendViewModel.FinishListener() {
                   @Override
                   public void onSuccess() {
                       adapter.notifyItemRangeChanged(0, mViewModel.getMomentList().size());
                   }

                   @Override
                   public void onFailure(String message) {
                       Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                   }
               });
           }

           @Override
           public void onLoadMore(@NonNull RefreshLayout refreshLayout){  //下拉加载更多
               super.onLoadMore(refreshLayout);
               refreshLayout.finishLoadMore(2000);
               //TODO: get request
               int num = mViewModel.getMomentList().size();
               mViewModel.RequestMoreMoment(new RecommendViewModel.FinishListener() {
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
        //TODO: implement adapter
        RecyclerView recyclerView = binding.recommendRecycle;
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