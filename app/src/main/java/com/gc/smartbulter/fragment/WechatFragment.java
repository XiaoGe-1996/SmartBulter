package com.gc.smartbulter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.gc.smartbulter.R;
import com.gc.smartbulter.adapter.WeChatAdapter;
import com.gc.smartbulter.entity.WeChatData;
import com.gc.smartbulter.ui.WebViewActivity;
import com.gc.smartbulter.utils.GsonUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class WechatFragment extends Fragment {

    private RecyclerView mRecyclerView_wechat;
    private PtrClassicFrameLayout mPtrFrame;
    private List<WeChatData> mList;
    private WeChatAdapter mWeChatAdapter;
    private RecyclerAdapterWithHF mAdapter;

    private boolean isFrist = true;
    private int pno = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        return view;
    }

    private void findView(final View view) {

        mList = new ArrayList<WeChatData>();
        mRecyclerView_wechat = (RecyclerView) view.findViewById(R.id.mRecyclerView_wechat);
        mRecyclerView_wechat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.mPtrFrame);
        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        //下拉刷新一些设置 详情参考文档
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        //进入Activity就进行自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);


        //下拉刷新
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                //模拟联网 延迟更新列表
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=20" + "&pno=" + String.valueOf(1);
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                prassJson(t);
                                mPtrFrame.refreshComplete();
                                mPtrFrame.setLoadMoreEnable(true);
                            }
                        });


                    }
                }, 1000);
            }
        });
        //上拉加载
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                //模拟联网 延迟更新列表
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //解析接口
                        pno = pno + 1;
                        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=10" + "&pno=" + String.valueOf(pno);
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                loadJson(t);
                                //异步操作要在这里面设置数据
                                mPtrFrame.loadMoreComplete(true);
                            }
                        });
                    }
                }, 500);
//                        mAdapter.notifyDataSetChanged();
                mPtrFrame.loadMoreComplete(true);
            }
        });
    }


    //解析json数据
    private void prassJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String wechat = jsonResult.getString("list");
            mList = GsonUtils.jsonToArrayList(wechat, WeChatData.class);
            mWeChatAdapter = new WeChatAdapter(getContext(), mList);
            mWeChatAdapter.setmOnItemClickListener(new WeChatAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(WeChatData data) {
                    Intent i = new Intent(getActivity(), WebViewActivity.class);
                    i.putExtra("title", data.getTitle());
                    i.putExtra("url", data.getUrl());
                    startActivity(i);
                }
            });
            mAdapter = new RecyclerAdapterWithHF(mWeChatAdapter);
            mRecyclerView_wechat.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void loadJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String wechat = jsonResult.getString("list");
            List<WeChatData> list = GsonUtils.jsonToArrayList(wechat, WeChatData.class);
            for (int i = 0; i < list.size(); i++) {
                mList.add(list.get(i));
                System.out.println(list.get(i).getTitle());
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
