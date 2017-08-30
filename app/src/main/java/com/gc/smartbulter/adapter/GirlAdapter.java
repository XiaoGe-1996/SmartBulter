package com.gc.smartbulter.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.gc.smartbulter.MainActivity;
import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.GirlData;
import com.gc.smartbulter.utils.PicassoUtils;
import com.gc.smartbulter.utils.SaveImageUtils;
import com.gc.smartbulter.utils.UtilTools;
import com.gc.smartbulter.view.CustomDialog;

import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.adapter
 * 文件名  ：GirlAdapter
 * 创建者  ：GC
 * 创建时间：2017/8/17 11:17
 * 描述    ：美女适配器
 */
public class GirlAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<GirlData> mList;
    private LayoutInflater inflater;
    private GirlData data;
    private int width, height;
    private WindowManager wm;
    private CustomDialog dialog, dialog1;
    private PhotoView dialog_iv;
    private Button btn_saveimg;
    private Activity activity;

    public GirlAdapter(Context mContext, List<GirlData> mList, CustomDialog dialog, CustomDialog dialog1, Activity activity) {
        this.mContext = mContext;
        this.mList = mList;
        this.dialog = dialog;
        this.dialog1 = dialog1;
        this.activity = activity;
        inflater = LayoutInflater.from(mContext);
        dialog_iv = (PhotoView) dialog.findViewById(R.id.pv_img);
        btn_saveimg = (Button) dialog1.findViewById(R.id.btn_saveimg);


        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tv_title;
        private TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.girl_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        data = mList.get(position);
        //解析图片
        final String url = data.getPicUrl();
        PicassoUtils.loadImageViewHolder(mContext, url, width / 2 - 20, width / 2 - 20, R.drawable.isloading, R.drawable.loading_fail, viewHolder.imageView);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_time.setText(data.getCtime());

        //点击图片，展示图片信息
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicassoUtils.loadImageView(mContext, url, dialog_iv);
                dialog.show();
                //保存图片
                saveImg(url);

            }
        });
    }

    //保存图片按钮
    private void saveImg(final String url) {
        btn_saveimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final Bitmap myBitmap = Glide.with(mContext)//上下文
                                        .load(url)//url
                                        .asBitmap() //必须
                                        .centerCrop()
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)//保存图片，保持原始宽高
                                        .get();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SaveImageUtils.saveImageToGallerys(mContext, myBitmap);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    UtilTools.showShrotToast(mContext,"该图片不能保存");
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
