package com.hyphenate.easeim.section.me.PersonalPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

//import com.hyphenate.easeim.databinding.ActivityPersonalPageBinding;
//import com.hyphenate.easeim.section.me.activity.databinding.ActivityPersonalPageBinding;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.follow.activity.FollowerActivity;
import com.hyphenate.easeim.section.follow.activity.SubscribedActivity;
import com.hyphenate.easeim.section.moment.activity.personal.PersonalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonalPageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBackGround;
    AvatarLayout avatarLayout;
    TextView tv_nickname;
    TextView tv_follow;
    TextView tv_isfollow;
    TextView tc_createtime;


    public static void actionStart(Context context) {
        Intent starter = new Intent(context, PersonalPageActivity.class);
        context.startActivity(starter);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        ivBackGround = (ImageView) findViewById(R.id.iv_bg);
        avatarLayout = findViewById(R.id.avatar_personal_page);
        tv_nickname = findViewById(R.id.nickname_personal_page);
        tv_follow = findViewById(R.id.personal_page_follow);
        tv_isfollow = findViewById(R.id.personal_page_isfllow);
        tc_createtime = findViewById(R.id.personal_page_createtime);
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
        int appUserId = sharedPreferences.getInt("appUserId", 0);
        Request request = new Request.Builder()
                .url("http://106././.79:40010/user/detail?id=" + Integer.toString(appUserId))
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

                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        tv_follow.setOnClickListener((View.OnClickListener) this);
//
//        tv_follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(PersonalPageActivity.this,"关注列表",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        tv_isfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(PersonalPageActivity.this,"被关注列表",
//                        Toast.LENGTH_LONG).show();
//            }
//        });

        tv_follow.setOnClickListener(this);

        tv_isfollow.setOnClickListener(this);

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_page_follow:
                Toast.makeText(PersonalPageActivity.this, "关注列表",
                        Toast.LENGTH_SHORT).show();
                FollowerActivity.actionStart(PersonalPageActivity.this);
                break;
            case R.id.personal_page_isfllow:
                Toast.makeText(PersonalPageActivity.this, "被关注列表",
                        Toast.LENGTH_SHORT).show();
                SubscribedActivity.actionStart(PersonalPageActivity.this);
                break;
        }
    }
}