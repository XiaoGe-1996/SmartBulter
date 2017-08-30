package com.gc.smartbulter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.CourierData;
import com.gc.smartbulter.entity.MyUser;

import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.adapter
 * 文件名  ：CourierAdapter
 * 创建者  ：GC
 * 创建时间：2017/8/12 15:41
 * 描述    ：物流时间轴适配器
 */
public class CourierAdapter extends RecyclerView.Adapter<CourierAdapter.ViewHolder> {

    private List<CourierData> mList;
    private Context mContext;
    private LayoutInflater inflater;
    private CourierData data;

    //构造方法
    public CourierAdapter(Context mContext,List<CourierData> mList){
        this.mContext = mContext;
        this.mList = mList;
//        inflater = LayoutInflater.from(mContext);
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    //用来实例化每一行的布局里的控件
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_remark;
        private TextView tv_zone;
        private TextView tv_datetime;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);
            tv_zone = (TextView) itemView.findViewById(R.id.tv_zone);
            tv_datetime = (TextView) itemView.findViewById(R.id.tv_datetime);
        }
    }

    //绑定每一行的布局，并将其传给ViewHolder
    @Override
    public CourierAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_courier_item,parent,false);
        return new CourierAdapter.ViewHolder(view);
    }

    //通过holder对里面的控件进行一系列的操作
    @Override
    public void onBindViewHolder(CourierAdapter.ViewHolder holder, int position) {

        //设置数据
        data = mList.get(mList.size()-position-1);
        holder.tv_remark.setText(data.getRemark());
        holder.tv_zone.setText(data.getZone());
        holder.tv_datetime.setText(data.getDatetime());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
