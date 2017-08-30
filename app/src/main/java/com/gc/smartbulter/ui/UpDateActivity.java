package com.gc.smartbulter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.gc.smartbulter.R;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

/*
 *项目名： SmartButler
 *包名：   com.imooc.smartbutler.ui
 *文件名:  UpdatActivity
 *创建者:  LGL
 *创建时间:2016/11/272:43
 *描述:   下载
 */
public class UpDateActivity extends BaseActivity {

    //正在下载
    public static final int HANDLER_LODING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_ON = 10003;


    private TextView tv_size;
    private String url;
    private String path;

    //进度条
    private NumberProgressBar number_progress_bar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LODING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes + " / " + totalSize);
                    //设置进度

                    // 30%  --- 100%   number_progress_bar.setpar(30)
                    //  5200.0 / 52000.0  10.0%  10 / 100  = 100%
                    number_progress_bar.setProgress((int) (((float) transferredBytes / (float) totalSize) * 100));

                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功");
                    //启动这个应用安装
                    startInstallApk();
                    break;
                case HANDLER_ON:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装
    private void startInstallApk() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size = (TextView) findViewById(R.id.tv_size);
        number_progress_bar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        number_progress_bar.setMax(100);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";

        //下载
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)) {
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    //L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message msg = new Message();
                    msg.what = HANDLER_LODING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    handler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    handler.sendEmptyMessage(HANDLER_ON);
                }
            });
        }
    }
}
