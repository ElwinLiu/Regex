package com.hyphenate.easeim.section.moment.activity.moment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeim.MainActivity;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.chat.activity.ChatActivity;
import com.hyphenate.easeim.section.me.PersonalPage.OthersPageActivity;
import com.hyphenate.easeim.section.moment.adapter.CommentAdapter;

import com.hyphenate.easeim.databinding.ActivityMomentBinding;
import com.hyphenate.easeim.section.moment.response.CommentListResp;
import com.hyphenate.easeim.section.moment.response.MomentListResp;
import com.hyphenate.easeim.section.moment.utils.FileUriConvertUtils;
import com.hyphenate.easeim.section.moment.utils.PhotoUtils;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.hyphenate.easeim.section.moment.view.ImageContainerView;
import com.hyphenate.easeui.constants.EaseConstant;
import com.iceteck.silicompressorr.SiliCompressor;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.util.List;


public class MomentActivity extends AppCompatActivity {

    private MomentViewModel mViewModel;
    private ActivityMomentBinding binding;
    private CommentAdapter adapter;
    private final int PhotoReqCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMomentBinding.inflate(getLayoutInflater());  //ViewBinding
        setContentView(binding.getRoot());
        InitVar();
        InitUI();
        //TODO: init request data
        //获取activity传递过来的moment
        Intent intend = getIntent();
        MomentListResp.Moment moment = (MomentListResp.Moment) intend.getSerializableExtra("moment");
        mViewModel.RequestMoment(moment.moment_id, new MomentViewModel.FinishListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
        //获取最新动态
        mViewModel.getCommentList().clear();
        mViewModel.RequestLatestComment(moment.moment_id,new MomentViewModel.FinishListener() {
            @Override
            public void onSuccess() {
                adapter.notifyItemRangeChanged(0,mViewModel.getCommentList().size());
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
        MomentListResp.Moment.updated_moment = mViewModel.getMoment().getValue();
    }

    /**
     * 初始化变量
     */
    private void InitVar(){
        //初始化ViewModel
        mViewModel = new ViewModelProvider(this).get(MomentViewModel.class);
        String token = SharedPreferencesUtils.getToken(getApplicationContext());
        int id = SharedPreferencesUtils.getId(getApplicationContext());
        mViewModel.setToken(token);
        mViewModel.setId(id);
        mViewModel.getMoment().observe(this, new Observer<MomentListResp.Moment>() {
            @Override
            public void onChanged(MomentListResp.Moment moment) {  //刷新动态显示UI
                setMomentUI(moment);
            }
        });

        //初始化adapter
        adapter = new CommentAdapter(mViewModel.getCommentList());
        //设置item_comment点击监听
        adapter.setItemClickListener(new CommentAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {  //TODO: Resume comment

            }

            @Override
            public void onAvatarClick(int position) {  //TODO
                CommentListResp.Comment comment = mViewModel.getCommentList().get(position);
                Intent starter = new Intent(getApplicationContext(), OthersPageActivity.class);
                starter.putExtra("u_id", comment.user_id);
                startActivity(starter);
            }
        });
    }


    /**
     * 初始化UI控件
     */
    private void InitUI(){
        //头像
        binding.momentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //TODO: goto personal page
                MomentListResp.Moment moment = mViewModel.getMoment().getValue();
                Intent starter = new Intent(getApplicationContext(), OthersPageActivity.class);
                starter.putExtra("u_id", moment.sender_id);
                startActivity(starter);
            }
        });

        //关注
        binding.momentFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MomentListResp.Moment moment = mViewModel.getMoment().getValue();
                if(!moment.is_follower){  //关注
                    mViewModel.Follow(new MomentViewModel.FinishListener() {
                        @Override
                        public void onSuccess() {
                            binding.momentFollow.setBackgroundResource(R.drawable.btn_white);
                            binding.momentFollow.setTextColor(Color.rgb(04,174,240));
                            binding.momentFollow.setText("私聊");
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{  //私聊
                    //TODO: goto private chat
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_CONVERSATION_ID, moment.sender_id.toString());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    startActivity(intent);
                }
            }
        });

