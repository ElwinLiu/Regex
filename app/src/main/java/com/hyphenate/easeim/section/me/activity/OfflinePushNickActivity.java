package com.hyphenate.easeim.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeim.R;
import com.hyphenate.easeim.common.constant.DemoConstant;
import com.hyphenate.easeim.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.easeim.common.livedatas.LiveDataBus;
import com.hyphenate.easeim.common.utils.PreferenceManager;
import com.hyphenate.easeim.section.base.BaseInitActivity;
import com.hyphenate.easeim.section.me.viewmodels.OfflinePushSetViewModel;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.EMLog;
import com.hyphenate.chat.EMUserInfo.*;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
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

public class OfflinePushNickActivity extends BaseInitActivity implements OnClickListener, TextWatcher {
    static private String TAG = "OfflinePushNickActivity";
    private EaseTitleBar titleBar;
    private EditText inputNickName;
    private Button saveNickName;
    private String nickName;
    private OfflinePushSetViewModel viewModel;

    List<CheckBox> checkBoxes = new ArrayList<>(); // 多选组
    List<Integer> selectedTags = new ArrayList<>();

    boolean isShowInfo = false;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, OfflinePushNickActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        if (intent != null) {
            nickName = intent.getStringExtra("nickName");
            // 发送请求，获取detail信息
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
//            MediaType mediaType = MediaType.parse("text/plain");
//            RequestBody body = RequestBody.create(mediaType, new byte[0]);
            // 获取 token
            SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
            String token = sharedPreferences.getString("appUserToken", "");
            // 获取 appUserId
            Integer appUserId = sharedPreferences.getInt("appUserId", 0);
            Request request = new Request.Builder()
                    .url("http://106.14.78.79:40010/user/detail?id=" + appUserId.toString())
                    .get()
                    .addHeader("Authorization", token)
                    .build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = client.newCall(request).execute();
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray tagsArray = jsonObject.getJSONArray("tags");

                        // 处理JSONArray
                        for (int i = 0; i < tagsArray.length(); i++) {
                            JSONObject jsonTag = tagsArray.getJSONObject(i);
                            int tagId = jsonTag.getInt("id");
                            // 将含有的标签标记为true
                            // 至于为什么有 1 个 if-else 这样的逻辑是因为后端接口提供的 Id 不是连续的……
                            if (tagId < 4) {
                                checkBoxes.get(tagId - 1).setChecked(true);
                            } else {
                                checkBoxes.get(tagId - 2).setChecked(true);
                            }
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_offline_push;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        inputNickName = (EditText) findViewById(R.id.et_input_nickname);
        saveNickName = (Button) findViewById(R.id.btn_save);

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

        // 发送请求，获取detail信息
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
//            RequestBody body = RequestBody.create(mediaType, new byte[0]);
        // 获取 token
        SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("appUserToken", "");
        // 获取 appUserId
        int appUserId = sharedPreferences.getInt("appUserId", 0);
        Request request = new Request.Builder()
                .url("http://106.14.78.79:40010/user/detail?id=" + Integer.toString(appUserId))
                .get()
                .addHeader("Authorization", token)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray tagsArray = jsonObject.getJSONArray("tags");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 处理JSONArray
                            for (int i = 0; i < tagsArray.length(); i++) {
                                JSONObject jsonTag = null;
                                try {
                                    jsonTag = tagsArray.getJSONObject(i);
                                    int tagId = jsonTag.getInt("id");
                                    // 将含有的标签标记为true
                                    // 至于为什么有 1 个 if-else 这样的逻辑是因为后端接口提供的 Id 不是连续的……
                                    if (tagId < 4) {
                                        checkBoxes.get(tagId - 1).setSelected(true);
                                    } else {
                                        checkBoxes.get(tagId - 2).setSelected(true);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(new EaseTitleBar.OnBackPressListener() {
            @Override
            public void onBackPress(View view) {
                onBackPressed();
            }
        });
        saveNickName.setOnClickListener(this);
        inputNickName.addTextChangedListener(this);
    }

    /**
     * 获取多选值
     */
    public void updateSelectedTags() {
        // 清空selectedTags
        selectedTags.clear();
        for (CheckBox chb : checkBoxes) {
            if (chb.getTag() == null) {
                continue;
            }
            if (chb.isChecked()) {
                // 如果Tag被选中了，那么添加到 selectedTag 里面去
                selectedTags.add(Integer.parseInt(chb.getTag().toString()));
            }
        }
    }

    @Override
    public void onClick(View v) {
//        if (!isShowInfo) {
//            // 发送请求，获取detail信息
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .build();
//            MediaType mediaType = MediaType.parse("text/plain");
////            RequestBody body = RequestBody.create(mediaType, new byte[0]);
//            // 获取 token
//            SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
//            String token = sharedPreferences.getString("appUserToken", "");
//            // 获取 appUserId
//            int appUserId = sharedPreferences.getInt("appUserId", 0);
//            Request request = new Request.Builder()
//                    .url("http://106.14.78.79:40010/user/detail?id=" + Integer.toString(appUserId))
//                    .get()
//                    .addHeader("Authorization", token)
//                    .build();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Response response = client.newCall(request).execute();
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        JSONArray tagsArray = jsonObject.getJSONArray("tags");
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // 处理JSONArray
//                                for (int i = 0; i < tagsArray.length(); i++) {
//                                    JSONObject jsonTag = null;
//                                    try {
//                                        jsonTag = tagsArray.getJSONObject(i);
//                                        int tagId = jsonTag.getInt("id");
//                                        // 将含有的标签标记为true
//                                        // 至于为什么有 1 个 if-else 这样的逻辑是因为后端接口提供的 Id 不是连续的……
//                                        if (tagId < 4) {
//                                            checkBoxes.get(tagId - 1).setChecked(true);
//                                        } else {
//                                            checkBoxes.get(tagId - 2).setChecked(true);
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                }
//                            }
//                        });
//
//
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//            isShowInfo = true;
//            return;
//        }

        if (v.getId() == R.id.btn_save) {
            String nick = inputNickName.getText().toString();
            if (nick != null && nick.length() > 0) {
                // 更新 selectedTags
                updateSelectedTags();
                if (selectedTags.size() < 3) {
                    showToast(R.string.myf_tags_too_few_error);
                    return;
                }
                EMClient.getInstance().userInfoManager().updateOwnInfoByAttribute(EMUserInfoType.NICKNAME, nick, new EMValueCallBack<String>() {
                    @Override
                    public void onSuccess(String value) {
                        EMLog.d(TAG, "fetchUserInfoById :" + value);
                        showToast(R.string.demo_offline_nickname_update_success);
                        nickName = nick;
                        PreferenceManager.getInstance().setCurrentUserNick(nick);


                        // 在这里更新 App 数据库
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"nickname\": \""
                                + nickName + "\",\r\n  \"tags\": " + selectedTags.toString() + "\r\n}");
                        SharedPreferences sharedPreferences = getSharedPreferences("appUserInfo", MODE_PRIVATE);
                        String token = sharedPreferences.getString("appUserToken", "");
                        Request request = new Request.Builder()
                                .url("http://106.14.78.79:40010/user/modify")
                                .method("PUT", body)
                                .addHeader("Authorization", token)
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject = new JSONObject(response.body().string());

                            // 成功了继续
                            // 没成功 弹出更新失败，然后返回
                            if (!jsonObject.getBoolean("success")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OfflinePushNickActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                });
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                        EaseEvent event = EaseEvent.create(DemoConstant.NICK_NAME_CHANGE, EaseEvent.TYPE.CONTACT);
                        //发送联系人更新事件
                        event.message = nick;
                        LiveDataBus.get().with(DemoConstant.NICK_NAME_CHANGE).postValue(event);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //同时更新推送昵称
                                viewModel.updatePushNickname(nick);
                            }
                        });
                    }

                    @Override
                    public void onError(int error, String errorMsg) {
                        EMLog.d(TAG, "fetchUserInfoById  error:" + error + " errorMsg:" + errorMsg);
                        showToast(R.string.demo_offline_nickname_update_failed);
                    }
                });
            } else {
                showToast(R.string.demo_offline_nickname_is_empty);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (nickName != null && nickName.length() > 0) {
            inputNickName.setText(nickName);
        } else {
            inputNickName.setText(EMClient.getInstance().getCurrentUser());
        }

        viewModel = new ViewModelProvider(this).get(OfflinePushSetViewModel.class);
        viewModel.getUpdatePushNicknameObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    getIntent().putExtra("nickName", nickName);
                    setResult(RESULT_OK, getIntent());
                    finish();
                }

                @Override
                public void onLoading(Boolean data) {
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
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
