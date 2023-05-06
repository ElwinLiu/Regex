package com.hyphenate.easeim.section.moment.ui.personal;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hyphenate.easeim.section.moment.activity.moment.MomentActivity;
import com.hyphenate.easeim.section.moment.adapter.MomentAdapter;
import com.hyphenate.easeim.databinding.FragmentPersonalBinding;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.databinding.FragmentRecommendBinding;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.ui.recommend.RecommendViewModel;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.List;


public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;
    private PersonalViewModel mViewModel;
    private MomentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater, container, false);
        InitVar();
        InitUI();
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
    private void LoadInitData(){
        if(mViewModel.getMomentList().size()==0){  //请求个人动态
            mViewModel.RequestLatestMoment(new PersonalViewModel.FinishListener() {
                @Override
                public void onSuccess() {
                    if(mViewModel.getMomentList().size()==0){
                        binding.personalBackground.setVisibility(View.VISIBLE);
                        binding.personalIcon.setVisibility(View.VISIBLE);
                    }
                    else{
                        binding.personalBackground.setVisibility(View.GONE);
                        binding.personalIcon.setVisibility(View.GONE);
                        adapter.notifyItemRangeChanged(0,mViewModel.getMomentList().size());
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                }
            });
        }
        else{  //返回原先位置
            LinearLayoutManager layoutManager = (LinearLayoutManager) binding.personalRecycle.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(mViewModel.getLastPosition(), mViewModel.getLastOffset());
            binding.personalBackground.setVisibility(View.GONE);
            binding.personalIcon.setVisibility(View.GONE);
        }
    }


    /**
     * 初始化变量
     */
    private void InitVar(){
        //初始化ViewModel
        mViewModel = new ViewModelProvider(this).get(PersonalViewModel.class);
        String token = SharedPreferencesUtils.getToken(getContext());
        int id = SharedPreferencesUtils.getId(getContext());
        mViewModel.setToken(token);
        int u_id = getActivity().getIntent().getIntExtra("u_id",id);  //默认显示自己的动态列表
        if(mViewModel.getId()!=u_id) mViewModel.getMomentList().clear();
        mViewModel.setId(u_id);

        //初始化adapter
        adapter = new MomentAdapter(mViewModel.getMomentList(),id);
        adapter.setShowInfo(false);
        adapter.setOnItemClickListener(new MomentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(getContext(), MomentActivity.class);
                intent.putExtra("moment",mViewModel.getMomentList().get(position));
                startActivity(intent);
            }
            @Override
            public void onFollowClick(int position) {}
            @Override
            public void onAvatarClick(int position) {}
        });
    }


    /**
     * 初始化UI
     */
    private void InitUI(){
        //初始化SmartRefreshLayout
        RefreshLayout refreshLayout = binding.personalRefresh;
        refreshLayout.setOnMultiListener(new SimpleMultiListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout){  //刷新获取最新动态
                super.onRefresh(refreshLayout);
                refreshLayout.finishRefresh(2000);
                int num = mViewModel.getMomentList().size();
                mViewModel.getMomentList().clear();
                adapter.notifyItemRangeRemoved(0,num);
                mViewModel.RequestLatestMoment(new PersonalViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        if(mViewModel.getMomentList().size()==0){
                            binding.personalBackground.setVisibility(View.VISIBLE);
                            binding.personalIcon.setVisibility(View.VISIBLE);
                        }
                        else{
                            binding.personalBackground.setVisibility(View.GONE);
                            binding.personalIcon.setVisibility(View.GONE);
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
                //TODO: get request
                int num = mViewModel.getMomentList().size();
                mViewModel.RequestMoreMoment(new PersonalViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        if(mViewModel.getMomentList().size()==0){
                            binding.personalBackground.setVisibility(View.VISIBLE);
                            binding.personalIcon.setVisibility(View.VISIBLE);
                        }
                        else{
                            binding.personalBackground.setVisibility(View.GONE);
                            binding.personalIcon.setVisibility(View.GONE);
                            adapter.notifyItemRangeChanged(num,mViewModel.getMomentList().size()-num);
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        //初始化RecycleView
        int id = SharedPreferencesUtils.getId(getContext());
        SwipeRecyclerView recyclerView = binding.personalRecycle;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if(mViewModel.getId()==id){  //仅可以管理自己的动态
            //创建右侧菜单
            SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
                @Override
                public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                    deleteItem.setImage(R.drawable.ic_delete);
                    deleteItem.setBackgroundColor(Color.rgb(255,0,0));
                    deleteItem.setHeight(MATCH_PARENT);
                    deleteItem.setWidth(200);
                    rightMenu.addMenuItem(deleteItem);
                }
            };
            //设置监听器
            recyclerView.setSwipeMenuCreator(swipeMenuCreator);
            //菜单点击监听
            recyclerView.setOnItemMenuClickListener(new OnItemMenuClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
                    // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                    menuBridge.closeMenu();
                    ShowDeleteDialog(adapterPosition);
                }
            });
        }
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
        recyclerView.setAdapter(adapter);
    }


    /**
     * 显示删除对话框
     */
    private void ShowDeleteDialog(int position){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        normalDialog.setMessage("确定要删除这条动态吗？");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mViewModel.DeleteMoment(position, new PersonalViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        if(mViewModel.getMomentList().size()==0){
                            binding.personalBackground.setVisibility(View.VISIBLE);
                            binding.personalIcon.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = normalDialog.show();  // 显示
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#04AEF0"));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#04AEF0"));
    }



}