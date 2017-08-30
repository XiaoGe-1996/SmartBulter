package com.gc.smartbulter.utils;

import android.util.Log;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.utils
 * 文件名  ：L
 * 创建者  ：GC
 * 创建时间：2017/8/7 16:37
 * 描述    ：TODO
 */
public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "Smartbutler";

    //5个等级 d，i，w，e，f
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }

    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }

}
