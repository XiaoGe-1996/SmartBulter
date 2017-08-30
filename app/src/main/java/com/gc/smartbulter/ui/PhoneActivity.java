package com.gc.smartbulter.ui;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.gc.smartbulter.R;
import com.gc.smartbulter.utils.GsonUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：PhoneActivity
 * 创建者  ：GC
 * 创建时间：2017/8/14 15:24
 * 描述    ：归属地查询
 */
public class PhoneActivity extends BaseActivity implements View.OnClickListener {

    //输入框
    private EditText et_number;
    //公司logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;

    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_del, btn_query;

    //标记位
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        
        initView();
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    //获取到输入框的内容

    @Override
    public void onClick(View v) {
        String str = et_number.getText().toString();
        /*
        * 1.获取数据框内容
        * 2.判断是否为空
        * 3.网络请求
        * 4.解析json
        * 5.结果显示
        *
        * 键盘逻辑
        * */

        //每次结尾添加1
        switch(v.getId()){
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:

                if (flag) {
                    flag = false;
                    str = "";
                    et_number.setText("");
                }
                et_number.setText(str + ((Button) v).getText());
                //移动光标
                et_number.setSelection(str.length() + 1);
                break;

            case R.id.btn_del:
                if (!TextUtils.isEmpty(str) && str.length() > 0) {
                    //每次结尾减去1
                    et_number.setText(str.substring(0, str.length() - 1));
                    //移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;

            case R.id.btn_query:
                if (!TextUtils.isEmpty(str)) {

                    if (testPhone(str)){
                        getPhone(str);
                    }else {
                        UtilTools.showShrotToast(getApplicationContext(),"请输入正确的手机号码");
                    }

                }else {
                    UtilTools.showShrotToast(getApplicationContext(),"号码不能为空");
                }
                break;
        }
    }

    //判断是不是手机号码
    private boolean testPhone(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //获取归属地
    private void getPhone(String str) {
        String url = "http://apis.juhe.cn/mobile/get?phone="+str+"&key="+ StaticClass.PHONE_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
                System.out.println(t);
            }
        });
    }

    /**
     * "province":"浙江",
     * "city":"杭州",
     * "areacode":"0571",
     * "zip":"310000",
     * "company":"中国移动",
     * "card":"移动动感地带卡"
     */

    //解析Json
    private void parsingJson(String t) {
        try {

            JSONObject jsonObject = new JSONObject(t);
            System.out.println(String.valueOf(jsonObject));
            JSONObject jsonResult = jsonObject.getJSONObject("result");

                String province = jsonResult.getString("province");
                String city = jsonResult.getString("city");
                String areacode = jsonResult.getString("areacode");
                String zip = jsonResult.getString("zip");
                String company = jsonResult.getString("company");
                String card = jsonResult.getString("card");

                tv_result.setText("归属地:" + province + city + "\n"
                        + "区号:" + areacode + "\n"
                        + "邮编:" + zip + "\n"
                        + "运营商:" + company);

                //图片显示
                switch (company) {
                    case "移动":
                        iv_company.setBackgroundResource(R.drawable.china_mobile);
                        break;
                    case "联通":
                        iv_company.setBackgroundResource(R.drawable.china_unicom);
                        break;
                    case "电信":
                        iv_company.setBackgroundResource(R.drawable.china_telecom);
                        break;
                }
                flag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
