package com.gc.smartbulter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.widget.ImageView;

import com.gc.smartbulter.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.utils
 * 文件名  ：PicassoUtils
 * 创建者  ：GC
 * 创建时间：2017/8/15 15:24
 * 描述    ：TODO
 */
public class PicassoUtils {

    //默认加载图片
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }

    //默认加载图片(指定大小)
    public static void loadImageViewSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext).load(url).config(Bitmap.Config.RGB_565).resize(width, height).centerCrop().into(imageView);
    }

    //加载图片有默认图片
    public static void loadImageViewHolder(Context mContext, String url,int width, int height, int loadImg,
                                           int errorImg, ImageView imageView) {
        Picasso.with(mContext).load(url).config(Bitmap.Config.RGB_565).resize(width, height).centerCrop().placeholder(loadImg).error(errorImg)
                .into(imageView);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context mContext, String url,ImageView imageView){
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation(imageView)).into(imageView);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {

        private ImageView mImg;
        public CropSquareTransformation(ImageView mImg){
            this.mImg = mImg;
        }
        @Override
        public Bitmap transform(Bitmap source) {

            int targetWidth = mImg.getWidth();

            if(source.getWidth()==0){
                return source;
            }
            //如果图片小于设置的宽度，则返回原图
            if(source.getWidth()<targetWidth){
                return source;
            }else{
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }

        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };
}
