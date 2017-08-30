package com.gc.smartbulter.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.MyUser;
import com.gc.smartbulter.utils.UtilTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：ForgetPasswordActivity
 * 创建者  ：GC
 * 创建时间：2017/8/9 16:21
 * 描述    ：TODO
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_now,et_new,et_new1,et_email;
    private Button btn_update_password,btn_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView();
    }

    private void initView() {
        et_now = (EditText) findViewById(R.id.et_now);
        et_new = (EditText) findViewById(R.id.et_new);
        et_new1 = (EditText) findViewById(R.id.et_new_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);

        btn_forget_password.setOnClickListener(this);
        btn_update_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:

                //1.获取输入框的邮箱
                final String email = et_email.getText().toString().trim();
                //2.判断是否为空
                if(!TextUtils.isEmpty(email)){
                    //3.发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                UtilTools.showShrotToast(getApplicationContext(),"邮箱已经发送至："+email);
                                finish();
                            }else {
                                UtilTools.showShrotToast(getApplicationContext(),"邮箱发送失败");
                            }
                        }
                    });
                }


                break;
            case R.id.btn_update_password:

                //获取值
                String pwd_now = et_now.getText().toString().trim();
                String pwd_new = et_new.getText().toString().trim();
                String pwd_new1 = et_new1.getText().toString().trim();
                //判断是否为空
                if(!TextUtils.isEmpty(pwd_now) & !TextUtils.isEmpty(pwd_new) & !TextUtils.isEmpty(pwd_new1)){
                    //判断两次新密码是否相同
                    if(pwd_new.equals(pwd_new1)){
                        //重置密码
                        MyUser.updateCurrentUserPassword(pwd_now, pwd_new, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    UtilTools.showShrotToast(getApplicationContext(),"重置密码成功");
                                    finish();
                                }else {
                                    UtilTools.showShrotToast(getApplicationContext(),"重置密码失败");
                                }
                            }
                        });

                    }else {
                        et_new1.setError("两次密码输入不一致");
                    }
                }else {
                    UtilTools.showShrotToast(getApplicationContext(),"输入框不能为空");
                }


                break;
        }
    }
}
