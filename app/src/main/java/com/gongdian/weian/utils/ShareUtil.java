package com.gongdian.weian.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gongdian.weian.model.Users;

/**
 * Created by qian-pc on 8/12/16.
 */
public class ShareUtil {
    //持久化
    public static boolean setSharedUser(Context context, Users users) {
        SharedPreferences preferences = context.getSharedPreferences("weian_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("pid", users.getPid());
        editor.putString("headurl", users.getHeadurl());
        editor.putString("id", users.getId());
        editor.putString("imei", users.getImei());
        editor.putString("pcode", users.getPcode());
        editor.putString("pname", users.getPname());
        editor.putString("uname", users.getUname());
        editor.putString("uids", users.getUids());
        editor.putString("urole", users.getUrole());
        editor.putString("version", users.getVersion());
        return editor.commit();
    }

    public static Users getSharedUser(Context context) {
        Users users = new Users();
        SharedPreferences preferences = context.getSharedPreferences("weian_user", Context.MODE_PRIVATE);
        users.setHeadurl(preferences.getString("headurl", ""));
        users.setId(preferences.getString("id", ""));
        users.setImei(preferences.getString("imei", ""));
        users.setPcode(preferences.getString("pcode", ""));
        users.setPid(preferences.getString("pid", ""));
        users.setPname(preferences.getString("pname", ""));
        users.setUids(preferences.getString("uids", ""));
        users.setUname(preferences.getString("uname", ""));
        users.setUrole(preferences.getString("urole", ""));
        users.setVersion(preferences.getString("version", ""));
        return  users;
    }

    public static boolean setToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("weian_token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("token",token);
        return editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("weian_token", Context.MODE_PRIVATE);
        return preferences.getString("token", "");
    }

}
