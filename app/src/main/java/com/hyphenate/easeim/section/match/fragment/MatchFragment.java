package com.hyphenate.easeim.section.match.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.easeim.R;
import com.hyphenate.easeim.section.base.BaseInitFragment;
import com.hyphenate.easeim.section.match.activity.LoadForMatchActivity;


public class MatchFragment extends BaseInitFragment implements View.OnClickListener{


    //
    private Button btVoiceMarch;
    private Button btAnonymous;
    private Button soulMatch;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_match;
    }

    @Override
    protected void initView(Bundle savedInstanceState){
        super.initView(savedInstanceState);
        btVoiceMarch=findViewById(R.id.bt_voiceCall);
        //btAnonymous=findViewById(R.id.bt_anonymous);
        soulMatch=findViewById(R.id.bt_soul);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btVoiceMarch.setOnClickListener(this);
        //btAnonymous.setOnClickListener(this);
        soulMatch.setOnClickListener(this);


        /*匿名匹配暂时不要了*/
        //btAnonymous.setVisibility(View.INVISIBLE);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {


        switch (view.getId())
        {
            case R.id.bt_voiceCall:
            {
                Intent intent = new Intent(mContext, LoadForMatchActivity.class);
                intent.putExtra("type", "Voice");
                mContext.startActivity(intent);
                break;
            }
           /* //匿名匹配
            case R.id.bt_anonymous:
            {
                Intent intent = new Intent(mContext, LoadForMatchActivity.class);
                intent.putExtra("type", "Anonymous");
                mContext.startActivity(intent);
                break;
            }*/

            case R.id.bt_soul:
            {
                Intent intent = new Intent(mContext, LoadForMatchActivity.class);
                intent.putExtra("type", "Soul");
                mContext.startActivity(intent);
                break;
            }

        }
    }


}