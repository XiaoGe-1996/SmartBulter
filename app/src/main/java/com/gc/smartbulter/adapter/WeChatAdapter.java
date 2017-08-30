package com.gc.smartbulter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.WeChatData;
import com.gc.smartbulter.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.adapter
 * 文件名  ：WeChatAdapter
 * 创建者  ：GC
 * 创建时间：2017/8/15 14:17
 * 描述    ：微信精选适配器
 */
public class WeChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<WeChatData> mList = new ArrayList<>();
    private LayoutInflater inflater;
    private WeChatData data;
    private int width,height;
    private WindowManager wm;

    public WeChatAdapter(Context mContext,List<WeChatData> mList){
        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(WeChatData data);
    }

    public void setmOnItemClickListener(WeChatAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_source = (TextView) itemView.findViewById(R.id.tv_source);
        }

    }

    @Override
    public WeChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wechat_item,parent,false);
        return new WeChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        WeChatAdapter.ViewHolder viewHolder = (WeChatAdapter.ViewHolder)holder;
        data = mList.get(position);
        if(!TextUtils.isEmpty(data.getFirstImg())){
//            PicassoUtils.loadImaheView(mContext,data.getFirstImg(),viewHolder.iv_img);
           PicassoUtils.loadImageViewSize(mContext, data.getFirstImg(), 150, 120, viewHolder.iv_img);
        }
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(mList.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
