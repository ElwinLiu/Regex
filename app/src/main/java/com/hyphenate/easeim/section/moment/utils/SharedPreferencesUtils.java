package com.hyphenate.easeim.section.moment.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {


    /**
     * 获取本地token, 若不存在返回null
     * @param context
     * @return token
     */
    static public String getToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("appUserInfo", Context.MODE_PRIVATE);
        String token = pref.getString("appUserToken",null);
        return token;
    }


    /**
     * 更新token, 原先不存在则新建
     * @param token
     * @param context
     */
    static public void updateToken(String token, Context context){
        SharedPreferences pref = context.getSharedPreferences("appUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("appUserToken",token);
        editor.commit();
    }


    /**
     * 获取本地用户id, 若不存在返回0
     * @param context
     * @return
     */
    static public int getId(Context context){
        SharedPreferences pref = context.getSharedPreferences("appUserInfo", Context.MODE_PRIVATE);
        int id = pref.getInt("appUserId",0);
        return id;
    }


    /**
     * 更新用户id, 原先不存在则新建
     * @param id
     * @param context
     */
    static public void updateId(int id,Context context){
        SharedPreferences pref = context.getSharedPreferences("appUserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("appUserId",id);
        editor.commit();
    }




}
