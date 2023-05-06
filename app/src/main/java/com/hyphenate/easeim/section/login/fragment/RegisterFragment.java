package com.hyphenate.easeim.section.login.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hyphenate.EMError;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.easeim.section.base.WebViewActivity;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeim.common.utils.ToastUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.easeim.section.base.BaseInitFragment;
import com.hyphenate.easeim.section.login.viewmodels.LoginViewModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterFragment extends BaseInitFragment implements TextWatcher, View.OnClickListener, EaseTitleBar.OnBackPressListener, CompoundButton.OnCheckedChangeListener {

    private EaseTitleBar mToolbarRegister;
    private EditText mEtLoginName;
    private EditText mEtLoginPwd;
    private EditText mEtLoginPwdConfirm;
    private Button mBtnLogin;
    private CheckBox cbSelect;
    private TextView tvAgreement;
    private String mUserName;
    private String mPwd;
    private String mPwdConfirm;
    private LoginViewModel mViewModel;
    private Drawable clear;
    private Drawable eyeOpen;
    private Drawable eyeClose;

    //    @BindViews({R.id.checkbox1, R.id.checkbox2, R.id.checkbox3})
    List<CheckBox> checkBoxes = new ArrayList<>(); // 多选组
    List<Integer> selectedTags = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolbarRegister = findViewById(R.id.toolbar_register);
        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mEtLoginPwdConfirm = findViewById(R.id.et_login_pwd_confirm);
        mBtnLogin = findViewById(R.id.btn_login);
        cbSelect = findViewById(R.id.cb_select);
        tvAgreement = findViewById(R.id.tv_agreement);
        // 添加标签进去checkBoxes
        checkBoxes.add(findViewById(R.id.checkbox1));
        checkBoxes.add(findViewById(R.id.checkbox2));
        checkBoxes.add(findViewById(R.id.checkbox3));
        checkBoxes.add(findViewById(R.id.checkbox4));
        checkBoxes.add(findViewById(R.id.checkbox5));
        checkBoxes.add(findViewById(R.id.checkbox6));
        checkBoxes.add(findViewById(R.id.checkbox7));
        checkBoxes.add(findViewById(R.id.checkbox8));
        checkBoxes.add(findViewById(R.id.checkbox9));
        checkBoxes.add(findViewById(R.id.checkbox10));
        checkBoxes.add(findViewById(R.id.checkbox11));
        checkBoxes.add(findViewById(R.id.checkbox12));
        checkBoxes.add(findViewById(R.id.checkbox13));
        checkBoxes.add(findViewById(R.id.checkbox14));
        checkBoxes.add(findViewById(R.id.checkbox15));
        checkBoxes.add(findViewById(R.id.checkbox16));
        checkBoxes.add(findViewById(R.id.checkbox17));
        checkBoxes.add(findViewById(R.id.checkbox18));
        checkBoxes.add(findViewById(R.id.checkbox19));
        checkBoxes.add(findViewById(R.id.checkbox20));
        checkBoxes.add(findViewById(R.id.checkbox21));
        checkBoxes.add(findViewById(R.id.checkbox22));
        checkBoxes.add(findViewById(R.id.checkbox23));
        checkBoxes.add(findViewById(R.id.checkbox24));
        checkBoxes.add(findViewById(R.id.checkbox25));
        checkBoxes.add(findViewById(R.id.checkbox26));
        checkBoxes.add(findViewById(R.id.checkbox27));
        checkBoxes.add(findViewById(R.id.checkbox28));
        checkBoxes.add(findViewById(R.id.checkbox29));
        checkBoxes.add(findViewById(R.id.checkbox30));
        checkBoxes.add(findViewById(R.id.checkbox31));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mEtLoginName.addTextChangedListener(this);
        mEtLoginPwd.addTextChangedListener(this);
        mEtLoginPwdConfirm.addTextChangedListener(this);
        mBtnLogin.setOnClickListener(this);
        mToolbarRegister.setOnBackPressListener(this);
        cbSelect.setOnCheckedChangeListener(this);
        EaseEditTextUtils.clearEditTextListener(mEtLoginName);
    }

    @Override
    protected void initData() {
        super.initData();
        //tvAgreement.setText(getSpannable());
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        mViewModel = new ViewModelProvider(mContext).get(LoginViewModel.class);
        mViewModel.getRegisterObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<String>(true) {
                @Override
                public void onSuccess(String data) {
                    ToastUtils.showToast(getResources().getString(R.string.em_register_success));
                    onBackPress();
                }

                @Override
                public void onError(int code, String message) {
                    if (code == EMError.USER_ALREADY_EXIST) {
                        ToastUtils.showToast(R.string.demo_error_user_already_exist);
                    } else {
                        ToastUtils.showToast(message);
                    }
                }

                @Override
                public void onLoading(String data) {
                    super.onLoading(data);
                    showLoading();
                }

                @Override
                public void hideLoading() {
                    super.hideLoading();
                    dismissLoading();
                }
            });

        });
        //切换密码可见不可见的两张图片
        eyeClose = getResources().getDrawable(R.drawable.d_pwd_hide);
        eyeOpen = getResources().getDrawable(R.drawable.d_pwd_show);
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwd, eyeClose, eyeOpen, null, null, null);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwdConfirm, eyeClose, eyeOpen, null, null, null);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkEditContent();
    }

    private void checkEditContent() {
        mUserName = mEtLoginName.getText().toString().trim();
        mPwd = mEtLoginPwd.getText().toString().trim();
        mPwdConfirm = mEtLoginPwdConfirm.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwd, eyeClose);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwdConfirm, eyeClose);
        setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm) && cbSelect.isChecked());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                registerToHx();
                break;
        }
    }

    /**
     * 获取多选值
     */
    public void updateSelectedTags() {
//        StringBuffer sb = new StringBuffer();
        // 清空selectedTags
        selectedTags.clear();
        for (CheckBox chb : checkBoxes) {
            if (chb.getTag() == null) {
                continue;
            }
            if (chb.isChecked()) {

//                if (sb.length() > 0) {
//                    sb.append(", ");
//                }
//                sb.append(chb.getTag().toString());
                // 如果Tag被选中了，那么添加到 selectedTag 里面去
                selectedTags.add(Integer.parseInt(chb.getTag().toString()));
            }
        }
//        return sb.toString();
    }


    private void registerToHx() {
        if (!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm)) {
            if (!TextUtils.equals(mPwd, mPwdConfirm)) {
                showToast(R.string.em_password_confirm_error);
                return;
            }
            updateSelectedTags();
            // 如果选中的标签数小于3，提示重新选择
            if(selectedTags.size()<3){
                showToast(R.string.myf_tags_too_few_error);
                return;
            }
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    "{\r\n  \"account\": \"" + mUserName
                            + "\",\r\n  \"nickname\": \""
                            + mUserName + "\",\r\n  \"password\": \""
                            + mPwd + "\",\r\n  \"tags\": "
                            + selectedTags.toString() + "\r\n}");
            Request request = new Request.Builder()
                    .url("http://106././.79:40010/user/register")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            // 执行注册事件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response;
                        response = client.newCall(request).execute();
                        // JSONObject jsonObject = JSONObject.fromObject(resopnse)
                        assert response.body() != null;
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        Integer appUserId = jsonObject.getInt("content");
                        System.out.println(appUserId);
                        String message = jsonObject.getString("message");
                        System.out.println(message);
                        boolean success = jsonObject.getBoolean("success");
                        System.out.println(success);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 向环信注册
                                mViewModel.register(appUserId.toString(), mPwd);
                            }
                        });


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
    }

    private void setButtonEnable(boolean enable) {
        mBtnLogin.setEnabled(enable);
        //同时需要修改右侧drawalbeRight对应的资源
//        Drawable rightDrawable;
//        if(enable) {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_enable);
//        }else {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_unable);
//        }
//        mBtnLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    @Override
    public void onBackPress(View view) {
        onBackPress();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_select:
                setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm) && isChecked);
                break;
        }
    }

    private SpannableString getSpannable() {
        SpannableString spanStr = new SpannableString(getString(R.string.em_login_agreement));
        //设置下划线
        //spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebViewActivity.actionStart(mContext, getString(R.string.em_register_service_agreement_url));
            }
        }, 2, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //spanStr.setSpan(new UnderlineSpan(), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebViewActivity.actionStart(mContext, getString(R.string.em_register_privacy_agreement_url));
            }
        }, 11, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    private abstract class MyClickableSpan extends ClickableSpan {

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.bgColor = Color.TRANSPARENT;
            ds.setColor(ContextCompat.getColor(mContext, R.color.white));
        }
    }
}
