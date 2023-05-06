package com.hyphenate.easeim.section.match.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.base.BaseActivity;
import com.hyphenate.easeim.section.base.BaseInitActivity;
import com.hyphenate.easeim.section.chat.activity.ChatActivity;
import com.hyphenate.easeim.section.contact.viewmodels.AddContactViewModel;
import com.hyphenate.easeim.section.match.bean.tag;
import com.hyphenate.easeui.constants.EaseConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadForMatchActivity extends BaseActivity {


    private String type;

    private String appUserToken;
    private Integer id;
    private String avatar_url;
    private String nickname;



    //获取系统当前时间字符串
    private String getChannel_name(int uid){
        long timeStamep=System.currentTimeMillis();
        return "channel_"+uid+"_"+timeStamep;
    }



    //启动后台线程进行匹配


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_for_match);


        //获取所保存的信息
        SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
        appUserToken = sharedPreferences.getString("appUserToken", "");
        id=sharedPreferences.getInt("appUserId",-1);



        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        ImageView mImageView = findViewById(R.id.load_image);
        AnimationDrawable mLoadingAnimationDrawable = (AnimationDrawable) mImageView.getDrawable();
        //直接就开始执行，性能不是最佳的。
        mLoadingAnimationDrawable.start();

        //存储一下头像的链接

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://106.14.78.79:40010/user/detail?id="+id;
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
                        nickname=jsonObject.optString("nickname", null);
                        avatar_url=jsonObject.optString("avatar", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {

                if (type.equals("Soul")) {

                    String url = "http://106.14.78.79:40010/match/normal";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)//上传接口
                            .get()
                            .addHeader("Authorization", appUserToken)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //获取用户的详细信息 将其更新到主线程中
                    try {
                        JSONObject jsonObjectALL = new JSONObject(response.body().string());
                        String userDetail = jsonObjectALL.optString("content", null);
                        Integer id_o=-1;
                        String nickname_m="";
                        if (!TextUtils.isEmpty(userDetail)){{
                            JSONObject jsonObject = new JSONObject(userDetail);
                            id_o=jsonObject.optInt("id", -1);
                            nickname_m=jsonObject.optString("nickname","");
                        }}

                        //匹配成功

                        openChatView(id_o,nickname_m);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                else if (type.equals("Voice")) {
                    //语音匹配开始通话
                    String url = "http://106.14.78.79:40010/match/audio";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    JSONObject jsonObjectS = new JSONObject();
                    try {
                        jsonObjectS.put("channel_name",getChannel_name(id));
                        jsonObjectS.put("uid",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObjectS.toString());
                    Request request = new Request.Builder()
                            .url(url)//上传接口
                            .post(body)
                            .addHeader("Authorization", appUserToken)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    Response response = null;
                    try {
                        response = call.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //获取用户的详细信息 将其更新到主线程中
                    try {
                        JSONObject jsonObjectALL = new JSONObject(response.body().string());
                        String userDetail = jsonObjectALL.optString("content", null);
                        //看看是不是
                        //获取call_id;
                        Integer call_id=-1;
                        Integer id_o=-1;
                        Boolean is_first=true;
                        String channelName="";
                        String token="";
                        if (!TextUtils.isEmpty(userDetail)){{
                            JSONObject jsonObject = new JSONObject(userDetail);
                            call_id=jsonObject.optInt("call_id", -1);
                            id_o=jsonObject.optInt("user_id", -1);
                            is_first=jsonObject.optBoolean("is_first",true);
                            channelName=jsonObject.optString("channelName");
                            token=jsonObject.optString("token");

                            Log.i("token",token);
                            Log.i("token",channelName);
                        }}

                        //匹配成功
                        openVoiceCallView(call_id,id_o,is_first,channelName,token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }
        }).start();
    }


    //打开新的聊天窗口
    private void openChatView(int id,String nickname_m) {
        //将这个窗口关闭
        runOnUiThread(new Runnable() {

            @Override
            public void run() {


                while (true){
                    if (nickname!=null||avatar_url!=null){
                        break;
                    }
                }



                Toast.makeText(LoadForMatchActivity.this, "已经发送好友申请!", Toast.LENGTH_SHORT).show();
                //添加好友!
                AddContactViewModel addContactViewModel=new ViewModelProvider(mContext).get(AddContactViewModel.class);
                addContactViewModel.addContact(String.valueOf(id),"Add me as a friend");

                //向对方发送加好友的请求


                Toast.makeText(LoadForMatchActivity.this, "匹配成功!", Toast.LENGTH_SHORT).show();

                //直接打开一个新的聊天窗口
                //ChatActivity.actionStart(getBaseContext(), id, EaseConstant.CHATTYPE_SINGLE);
                Intent intent=new Intent(LoadForMatchActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CONVERSATION_ID, String.valueOf(id));
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                startActivity(intent);
                finish();




            }
        });

    }


    //打开语音通话界面
    private void openVoiceCallView(int call_id,int id_o,boolean is_first,String channelName,String token){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {


                while (true){
                    if (nickname!=null||avatar_url!=null){
                        break;
                    }
                }

                if(is_first==false){
                    Toast.makeText(LoadForMatchActivity.this, "匹配成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoadForMatchActivity.this,VoiceMatchActivity.class);
                    intent.putExtra("id_m",id);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("avatar_url",avatar_url);
                    intent.putExtra("channelName",channelName);
                    intent.putExtra("token",token);
                    intent.putExtra("is_first",false);
                    intent.putExtra("id_o",id_o);
                    intent.putExtra("call_id",call_id);
                    startActivity(intent);
                }
                //主界面进行等待
                else{
                    Toast.makeText(LoadForMatchActivity.this, "正在为您匹配", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoadForMatchActivity.this,VoiceMatchActivity.class);
                    intent.putExtra("id_m",id);
                    intent.putExtra("nickname",nickname);
                    intent.putExtra("avatar_url",avatar_url);
                    intent.putExtra("channelName",channelName);
                    intent.putExtra("token",token);
                    intent.putExtra("is_first",true);
                    intent.putExtra("id_o",-1);
                    intent.putExtra("call_id",call_id);
                    startActivity(intent);
                }
            }
        });
        this.finish();
    }


}