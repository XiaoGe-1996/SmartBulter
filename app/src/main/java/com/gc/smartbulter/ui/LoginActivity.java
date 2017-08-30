package com.gc.smartbulter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.smartbulter.MainActivity;
import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.MyUser;
import com.gc.smartbulter.utils.L;
import com.gc.smartbulter.utils.ShareUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.gc.smartbulter.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import java.sql.SQLOutput;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：LoginActivity
 * 创建者  ：GC
 * 创建时间：2017/8/9 10:42
 * 描述    ：登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册按钮
    private Button btn_register;
    //登录按钮
    private Button btn_login;
    private EditText et_username,et_password;
    private AppCompatCheckBox keep_login;
    private TextView tv_forget;
    private CustomDialog dialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        keep_login = (AppCompatCheckBox) findViewById(R.id.keep_login);
        tv_forget = (TextView) findViewById(R.id.tv_forget);

        tv_forget.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        //参数分别是   上下文，宽，高，内容布局，样式，居中显示，动画样式
        dialog = new CustomDialog(this,WRAP_CONTENT,WRAP_CONTENT,R.layout.dialog_loding,R.style.Theme_Dialog, Gravity.CENTER,R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        //设置选中的状态
        boolean isCheck =  ShareUtils.getBoolean(this, StaticClass.SHARE_KEEP_LOGIN,false);
        keep_login.setChecked(isCheck);
        if(isCheck){
            et_username.setText(ShareUtils.getString(this,StaticClass.SHARE_USERNAME,null));
            et_password.setText(ShareUtils.getString(this,StaticClass.SHARE_PASSWORD,null));
        }

    }

    private void keppLogin(){
        //保存状态
        ShareUtils.putBoolean(this,StaticClass.SHARE_KEEP_LOGIN,keep_login.isChecked());

        //是否记住密码
        if(keep_login.isChecked()){
            ShareUtils.putString(this,StaticClass.SHARE_USERNAME,et_username.getText().toString().trim());
            ShareUtils.putString(this,StaticClass.SHARE_PASSWORD,et_password.getText().toString().trim());
        }else {
            ShareUtils.delSingleShare(this,StaticClass.SHARE_USERNAME);
            ShareUtils.delSingleShare(this,StaticClass.SHARE_PASSWORD);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.btn_login:
                //1.获取数据框的值
                String name = et_username.getText().toString().trim();
                String password =  et_password.getText().toString().trim();
                //登录
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)){
                    dialog.show();
                    final MyUser user =  new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);

                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断结果
                            if(e==null){
                                //判断邮箱是否验证
                                if(user.getEmailVerified()){
                                    keppLogin();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    UtilTools.showShrotToast(getApplicationContext(),"请前往邮箱验证");
                                }
                            }else {
                                UtilTools.showShrotToast(getApplicationContext(),"登陆失败");
                            }
                        }
                    });

                }else{
                    UtilTools.showShrotToast(this,"输入框不能为空");
                }

                break;
        }
    }
}
