package com.hyphenate.easeim.section.follow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeim.R;

public class FollowerActivity extends AppCompatActivity {

    public static void actionStart(Context context){
        Intent intent =new Intent(context, FollowerActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
    }
}