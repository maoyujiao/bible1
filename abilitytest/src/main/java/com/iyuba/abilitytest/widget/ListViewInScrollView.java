package com.iyuba.abilitytest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 该ListView用于嵌套在ListView里面只显示一条或者两条的问题
 *
 * @author LiuZhenli on 2016/12/13 11:21 Email: 848808263@qq.com
 * @version 1.0.0
 */

public class ListViewInScrollView extends ListView {


    public ListViewInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewInScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
