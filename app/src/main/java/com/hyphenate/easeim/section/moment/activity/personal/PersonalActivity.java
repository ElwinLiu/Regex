package com.hyphenate.easeim.section.moment.activity.personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeim.R;

public class PersonalActivity extends AppCompatActivity {


    public static void actionStart(Context context) {
        Intent starter = new Intent(context, PersonalActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
    }
}