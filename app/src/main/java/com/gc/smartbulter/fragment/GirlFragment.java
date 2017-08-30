package com.gc.smartbulter.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.gc.smartbulter.R;
import com.gc.smartbulter.adapter.GirlAdapter;
import com.gc.smartbulter.entity.GirlData;
import com.gc.smartbulter.utils.GsonUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.gc.smartbulter.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.GoogleDotView;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class GirlFragment extends Fragment implements View.OnClickListener {

    //美女
    private RecyclerView mGridView;
    private GirlAdapter mAdapter;
    private List<GirlData> mList = new ArrayList<>();
    private GirlData mData;

    //提示框
    private CustomDialog dialog,dialog1;
    //预览图片
    private PhotoView pv_img;

    private int num;
    private String url;
    private int width,height;
    private WindowManager wm;
    private TwinklingRefreshLayout refreshLayout;
    private ImageView iv_more;
    private Button btn_saveimg,btn_cancel;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        activity = this.getActivity();
        refreshLayout.startRefresh();
        return view;
    }

    //初始化View
    private void findView(View view) {

        wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        width = (width * 3) / 4;
        height = wm.getDefaultDisplay().getHeight();
        height = (height * 3) / 4;

        mGridView = (RecyclerView) view.findViewById(R.id.mGridView);
        mGridView.setLayoutManager(new GridLayoutManager(getContext(),2));
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);

        //设置默认header
        SinaRefreshView googleDotView = new SinaRefreshView(getContext());
        refreshLayout.setHeaderView(googleDotView);
        //刷新
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        refreshLayout.finishRefreshing();
                    }
                },2000);
            }
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });

        //初始化,显示图片dialog
        dialog = new CustomDialog(getActivity(),MATCH_PARENT,MATCH_PARENT, R.layout.dialog_girl, R.style.Theme_GirlDialog, Gravity.CENTER);
        //屏幕外点击无效
        dialog.setCancelable(true);

        //图片保存到本地dialog
        dialog1 = new CustomDialog(getActivity(),MATCH_PARENT,WRAP_CONTENT, R.layout.dialog_saveimg, R.style.Theme_GirlDialog, Gravity.BOTTOM);

        //显示图片dialog中的控件实例化
        pv_img = (PhotoView) dialog.findViewById(R.id.pv_img);
        iv_more = (ImageView) dialog.findViewById(R.id.iv_more);
        //保存图片dialog中的控件实例化
//        btn_saveimg = (Button) dialog1.findViewById(R.id.btn_saveimg);
        btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        // 启用图片缩放功能
        pv_img.enable();

        pv_img.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //长按可以保存图片
        pv_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog1.show();
                return false;
            }
        });
    }

    //加载更多数据
    private void loadMoreData() {
        num ++;
        url = "http://api.huceo.com/meinv/?key="+ StaticClass.GIRL_KEY+"&num="+String.valueOf(20)+"&page="+String.valueOf(num);
        // 网络请求获取数据
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String girls = jsonObject.getString("newslist");
                    List<GirlData> list = GsonUtils.jsonToArrayList(girls, GirlData.class);
                    for(int i = 0;i<list.size();i++){
                        mList.add(list.get(i));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //初始化数据
    private void refreshData() {
        num = 1;
        url = "http://api.huceo.com/meinv/?key="+ StaticClass.GIRL_KEY+"&num="+String.valueOf(20)+"&page="+String.valueOf(num);
        // 网络请求获取数据
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String girls = jsonObject.getString("newslist");
                    mList = GsonUtils.jsonToArrayList(girls, GirlData.class);

                    mAdapter = new GirlAdapter(getActivity(), mList,dialog,dialog1,activity);
                    //设置适配器
                    mGridView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pv_img:
                dialog.dismiss();
                break;
            case R.id.iv_more:
                dialog1.show();
                break;
            case R.id.btn_cancel:
                dialog1.dismiss();
                break;
        }
    }
}
