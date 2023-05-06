package com.hyphenate.easeim.section.moment.activity.post;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.moment.activity.moment.MomentViewModel;
import com.hyphenate.easeim.databinding.ActivityPostBinding;
import com.hyphenate.easeim.section.moment.utils.FileUriConvertUtils;
import com.hyphenate.easeim.section.moment.utils.PhotoUtils;
import com.hyphenate.easeim.section.moment.utils.SharedPreferencesUtils;
import com.hyphenate.easeim.section.moment.view.ImageContainerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.iceteck.silicompressorr.SiliCompressor;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostActivity extends AppCompatActivity {
    private ActivityPostBinding binding;
    private PostViewModel mViewModel;
    private final int PhotoReqCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        String token = SharedPreferencesUtils.getToken(getApplicationContext());
        mViewModel.setToken(token);
        InitUI();  //初始化UI控件
    }


    /**
     * 初始化UI控件
     */
    private void InitUI(){
        //返回按钮
        ImageButton back_btn = binding.postBackBtn;
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //返回广场
                finish();
            }
        });

        //确认发布按钮
        Button confirm_btn = binding.confirmBtn;
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //发布动态
                PostMoment();
            }
        });

        //底部菜单栏
        BottomNavigationView nav_view = binding.bottomNavigationView;
        nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.photo:
                        if(mViewModel.getSelected_img().size()==0) SelectImage();  //只允许上传一张照片
                        else Toast.makeText(PostActivity.this,"只能选一张照片哦！",Toast.LENGTH_LONG).show();
                        break;
                    default:
                }
                return true;
            }
        });
    }


    /**
     * 发布动态
     */
    private void PostMoment() {
        String content = binding.postContent.getText().toString();
        String path=null;
        if(mViewModel.getSelected_img().size()>0){  //注意path, uri, file区别与转换
            path = mViewModel.getSelected_img().get(0);
            Uri src_uri = Uri.fromFile(new File(path));
            File cache_dir = getFilesDir();
            Uri dest_uri = Uri.parse(SiliCompressor.with(getApplicationContext()).compress(src_uri.toString(),cache_dir));
            path = FileUriConvertUtils.uriToFileApiQ(dest_uri,getApplicationContext()).getPath();
        }
        if(content.length()==0){
            Toast.makeText(getApplicationContext(),"分享内容不能为空哦！",Toast.LENGTH_LONG).show();
            return;
        }
        mViewModel.PostMoment(content,path,new PostViewModel.FinishListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"分享动态成功！",Toast.LENGTH_LONG).show();
                //收起软键盘
                InputMethodManager imm = (InputMethodManager) binding.postContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.postContent.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * 从相册选择图片
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
        Bitmap bitmap1 = PhotoUtils.decodeSampledBitmapFromFd(path, 300, 300);  //压缩图片
        Bitmap bitmap2 = PhotoUtils.rotateBitmapByDegree(bitmap1, degree);  //根据旋转角度旋转图片
        //在界面上添加新的View
        ImageContainerView imageContainer = new ImageContainerView(this);
        imageContainer.setImageBitmap(bitmap2);
        imageContainer.setOnCancelClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //取消上传图片
                mViewModel.getSelected_img().remove(path);
                binding.tableRow.removeView(imageContainer);
            }
        });
        binding.tableRow.addView(imageContainer,mViewModel.getSelected_img().size());
        mViewModel.getSelected_img().add(path);
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