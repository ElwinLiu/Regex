package com.hyphenate.easeim.section.me.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easeim.DemoHelper;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.common.constant.DemoConstant;
import com.hyphenate.easeim.common.livedatas.LiveDataBus;
import com.hyphenate.easeim.common.utils.BitmapUtils;
import com.hyphenate.easeim.common.utils.CameraUtils;
import com.hyphenate.easeim.common.utils.PreferenceManager;
import com.hyphenate.easeim.common.utils.SPUtils;
import com.hyphenate.easeim.common.widget.ArrowItemView;
import com.hyphenate.easeim.section.base.BaseInitActivity;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.EMLog;
import com.hyphenate.chat.EMUserInfo.*;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserDetailActivity extends BaseInitActivity {
    static private String TAG = "UserDetailActivity";
    private EaseTitleBar titleBar;
    private ArrowItemView itemNickname;
    private EaseImageView headImageView;
    private String headImageUrl = null;
    private String nickName;

    //权限请求
    private RxPermissions rxPermissions;

    //是否拥有权限
    private boolean hasPermissions = false;

    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;

    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 3;
    //启动相册标识
    public static final int SELECT_PHOTO = 4;


    //Base64
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        checkVersion();
    }

    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 检查版本
     */
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions(this);
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
//                            showMsg("已获取权限");
                            hasPermissions = true;
                        } else {//申请失败
//                            showMsg("权限未开启");
                        }
                    });
        } else {
            //Android6.0以下
            showMsg("无需请求动态权限");
        }
    }


    /**
     * 拍照
     */
    private void takePhoto() {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }


    public static void actionStart(Context context, String nickName, String url) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("imageUrl", url);
        intent.putExtra("nickName", nickName);
        context.startActivity(intent);
    }

    /**
     * init intent
     *
     * @param intent
     */
    @Override
    protected void initIntent(Intent intent) {
        if (intent != null) {
            headImageUrl = intent.getStringExtra("imageUrl");
            nickName = intent.getStringExtra("nickName");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_user_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        itemNickname = findViewById(R.id.item_nickname);
        headImageView = findViewById(R.id.tv_headImage_view);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(new EaseTitleBar.OnBackPressListener() {
            @Override
            public void onBackPress(View view) {
                onBackPressed();
            }
        });
        itemNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OfflinePushNickActivity.class);
                intent.putExtra("nickName", nickName);
                startActivityForResult(intent, 2);
            }
        });
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ChooseHeadImageActivity.class);
//                intent.putExtra("headUrl",headImageUrl);
//                startActivityForResult(intent, 1);

                // 底部弹出弹框，选择头像
                bottomSheetDialog = new BottomSheetDialog(UserDetailActivity.this);
                bottomView = getLayoutInflater().inflate(R.layout.myf_dialog_bottom, null);
                bottomSheetDialog.setContentView(bottomView);

                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
                TextView tvTakePictures = bottomView.findViewById(R.id.tv_take_pictures);
                TextView tvOpenAlbum = bottomView.findViewById(R.id.tv_open_album);
                TextView tvCancel = bottomView.findViewById(R.id.tv_cancel);

                //拍照
                tvTakePictures.setOnClickListener(myf_v -> {
                    if (!hasPermissions) {
//                        showMsg("未获取到权限");
                        checkVersion();
//                        return;
                    }
                    takePhoto();
                    Toast.makeText(UserDetailActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.cancel();
                });
                //打开相册
                tvOpenAlbum.setOnClickListener(myf_v -> {
                    if (!hasPermissions) {
//                        showMsg("未获取到权限");
                        checkVersion();
//                        return;
                    }
                    openAlbum();
                    Toast.makeText(UserDetailActivity.this, "打开相册", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.cancel();
                });
                //取消
                tvCancel.setOnClickListener(myf_v -> {
                    bottomSheetDialog.cancel();
                });
                bottomSheetDialog.show();


            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (headImageUrl != null && headImageUrl.length() > 0) {
            Glide.with(mContext).load(headImageUrl).placeholder(R.drawable.em_login_logo).into(headImageView);
        }
        if (headImageUrl == null || nickName == null) {
            intSelfDate();
        }

        //增加数据变化监听
        addLiveDataObserver();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 && resultCode == RESULT_OK)) {
            if (data != null) {
                headImageUrl = data.getStringExtra("headImage");
                Glide.with(mContext).load(headImageUrl).placeholder(R.drawable.em_login_logo).into(headImageView);
            }
        } else if ((requestCode == 2 && resultCode == RESULT_OK)) {
            if (data != null) {
                nickName = data.getStringExtra("nickName");
            }
        } else if ((requestCode == TAKE_PHOTO && resultCode == RESULT_OK)) {
            // 拍照完回来了
            // 显示图片
            displayImage(outputImagePath.getAbsolutePath());
        } else if ((requestCode == SELECT_PHOTO && resultCode == RESULT_OK)) {
            // 选择完照片回来了
            String imagePath = null;
            //判断手机系统版本号
            //4.4及以上系统使用这个方法处理图片
            assert data != null;
            imagePath = CameraUtils.getImageOnKitKatPath(data, this);
            //显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
//            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead);

            //压缩图片
//            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));

            //放入缓存
            SPUtils.putString("imageUrl",imagePath,this);
            // 请求 图下给你的 url
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(60000, TimeUnit.MILLISECONDS)
                    .readTimeout(60000, TimeUnit.MILLISECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", imagePath,
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(imagePath)))
                    .build();

            // 获取 token
            SharedPreferences sharedPreferences= getSharedPreferences("appUserInfo", MODE_PRIVATE);
            String token = sharedPreferences.getString("appUserToken","");

            Request request = new Request.Builder()
                    .url("http://106././.79:40010/user/avatarUpdate")
                    .method("POST", body)
                    .addHeader("Authorization", token)
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = client.newCall(request).execute();
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        // 如果请求成功，显示图像
                        if(jsonObject.getBoolean("success")){
//                            headImageUrl = jsonObject.getString("content");
//                            Glide.with(mContext).load(headImageUrl).placeholder(R.drawable.em_login_logo).into(headImageView);
//                            intSelfDate1();
                            String selectHeadUrl = jsonObject.getString("content");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    EMClient.getInstance().userInfoManager().updateOwnInfoByAttribute(EMUserInfoType.AVATAR_URL, selectHeadUrl, new EMValueCallBack<String>() {
                                        @Override
                                        public void onSuccess(String value) {
                                            EMLog.d(TAG, "updateOwnInfoByAttribute :" + value);
                                            showToast(R.string.demo_head_image_update_success);
                                            PreferenceManager.getInstance().setCurrentUserAvatar(selectHeadUrl);
                                            DemoHelper.getInstance().getUserProfileManager().updateUserAvatar(selectHeadUrl);
                                            EaseEvent event = EaseEvent.create(DemoConstant.AVATAR_CHANGE, EaseEvent.TYPE.CONTACT);
                                            //发送联系人更新事件
                                            event.message = selectHeadUrl;
                                            LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE).postValue(event);
                                            getIntent().putExtra("headImage", selectHeadUrl);
                                            setResult(RESULT_OK, getIntent());
                                            finish();
                                        }

                                        @Override
                                        public void onError(int error, String errorMsg) {
                                            EMLog.d(TAG, "updateOwnInfoByAttribute  error:" + error + " errorMsg:" + errorMsg);
                                            showToast(R.string.demo_head_image_update_failed);
                                        }
                                    });
                                }
                            });
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


            //转Base64
