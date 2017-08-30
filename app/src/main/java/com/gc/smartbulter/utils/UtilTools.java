package com.gc.smartbulter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.utils
 * 文件名  ：UtilTools
 * 创建者  ：GC
 * 创建时间：2017/8/7 11:17
 * 描述    ：工具统一类
 */
public class UtilTools {

    //设置字体
    public static void setFont(Context mContext,TextView textView){
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }

    //弹出Toast
    public static void showShrotToast(Context mContext,String string){
        Toast.makeText(mContext,string,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context mContext,String string){
        Toast.makeText(mContext,string,Toast.LENGTH_LONG).show();
    }

    //保存图片到share
    public static void putImageToShare(Context mContext, ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将BitMap压缩成字节输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byStream);
        //第二步:利用Base64将字节数组输出流转换成String
        byte [] byteArray = byStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步：将String 保存在SharePerfences
        ShareUtils.putString(mContext,"image_title",imageString);
    }

    //读取图片
    public static void getImageToShare(Context mContext, ImageView imageView){
        //拿到图片String
        String imageString = ShareUtils.getString(mContext,"image_title","");
        if(!imageString.equals("")){
            //利用Base64将String转换
            byte [] byteArray = Base64.decode(imageString,Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
            //生成bitMap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);
        }
    }



}
