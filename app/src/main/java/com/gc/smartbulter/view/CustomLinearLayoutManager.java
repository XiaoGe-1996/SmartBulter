package com.gc.smartbulter.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 项目名  ：HomeworkRecyclerView
 * 包名    ：com.gc.homeworkrecyclerview
 * 文件名  ：CustomLinearLayoutManager
 * 创建者  ：GC
 * 创建时间：2017/8/19 11:32
 * 描述    ：禁止RecyclerView滑动
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