//            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            Toast.makeText(UserDetailActivity.this, "图片获取失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void intSelfDate() {
        String[] userId = new String[1];
        userId[0] = EMClient.getInstance().getCurrentUser();
        EMUserInfoType[] userInfoTypes = new EMUserInfoType[2];
        userInfoTypes[0] = EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfoType.AVATAR_URL;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMUserInfo userInfo = userInfos.get(EMClient.getInstance().getCurrentUser());

                        //昵称
                        if (userInfo != null && userInfo.getNickName() != null &&
                                userInfo.getNickName().length() > 0) {
                            nickName = userInfo.getNickName();
                            PreferenceManager.getInstance().setCurrentUserNick(nickName);
                        }
                        //头像
                        if (userInfo != null && userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() > 0) {
                            headImageUrl = userInfo.getAvatarUrl();
                            Glide.with(mContext).load(headImageUrl).placeholder(R.drawable.em_login_logo).into(headImageView);
                            PreferenceManager.getInstance().setCurrentUserAvatar(headImageUrl);
                        }
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG, "fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });

    }

    private void intSelfDate1() {
        String[] userId = new String[1];
        userId[0] = EMClient.getInstance().getCurrentUser();
        EMUserInfoType[] userInfoTypes = new EMUserInfoType[2];
        userInfoTypes[0] = EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfoType.AVATAR_URL;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes, new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMUserInfo userInfo = userInfos.get(EMClient.getInstance().getCurrentUser());

                        //昵称
                        if (userInfo != null && userInfo.getNickName() != null &&
                                userInfo.getNickName().length() > 0) {
                            // nickName = userInfo.getNickName();
                            userInfo.setNickname(nickName);
                            PreferenceManager.getInstance().setCurrentUserNick(nickName);
                        }
                        //头像
                        if (userInfo != null && userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() > 0) {
                            // headImageUrl = userInfo.getAvatarUrl();
                            userInfo.setAvatarUrl(headImageUrl);
                            Glide.with(mContext).load(headImageUrl).placeholder(R.drawable.em_login_logo).into(headImageView);
                            PreferenceManager.getInstance().setCurrentUserAvatar(headImageUrl);
                        }
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG, "fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });

    }

    protected void addLiveDataObserver() {
        LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                Glide.with(mContext).load(event.message).placeholder(R.drawable.em_login_logo).into(headImageView);
            }
        });
        LiveDataBus.get().with(DemoConstant.NICK_NAME_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                nickName = event.message;
            }
        });
    }
}
