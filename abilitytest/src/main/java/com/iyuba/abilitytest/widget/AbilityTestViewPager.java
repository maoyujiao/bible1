package com.iyuba.abilitytest.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author liuzhenli  重写ViewPager
 */
public class AbilityTestViewPager extends ViewPager {

    private boolean isScrollable = true;//false 禁止其左右滑动

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    public AbilityTestViewPager(Context context) {
        super(context);
    }

    public AbilityTestViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return isScrollable&&super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent even) {
        if (!isScrollable)
            return false;

        if (even.getAction() == MotionEvent.ACTION_DOWN) {
            preX = (int) even.getX();
        } else {
            if (Math.abs((int) even.getX() - preX) > 10) {
                return true;
            } else {
                preX = (int) even.getX();
            }
        }


        return super.onInterceptTouchEvent(even);
    }

    private int preX = 0;


}
