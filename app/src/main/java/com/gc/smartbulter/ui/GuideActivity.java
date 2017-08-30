package com.gc.smartbulter.ui;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gc.smartbulter.MainActivity;
import com.gc.smartbulter.R;
import com.gc.smartbulter.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名  ：SmartBulter
 * 包名    ：com.gc.smartbulter.ui
 * 文件名  ：GuideActivity
 * 创建者  ：GC
 * 创建时间：2017/8/8 10:01
 * 描述    ：引导页
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1,view2,view3;
    //小圆点
    private ImageView point1,point2,point3;
    //跳过
    private ImageView iv_back;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    //初始化View
    private void initView() {

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        //设置默认图片
        setPointImg(true,false,false);


        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        view1 = View.inflate(this,R.layout.pager_item_one,null);
        view2 = View.inflate(this,R.layout.pager_item_two,null);
        view3 = View.inflate(this,R.layout.pager_item_three,null);

        view3.findViewById(R.id.btn_start).setOnClickListener(this);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());

        //监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //page切换回调
            @Override
            public void onPageSelected(int position) {
                L.i("position:"+position);
                switch (position){
                    case 0:
                        setPointImg(true,false,false);
                        //设置显示
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false,true,false);
                        //设置显示
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false,false,true);
                        //设置隐藏
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.btn_start:
                 startActivity(new Intent(this, LoginActivity.class));
                 finish();
                 break;
             case R.id.iv_back:
                 startActivity(new Intent(this, LoginActivity.class));
                 finish();
                 break;
         }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //滑动
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }

        //删除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mList.get(position));
            //super.destroyItem(container, position, object);
        }
    }
    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1,boolean isCheck2,boolean isCheck3){

        if(isCheck1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else {
            point1.setBackgroundResource(R.drawable.point_off);
        }

        if(isCheck2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else {
            point2.setBackgroundResource(R.drawable.point_off);
        }

        if(isCheck3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else {
            point3.setBackgroundResource(R.drawable.point_off);
        }

    }
}
