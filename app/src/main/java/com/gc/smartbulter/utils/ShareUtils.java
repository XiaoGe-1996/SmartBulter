package com.gc.smartbulter.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.utils
 * 文件名  ：ShareUtils
 * 创建者  ：GC
 * 创建时间：2017/8/7 18:53
 * 描述    ：SharedPreferences 封装
 */
public class ShareUtils {

    //单例模式
    private ShareUtils() {}
    private static ShareUtils single=null;
    //静态工厂方法
    public static ShareUtils getInstance() {
        if (single == null) {
            single = new ShareUtils();
        }
        return single;
    }




    public static final String spName = "config";

    //存入String数据
    public static void putString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }
    //读取String数据   键，默认值
    public static String getString(Context mContext,String key,String defValue){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    //存入Int数据
    public static void putInt(Context mContext,String key,int value){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
    //读取Int数据   键，默认值
    public static int getInt(Context mContext,String key,int defValue){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    //存入Int数据
    public static void putBoolean(Context mContext,String key,Boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }
    //读取Int数据   键，默认值
    public static boolean getBoolean(Context mContext, String key, Boolean defValue){
        SharedPreferences sp = mContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    //删除单个
    public static void delSingleShare(Context mContext, String key){
        SharedPreferences sp = mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    //删除所有
    public static void delAllShare(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


}
