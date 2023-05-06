package com.hyphenate.easeim.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeim.BuildConfig;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.common.widget.ArrowItemView;
import com.hyphenate.easeim.section.base.BaseInitActivity;
import com.hyphenate.easeui.widget.EaseTitleBar;

public class AboutHxActivity extends BaseInitActivity implements View.OnClickListener, EaseTitleBar.OnBackPressListener {
    private EaseTitleBar title_bar;
    private TextView tv_version;
    private ArrowItemView item_product;
    private ArrowItemView item_company;
    private ArrowItemView item_Elwin;
    private ArrowItemView item_Steven705;
    private ArrowItemView item_Cherries;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, AboutHxActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_about_hx;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        title_bar = findViewById(R.id.title_bar);
        tv_version = findViewById(R.id.tv_version);
        item_product = findViewById(R.id.item_product);
        item_company = findViewById(R.id.item_company);
        item_Elwin = findViewById(R.id.item_Elwin);
        item_Steven705 = findViewById(R.id.item_Steven705);
        item_Cherries = findViewById(R.id.item_Cherries);
    }

    @Override
    protected void initData() {
        super.initData();
//        tv_version.setText(getString(R.string.em_about_hx_version, BuildConfig.VERSION_NAME));
        tv_version.setText("CUG-SE-Soul-V1.0");
    }

    @Override
    protected void initListener() {
        super.initListener();
        title_bar.setOnBackPressListener(this);
        item_product.setOnClickListener(this);
        item_company.setOnClickListener(this);
        item_Elwin.setOnClickListener(this);
        item_Steven705.setOnClickListener(this);
        item_Cherries.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_product:
                jumpToAgarthasf();
                break;
            case R.id.item_company:
                jumpToYnmtDJ();
                break;
            case R.id.item_Elwin:
                jumpToElwin();
                break;
            case R.id.item_Steven705:
                jumpToSteven705();
                break;
            case R.id.item_Cherries:
                jumpToCherries();
                break;

        }
    }

    private void jumpToAgarthasf() {
        Uri uri = Uri.parse("https://agarthasf.github.io/");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void jumpToElwin() {
        Uri uri = Uri.parse("https://elwinliu.github.io/");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void jumpToSteven705() {
        Uri uri = Uri.parse("https://github.com/steven705");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void jumpToYnmtDJ() {
        Uri uri = Uri.parse("https://github.com/YnmtDJ");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void jumpToCherries() {
        Uri uri = Uri.parse("https://blog.csdn.net/myf_666?type=blog");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }
}