        //点赞
        binding.momentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.Like(!mViewModel.getMoment().getValue().is_liked, new MomentViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        MomentListResp.Moment moment = mViewModel.getMoment().getValue();
                        binding.momentLike.setText(moment.like_num.toString());
                        Drawable drawable;
                        if (moment.is_liked)
                            drawable = getDrawable(R.drawable.ic_like_solid);
                        else drawable = getDrawable(R.drawable.ic_like);
                        drawable.setBounds(0, 0, 60, 60);
                        binding.momentLike.setCompoundDrawables(drawable, null, null, null);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //评论
        binding.momentComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                binding.commentInput.requestFocus();//setFocus方法无效 //addAddressRemarkInfo is EditText
            }
        });

        //评论添加图片
        binding.commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //限制至多选择一张图片
                if(mViewModel.getSelectedImg().size()==0) SelectImage();
                else Toast.makeText(getApplicationContext(),"只能选一张图片哦！",Toast.LENGTH_LONG).show();
            }
        });

        //发送按钮
        binding.commentSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {  //TODO: Send Comment
                String content = binding.commentInput.getText().toString();
                if(content.length()==0) return;
                String path=null;
                if(mViewModel.getSelectedImg().size()>0){  //注意path, uri, file区别与转换
                    path = mViewModel.getSelectedImg().get(0);
                    Uri src_uri = Uri.fromFile(new File(path));
                    File cache_dir = getFilesDir();
                    Uri dest_uri = Uri.parse(SiliCompressor.with(getApplicationContext()).compress(src_uri.toString(),cache_dir));
                    path = FileUriConvertUtils.uriToFileApiQ(dest_uri,getApplicationContext()).getPath();
                }
                mViewModel.SendComment(content, path, new MomentViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"评论成功！",Toast.LENGTH_LONG).show();
                        //评论栏清空
                        binding.commentInput.setText("");
                        InputMethodManager imm = (InputMethodManager) binding.commentInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binding.commentInput.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        binding.commentTableRow.removeAllViews();
                        //评论数增加
                        binding.momentComment.setText(mViewModel.getMoment().getValue().comment_num.toString());
                        //重新请求评论
                        int num = mViewModel.getCommentList().size();
                        mViewModel.getCommentList().clear();
                        adapter.notifyItemRangeRemoved(0,num);
                        mViewModel.RequestLatestComment(mViewModel.getMoment().getValue().moment_id, new MomentViewModel.FinishListener() {
                            @Override
                            public void onSuccess() {
                                adapter.notifyItemRangeChanged(0,mViewModel.getCommentList().size());
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //初始化SmartRefreshLayout
        RefreshLayout refreshLayout = binding.commentRefresh;
        refreshLayout.setEnableRefresh(false);  //禁止下拉刷新
        refreshLayout.setOnMultiListener(new SimpleMultiListener(){
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout){  //刷新获取最新动态
                super.onRefresh(refreshLayout);
                refreshLayout.finishRefresh(2000);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout){  //下拉加载更多
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadMore(2000);
                //TODO: get request
                int num = mViewModel.getCommentList().size();
                mViewModel.RequestMoreComment(mViewModel.getMoment().getValue().moment_id,new MomentViewModel.FinishListener() {
                    @Override
                    public void onSuccess() {
                        adapter.notifyItemRangeChanged(num,mViewModel.getCommentList().size()-num);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //初始化RecycleView
        RecyclerView recyclerView = binding.commentRecycle;
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }


    /**
     * 设置动态显示的UI
     */
    private void setMomentUI(MomentListResp.Moment moment){
        Glide.with(this).load(moment.avatar).centerCrop().into(binding.momentAvatar);
        binding.momentName.setText(moment.nickname);
        binding.momentDate.setText(moment.created_time);
        binding.momentContent.setText(moment.text_content);
        if(!moment.image.equals("")){
            Glide.with(this).load(moment.image).override(600,800).into(binding.momentImage);
            binding.momentImage.setVisibility(View.VISIBLE);
        }
        else {
            binding.momentImage.setVisibility(View.GONE);
        }
        if(mViewModel.getId()!=mViewModel.getMoment().getValue().sender_id){  //用户自己不显示
            if(moment.is_follower) {
                binding.momentFollow.setBackgroundResource(R.drawable.btn_white);
                binding.momentFollow.setTextColor(Color.rgb(04,174,240));
                binding.momentFollow.setText("私聊");
            }
            else{
                binding.momentFollow.setBackgroundResource(R.drawable.btn_blue);
                binding.momentFollow.setTextColor(Color.rgb(255,255,255));
                binding.momentFollow.setText("关注");
            }
        }
        else binding.momentFollow.setVisibility(View.GONE);
        binding.momentLike.setText(String.valueOf(moment.like_num));
        Drawable drawable;
        if(moment.is_liked) drawable = getDrawable(R.drawable.ic_like_solid);
        else drawable = getDrawable(R.drawable.ic_like);
        drawable.setBounds(0,0,60,60);
        binding.momentLike.setCompoundDrawables(drawable,null,null,null);
        binding.momentComment.setText(String.valueOf(moment.comment_num));
    }


    /**
     * 选择一张照片
     */
    private void SelectImage(){
        //自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);

            }
        });

        //自由配置选项
        ISListConfig config = new ISListConfig.Builder().
                multiSelect(false).//不多选
                statusBarColor(Color.parseColor("#04AEF0")).  //沉浸式状态栏
                title("选择一张图片表达自己的看法吧！").  //设置标题
                titleBgColor(Color.parseColor("#04AEF0")).  //标题背景颜色
                maxNum(1).  //最多选择一张
                needCamera(false).
                build();

        //跳转到图片选择器
        ISNav.getInstance().toListActivity(this,config,PhotoReqCode);
    }


    /**
     * 将选择的照片显示在界面上
     * @param path
     */
    private void HandleSelectedImage(String path){
        int degree = PhotoUtils.getBitmapDegree(path);  //获取旋转角度
        Bitmap bitmap1 = PhotoUtils.decodeSampledBitmapFromFd(path, 150, 150);  //压缩图片
        Bitmap bitmap2 = PhotoUtils.rotateBitmapByDegree(bitmap1, degree);  //根据旋转角度旋转图片
        //在界面上添加新的View
        ImageContainerView imageContainer = new ImageContainerView(this);
        imageContainer.setImageBitmap(bitmap2);
        imageContainer.setOnCancelClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //取消上传图片
                mViewModel.getSelectedImg().remove(path);
                binding.commentTableRow.removeView(imageContainer);
            }
        });
        binding.commentTableRow.addView(imageContainer,mViewModel.getSelectedImg().size());
        mViewModel.getSelectedImg().add(path);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == PhotoReqCode && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                //TODO: handle selected images
                HandleSelectedImage(path);
            }
        }
    }

}