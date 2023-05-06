package com.hyphenate.easeim.section.follow.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.hyphenate.easeim.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SubscribedActivity extends AppCompatActivity {

    public static void actionStart(Context context){
        Intent intent =new Intent(context, SubscribedActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed);
    }
}