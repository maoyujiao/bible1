package com.iyuba.CET4bible.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.iyuba.CET4bible.widget.CustomDragLayout.Status;

/**
 * 自定义的ViewGroup, 处理touch事件
 *
 * @author poplar
 */
public class MyRelativeLayout extends RelativeLayout {

    private CustomDragLayout mDragLayout;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 1. 获取当前的状态

    public void setDragLayout(CustomDragLayout mDragLayout) {
        this.mDragLayout = mDragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragLayout.getStatus() == Status.Close) {
            // 如果当前状态是Close状态

            Log.e("My onInterceptTouchEvent", "调用父类");
            return super.onInterceptTouchEvent(ev);
        } else {
            Log.e("My onInterceptTouchEvent", "拦截时间");
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragLayout.getStatus() == Status.Close) {
            return super.onTouchEvent(event);
        } else {

            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP) {
                mDragLayout.close();
            }

            return true;
        }
    }

}
