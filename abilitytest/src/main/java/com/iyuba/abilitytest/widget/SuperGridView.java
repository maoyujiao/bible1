package com.iyuba.abilitytest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义gridview，解决ListView中嵌套gridview显示不正常的问题（1行半）
 *
 * @author 来自博客:http://yxwang0615.iteye.com/blog/1739187
 *         Created by Administrator on 2016/9/24.
 */
public class SuperGridView extends GridView {

    public SuperGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperGridView(Context context) {
        super(context);
    }

    public SuperGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}