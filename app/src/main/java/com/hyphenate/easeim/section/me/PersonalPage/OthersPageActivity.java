package com.hyphenate.easeim.section.me.PersonalPage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.hyphenate.easeim.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OthersPageActivity extends AppCompatActivity {

    int u_id;

    ImageView ivBackGround;
    AvatarLayout avatarLayout;
    TextView tv_nickname;
    TextView tv_follow;
    TextView tv_isfollow;
    TextView tc_createtime;

    FlexboxLayout flexTags;


    /**
     * 启动他人主页
     *
     * @param context 上下文
     * @param id      app数据库 用户 id u_id
     */
    public static void actionStart(Context context, int id) {
        Intent starter = new Intent(context, OthersPageActivity.class);
        starter.putExtra("u_id", id);
        context.startActivity(starter);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_page);
        u_id = getIntent().getIntExtra("u_id", 0);

        ivBackGround = findViewById(R.id.iv_bg);
        avatarLayout = findViewById(R.id.avatar_personal_page);
        tv_nickname = findViewById(R.id.nickname_personal_page);
        tv_follow = findViewById(R.id.personal_page_follow);
        tv_isfollow = findViewById(R.id.personal_page_isfllow);
        tc_createtime = findViewById(R.id.personal_page_createtime);
        // 存储标签
        flexTags = findViewById(R.id.flexbox);


        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Bitmap[] bitmap = {BlurBitmapUtil.blurBitmap(this, BitmapFactory.decodeResource(getResources(), R.drawable.avatar), 3f)};

        Context mContext = this;
//        bitmap = BlurBitmapUtil.blurBitmap(mContext, BitmapFactory.decodeResource(getResources(), R.drawable.avatar), 3f);
        // okhttp 请求头像的 url
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        // 获取 token
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("appUserInfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("appUserToken", "");
        // 获取 appUserId
//        int appUserId = sharedPreferences.getInt("appUserId", 0);
        Request request = new Request.Builder()
                .url("http://106.14.78.79:40010/user/detail?id=" + Integer.toString(u_id))
                .get()
                .addHeader("Authorization", token)
                .build();

        new Thread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean("success")) {
                        JSONObject content = jsonObject.getJSONObject("content");
                        String avatarUrl = content.getString("avatar");
                        Bitmap bitmap;
                        bitmap = getBitmap(avatarUrl);
//                        bitmap = getBitmap("https://scpic.chinaz.net/files/pic/pic9/202009/apic27858.jpg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                avatarLayout.setAvatarImage(bitmap);
//                                avatarLayout.onFinishInflate();
                                ivBackGround.setImageBitmap(bitmap);
                                String tmp = null;
                                try {
                                    tmp = content.getString("nickname");
                                    tv_nickname.setText(content.getString("nickname"));
                                    tmp = Integer.toString(content.getInt("follower_num"));
                                    tv_follow.setText(Integer.toString(content.getInt("follower_num")));
                                    tmp = Integer.toString(content.getInt("subscribed_num"));
                                    tv_isfollow.setText(Integer.toString(content.getInt("subscribed_num")));
                                    tmp = content.getString("created_time");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                assert tmp != null;
                                String[] strings = tmp.split("T");
                                tc_createtime.setText(strings[0]);
                            }
                        });

                        JSONArray jsonArray = content.getJSONArray("tags");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            TextView textView = new TextView(mContext);
                            textView.setBackgroundResource(R.drawable.text_circle_gray);//给标签画一个背景
                            textView.setText(jsonObject1.getString("tag_content"));
                            textView.setTextColor(0xffa8c4d7);//设置字体颜色
                            textView.setGravity(Gravity.CENTER);//字体居中
                            textView.setPadding(10, 5, 10, 5);//设置padding
                            textView.setTextSize(13);//设置字体大小
                            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
//            params.setFlexBasisPercent(0.2f);//相对父类所占的比例 ,可以控制一行有几个标签
                            params.setMargins(20, 10, 0, 0);//根据需求自己设置
                            textView.setLayoutParams(params);
                            flexTags.addView(textView);
                        }

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }
}