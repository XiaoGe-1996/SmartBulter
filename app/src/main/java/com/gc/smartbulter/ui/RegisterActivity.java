package com.gc.smartbulter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gc.smartbulter.R;
import com.gc.smartbulter.entity.MyUser;
import com.gc.smartbulter.utils.UtilTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：RegisterActivity
 * 创建者  ：GC
 * 创建时间：2017/8/9 10:55
 * 描述    ：注册
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_user,et_age,et_desc,et_pass,et_password,et_email;
    private RadioGroup mRadioGroup;
    private Button btn_reister;
    private boolean defSex = true;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_reister = (Button) findViewById(R.id.btnRegistered);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);

        btn_reister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegistered:
                //获取用户名， .trim()方法是去空格
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String passwrod = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                //判断是否为空
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &!TextUtils.isEmpty(pass) &!TextUtils.isEmpty(passwrod) &!TextUtils.isEmpty(email)){
                    //判断密码是否一致
                    if(pass.equals(passwrod)){
                        //判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                if(checkedId == R.id.rb_boy){
                                    defSex = true;
                                }else if(checkedId == R.id.rb_girl){
                                    defSex = false;
                                }
                            }
                        });
                        //判断简介是否为空
                        if(TextUtils.isEmpty(desc)){
                            desc = "这个人很懒，什么都没有留下。";
                        }

                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(pass);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(defSex);
                        user.setDesc(desc);

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e==null){
                                    UtilTools.showShrotToast(getApplicationContext(),"注册成功");
                                    finish();
                                }else {
                                    UtilTools.showShrotToast(getApplicationContext(),"注册失败"+e.toString());
                                }
                            }
                        });
                    }else {
                        et_password.setError("两次输入的密码不一致！");
                    }



                }else {
                    UtilTools.showShrotToast(this,"输入框不能为空！");
                }
                break;
        }

    }
}
