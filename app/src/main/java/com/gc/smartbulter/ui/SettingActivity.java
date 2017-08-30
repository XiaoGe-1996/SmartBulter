package com.gc.smartbulter.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.service.SmsService;
import com.gc.smartbulter.utils.ShareUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：SettingActivity
 * 创建者  ：GC
 * 创建时间：2017/8/7 15:28
 * 描述    ：设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Switch sw_speak;
    private Switch sw_sms;
    private LinearLayout ll_update,ll_scan,ll_qr_code,ll_my_location;
    private TextView tv_version;

    private String versionName;
    private ImageView iv_erweima;
    private int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {

        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);
        sw_sms = (Switch) findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = (TextView) findViewById(R.id.tv_version);
        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        ll_qr_code = (LinearLayout) findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
        ll_my_location = (LinearLayout) findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);

        boolean isSpeak = ShareUtils.getBoolean(this,"isSpeak",false);
        sw_speak.setChecked(isSpeak);

        boolean isSms = ShareUtils.getBoolean(this,"isSms",false);
        sw_sms.setChecked(isSms);


        getVersionNameCode();
        tv_version.setText("检测版本:"+versionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                ShareUtils.putBoolean(this,"isSpeak",sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_sms.isSelected());
                //保存状态
                ShareUtils.putBoolean(this,"isSms",sw_speak.isChecked());
                if(sw_sms.isChecked()){
                    //启动短信服务
                    startService(new Intent(this,SmsService.class));
                }else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;

            case R.id.ll_update:
                /**
                 * 1.请求服务器配置文件，拿到code
                 * 2.比较
                 * 3.dialog提示
                 * 4.跳转到更新界面，并把url传递过去
                 *
                 */
                break;
            case R.id.ll_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
                startActivity(intent);
                break;

            case R.id.ll_qr_code:
                Bitmap mBitmap = CodeUtils.createImage("123",400, 400, BitmapFactory.decodeResource(getResources(),R.drawable.logo_everyday));
                iv_erweima.setImageBitmap(mBitmap);
                break;

            case R.id.ll_my_location:
                startActivity(new Intent(this,LocationActivity.class));
                break;

        }
    }

    //获取版本号code
    private void getVersionNameCode(){
        try {
            PackageManager pm = getPackageManager();
            PackageInfo info  = pm.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
