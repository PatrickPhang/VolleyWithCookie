package com.example.volleydemo;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    public static void saveSettingNote(Context context, String key, String saveData){//保存设置
        SharedPreferences.Editor note = context.getSharedPreferences("Test",context.MODE_PRIVATE).edit();
        note.putString(key, saveData);
        note.commit();
    }
    public static String getSettingNote(Context context,String s){//获取保存设置
        SharedPreferences read = context.getSharedPreferences("Test", context.MODE_PRIVATE);
        return read.getString(s, "");
    }
}
