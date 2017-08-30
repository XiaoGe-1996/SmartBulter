package com.gc.smartbulter.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.utils.L;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.view.DispatchLinearLayout;


/*
 *项目名： SmartButler
 *包名：   com.imooc.smartbutler.service
 *文件名:  SmsService
 *创建者:  LGL
 *创建时间:2016/11/202:42
 *描述:    短信监听服务
 */
public class SmsService extends Service implements View.OnClickListener {

    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;

    //窗口管理
    private WindowManager wm;
    //布局参数
    private WindowManager.LayoutParams layoutparams;
    //View
    private DispatchLinearLayout mView;

    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;

    private HomeWatchReceiver mHomeWatchReceiver;

    public static final String SYSTEM_DIALOGS_RESON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    //初始化
    private void init() {
        L.i("init service");

        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intent);

        mHomeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeWatchReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");

        //注销
        unregisterReceiver(smsReceiver);
        unregisterReceiver(mHomeWatchReceiver);
    }


    //短信广播
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StaticClass.SMS_ACTION.equals(action)) {
                L.i("来短信了");

                //获取短信内容返回的是一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object obj : objs) {
                    //把数组元素转换成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent = sms.getMessageBody();
                    L.i("短信的内容：" + smsPhone + ":" + smsContent);
                    showWindow();
                }
            }
        }
    }

    //窗口提示
    private void showWindow() {
        //获取系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutparams = new WindowManager.LayoutParams();
        //定义宽高
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutparams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutparams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutparams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutparams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        tv_phone = (TextView) mView.findViewById(R.id.tv_phone);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);
        btn_send_sms = (Button) mView.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        //设置数据
        tv_phone.setText("发件人:" + smsPhone);
        L.i("短信内容：" + smsContent);
        tv_content.setText(smsContent);

        //添加View到窗口
        wm.addView(mView, layoutparams);

        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);
    }

    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener
            = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                L.i("我按了BACK键");
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if (mView.getParent() != null) {
                    wm.removeView(mView);
                }
                break;
        }
    }

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)) {
                    L.i("我点击了HOME键");
                    if (mView.getParent() != null) {
                        wm.removeView(mView);
                    }
                }
            }
        }
    }
}
