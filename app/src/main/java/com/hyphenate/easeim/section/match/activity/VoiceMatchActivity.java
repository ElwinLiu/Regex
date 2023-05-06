package com.hyphenate.easeim.section.match.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.base.BaseActivity;
import com.hyphenate.easeim.section.contact.viewmodels.AddContactViewModel;
import com.hyphenate.easeim.section.match.bean.tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Java


// Java
import io.agora.rtc.RtcEngine;
import io.agora.rtc.IRtcEngineEventHandler;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class VoiceMatchActivity extends BaseActivity implements View.OnClickListener{


    // 填写项目的 App ID，可在 Agora 控制台中生成。
    private String appId = "56247a7e72a6438fb35355d5e2e58349";
    private String channelName = "";
    private String token = "";
    private RtcEngine mRtcEngine;

    //回调函数
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        //对方加入频道之后的回调函数
        public void onUserJoined(int uid, int elapsed) {

            //重新写入id
            id_o=uid;
            //启动计时器
            countTimer();
            //
            if(is_first==true){
                setMyInf();
                //将对方的信息写入
                getUserInf();
                //看看是否需要关闭原来的计时器
                closeCountTimer01();
            }
        }
        @Override
        //对方离开了频道的回调函数
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            //直接挂断电话即可
            try {
                closeConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private static final int PERMISSION_REQ_ID = 22;
    // 待申请的权限
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };

    //判断权限是否升级申请成功
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    //功能按键
    private Button bt_loudly;
    private Button bt_reveal_identity;
    private Button bt_hang_up;
    //头像 ID 引力签
    private ImageView avatar_o;
    private ImageView avatar_m;
    private TextView tv_nickname_o;
    private TextView tv_nickname_m;
    private TextView tv_tag_title;
    private TextView tv_tag;
    //倒计时
    private TextView tv_time;

    //匹配倒计时
    private TextView tv_match_time;

    //我的的ID和对方的ID
    private Integer id_m;
    private Integer id_o;//这里只是默认 因为没有真的匹配
    private String nickname;
    private String avatar_url;
    private Bitmap bitmap_m;

    //两个人的身份是否公开 怎么知道对方是否公开了身份 对方公开身份之后还需要加为好友!!!!!
    private boolean open_st_m=false,open_st_o=false;
    //扬声器
    private AudioManager audioManager;
    private boolean isSpeakerOpen = true;//默认开启手机扬声器
    private static int currVolume = 0;//当前音量

    private String appUserToken;


    private Integer call_id;


    //是不是第一个接电话的
    private Boolean is_first;

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_match);

        bt_loudly=findViewById(R.id.bt_loud_speaker);
        bt_reveal_identity=findViewById(R.id.bt_reveal_identity);
        bt_hang_up=findViewById(R.id.bt_hang_up);
        avatar_o=(ImageView) findViewById(R.id.avatar_o);
        avatar_m=(ImageView) findViewById(R.id.avatar_m);
        tv_nickname_o=findViewById(R.id.tv_nickname_o);
        tv_nickname_m=findViewById(R.id.tv_nickname_m);
        tv_tag_title=findViewById(R.id.tv_tag_title);
        tv_tag=findViewById(R.id.tv_tag);
        tv_match_time=findViewById(R.id.tv_match_time);
        tv_time=findViewById(R.id.tv_time);
        //设置点击事件
        bt_loudly.setOnClickListener(this);
        bt_reveal_identity.setOnClickListener(this);
        bt_hang_up.setOnClickListener(this);
        audioManager = ((AudioManager) getSystemService(AUDIO_SERVICE));



        //获取信息
        Intent intent =getIntent();

        id_m=intent.getIntExtra("id_m",-1);
        nickname=intent.getStringExtra("nickname");
        avatar_url=intent.getStringExtra("avatar_url");
        channelName=intent.getStringExtra("channelName");
        token=intent.getStringExtra("token");
        is_first=intent.getBooleanExtra("is_first",true);
        id_o=intent.getIntExtra("id_o",-1);
        call_id=intent.getIntExtra("call_id",-1);


        SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
        appUserToken = sharedPreferences.getString("appUserToken", "");






        //如果是先加入频道的话 那么就
        if(is_first){
            avatar_o.setVisibility(View.INVISIBLE);
            avatar_m.setVisibility(View.INVISIBLE);
            tv_nickname_o.setVisibility(View.INVISIBLE);
            tv_nickname_m.setVisibility(View.INVISIBLE);
            tv_tag_title.setVisibility(View.INVISIBLE);
            tv_tag.setVisibility(View.INVISIBLE);
            tv_time.setVisibility(View.INVISIBLE);
            countTimer01();
        }
        else{

            tv_match_time.setVisibility(View.INVISIBLE);
            setMyInf();
            //设置对方的信息
            getUserInf();
        }



        // 如果已经授权，则初始化 RtcEngine 并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }
        else{
            //没有获取足够的权限 给出提示

        }




    }


    //匹配倒计时
    private final Handler mHandler01 = new Handler();
    //计时器操作
    private final Runnable TimerRunnable01 = new Runnable() {

        @Override
        public void run() {

            String time=tv_match_time.getText().toString();
            String[] ret01=time.split("-");
            time=ret01[1];
            if(!time.equals("00:00")){
                String[] ret=time.split(":");
                int num1 = Integer.parseInt(ret[1]);
                if(num1>10){
                    time="00:"+(num1-1);
                }else{
                    time="00:0"+(num1-1);
                }
                tv_match_time.setText(ret01[0]+"-"+time);
                countTimer01();
            }
            else{

                Toast.makeText(VoiceMatchActivity.this, "当前在线用户少，请再次尝试!", Toast.LENGTH_SHORT).show();
                //直接挂断电话
                finish();

            }

        }
    };
    private void countTimer01(){
        mHandler01.postDelayed(TimerRunnable01, 1000);
    }
    private void closeCountTimer01(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(is_first==true){
                    avatar_o.setVisibility(View.VISIBLE);
                    avatar_m.setVisibility(View.VISIBLE);
                    tv_nickname_o.setVisibility(View.VISIBLE);
                    tv_nickname_m.setVisibility(View.VISIBLE);
                    tv_tag_title.setVisibility(View.VISIBLE);
                    tv_tag.setVisibility(View.VISIBLE);
                    tv_time.setVisibility(View.VISIBLE);
                    tv_match_time.setVisibility(View.INVISIBLE);

                    is_first=false;


                    //开启通话第二个计时器
                    countTimer();
                }

            }
        });


    }
    //3分钟倒计时
    private final Handler mHandler = new Handler();
    //计时器操作
    private final Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {

            String time=tv_time.getText().toString();
            if(!time.equals("00:00")){
                String[] ret=time.split(":");
                int num0 = Integer.parseInt(ret[0]);
                int num1 = Integer.parseInt(ret[1]);
                if(ret[1].equals("00"))
                {
                    time="0"+(num0-1)+":59";
                }else{
                    if(num1>10){
                        time="0"+num0+":"+(num1-1);
                    }else{
                        time="0"+num0+":0"+(num1-1);
                    }
                }
                tv_time.setText(time);
                countTimer();
            }
            else{
                //匹配成功 开始聊天

            }

        }
    };
    //计时器函数
    private void countTimer(){
        mHandler.postDelayed(TimerRunnable, 1000);
    }


    private void setMyInf(){
        //暂时将本人的头像拿过来
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(avatar_url)//上传接口
                        .get()
                        .build();
                Call call=okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    byte[] data = response.body().bytes();
                    bitmap_m = BitmapFactory.decodeByteArray(data, 0, data.length);


                    setMyInf_02();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setMyInf_02(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_nickname_m.setText(nickname);
                avatar_m.setImageBitmap(bitmap_m);
            }
        });
    }



    //获取对方的信息
    private void getUserInf(){
        //获取对方的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://106.14.78.79:40010/user/detail?id="+id_o;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)//上传接口
                        .get()
                        .build();
                Call call=okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取用户的详细信息 将其更新到主线程中
                try {
                    JSONObject jsonObjectALL = new JSONObject(response.body().string());
                    String userDetail=jsonObjectALL.optString("content",null);
                    if (!TextUtils.isEmpty(userDetail)){
                        JSONObject jsonObject = new JSONObject(userDetail);
                        String nickname_o=jsonObject.optString("nickname", null);
                        String avatar_url=jsonObject.optString("avatar", null);
                        List<tag> tags_list = new ArrayList<>();
                        String tag_list_str=jsonObject.optString("tags", null);
                        if(!TextUtils.isEmpty(tag_list_str)){
                            JSONArray jsonArray = new JSONArray(tag_list_str);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                tag mytag=new tag(jsonObject1.optInt("id", 0),jsonObject1.optString("tag_content", null));
                                tags_list.add(mytag);
                            }
                        }
                        setContent(nickname_o,tags_list,avatar_url);
                    }else{

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void getUserAvatar(String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)//上传接口
                        .get()
                        .build();
                Call call=okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    byte[] data = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    setAvatar(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取的内容显示在上面
    private void setContent(String nickname,List<tag> tags_list,String avatar_url){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //自己
                tv_nickname_o.setText(nickname);
                //拼接字符串
                StringBuilder tag= new StringBuilder();
                for (int i=0;i<tags_list.size();i++){
                    if(i!=tags_list.size()-1){
                        tag.append(tags_list.get(i).getTag_content()).append(",");
                    }else{
                        tag.append(tags_list.get(i).getTag_content());
                    }
                }
                tv_tag.setText(tag.toString());
                //将对方的头像拿过来
                getUserAvatar(avatar_url);
            }
        });
    }

    //设置头像
    private void setAvatar(Bitmap bitmap){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avatar_o.setImageBitmap(bitmap);
            }
        });
    }



    private void closeConnection() throws InterruptedException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VoiceMatchActivity.this,"对方已经挂断电话",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }


    //通话函数
    private void initializeAndJoinChannel() {

        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error");
        }
        mRtcEngine.joinChannel(token, channelName, "", id_m);

        /*try {
            RtcEngineConfig config = new RtcEngineConfig();
            config.mContext = getBaseContext();
            config.mAppId = appId;
            config.mEventHandler = mRtcEventHandler;
            mRtcEngine = RtcEngine.create(config);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        ChannelMediaOptions options = new ChannelMediaOptions();
        // 设置频道场景为 BROADCASTING。
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
        // 将用户角色设置为 BROADCASTER。
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;



        // 使用临时 Token 加入频道。
        // 你需要自行指定用户 ID，并确保其在频道内的唯一性。
        //3个参数为token(服务器分发) channelName(通话的双方所用的必须一致 可以自己指定) uid(用用户id指定)
        mRtcEngine.joinChannel(token, channelName, id_m, options);*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //这里还是需要直接调用挂断函数
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://106.14.78.79:40010/match/audioStop?id="+call_id)
                        .addHeader("Authorization", appUserToken)
                        .get()
                        .build();
                Call call=okHttpClient.newCall(request);
                Response response = null;
                try {
                    response = call.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //本次通话结束
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }


    //按钮点击时间
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_loud_speaker:{
                isSpeakerOpen = !isSpeakerOpen;
                if (isSpeakerOpen)
                {
                    bt_loudly.setText("扬声器已打开");
                    OpenSpeaker();
                } else {
                    bt_loudly.setText("扬声器已关闭");
                    CloseSpeaker();
                }
                break;
            }
            case R.id.bt_reveal_identity:{


                if(is_first==true){
                    Toast.makeText(VoiceMatchActivity.this, "还没有建立链接!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(VoiceMatchActivity.this, "已经发送好友申请!", Toast.LENGTH_SHORT).show();

                    //加好友的操作
                    AddContactViewModel addContactViewModel=new ViewModelProvider(mContext).get(AddContactViewModel.class);
                    addContactViewModel.addContact(String.valueOf(id_o),"Add me as a friend");
                }


                break;
            }
            case R.id.bt_hang_up:{
                //关闭窗口 对方那里直接挂断
                finish();
                break;
            }
        }
    }



    //扬声器操作
    public void OpenSpeaker() {
        try {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            // 获取当前通话音量
            currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

            if (!audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);

                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void CloseSpeaker() {
        try {
            if (audioManager != null) {
                if (audioManager.isSpeakerphoneOn()) {
                    audioManager.setSpeakerphoneOn(false);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currVolume,AudioManager.STREAM_VOICE_CALL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}