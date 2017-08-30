package com.gc.smartbulter.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gc.smartbulter.R;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：WebViewActivity
 * 创建者  ：GC
 * 创建时间：2017/8/15 18:48
 * 描述    ：TODO
 */
public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String title;
    private String url;
    private Intent i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {
        i = getIntent();
        //设置标题
        title = i.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        url = i.getStringExtra("url");
        mWebView = (WebView) findViewById(R.id.mWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);

        //加载网页

        //支持JS
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);

        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
        });


    }

    public class WebViewClient extends WebChromeClient{
        //进度变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
