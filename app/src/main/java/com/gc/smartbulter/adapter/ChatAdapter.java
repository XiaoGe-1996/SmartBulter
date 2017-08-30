package com.gc.smartbulter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.ChatListData;

import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.adapter
 * 文件名  ：ChatAdapter
 * 创建者  ：GC
 * 创建时间：2017/8/15 8:21
 * 描述    ：对话adapter
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //左边的type
    public static final int VALUE_LEFT_TEXT = 1;
    //右边的type
    public static final int VALUE_RIGHT_TEXT = 2;

    private ChatListData data;
    private List<ChatListData> mList;
    private Context mContext;
    private LayoutInflater inflater;

    public ChatAdapter(Context mContext,List<ChatListData> mList){

        this.mContext = mContext;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    //为不同左右两边设计不同的ViewHolder
    static class LeftViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_left_text;

        public LeftViewHolder(View itemView) {
            super(itemView);
            tv_left_text = (TextView)itemView.findViewById(R.id.tv_left_text);
        }
    }

    static class RightViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_right_text;

        public RightViewHolder(View itemView) {
            super(itemView);
            tv_right_text = (TextView) itemView.findViewById(R.id.tv_right_text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //根据不同的viewType返回不同的view
        switch (viewType){
            case VALUE_LEFT_TEXT:
                return new LeftViewHolder(inflater.inflate(R.layout.left_item,parent,false));
            case VALUE_RIGHT_TEXT:
                return new RightViewHolder(inflater.inflate(R.layout.right_item,parent,false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);
        switch (viewType){
            case VALUE_LEFT_TEXT:
                ((LeftViewHolder)holder).tv_left_text.setText(mList.get(position).getText());
                break;
            case VALUE_RIGHT_TEXT:
                ((RightViewHolder)holder).tv_right_text.setText(mList.get(position).getText());
                break;
        }

    }
    //根据数据判断Type
    @Override
    public int getItemViewType(int position) {
        int type = mList.get(position).getType();
        return type;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
