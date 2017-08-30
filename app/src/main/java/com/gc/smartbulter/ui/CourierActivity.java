package com.gc.smartbulter.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.smartbulter.R;
import com.gc.smartbulter.adapter.CourierAdapter;
import com.gc.smartbulter.entity.CourierData;
import com.gc.smartbulter.utils.GsonUtils;
import com.gc.smartbulter.utils.StaticClass;
import com.gc.smartbulter.utils.UtilTools;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：CourierActivity
 * 创建者  ：GC
 * 创建时间：2017/8/12 11:17
 * 描述    ：快的查寻
 */
public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    private RecyclerView mRecyclerView;
    private List<CourierData> courierDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        
        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_get_courier = (Button) findViewById(R.id.btn_get_courier);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        btn_get_courier.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_courier:
                //获取输入框内容
                //判断是否为空
                //拿到数据源请求json
                // 解析json
                //RecyclerView适配器
                //实体类，item布局
                //设置数据，显示效果

                //获取内容
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                //拼接URL
                String url = "http://v.juhe.cn/exp/index?key="+ StaticClass.COURIER_KEY+"&com="+name+"&no="+number;
                //判断是否为空
                if(!TextUtils.isEmpty(name) & !TextUtils.isEmpty(number)){
                    //拿到数据，请求json
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
//                            UtilTools.showShrotToast(getApplicationContext(),t);
                            //解析json
                            parsingJson(t);
                            CourierAdapter courierAdapter = new CourierAdapter(getApplicationContext(), courierDatas);
                            mRecyclerView.setAdapter(courierAdapter);
                        }
                    });
                }else {
                    UtilTools.showShrotToast(getApplicationContext(),"输入框不能为空");
                }

                break;
        }
    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String jsonArray = String.valueOf(jsonResult.getJSONArray("list"));

            courierDatas = GsonUtils.jsonToArrayList(jsonArray, CourierData.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
