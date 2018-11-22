package com.iyuba.CET4bible.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;

/**
 * 按钮控件，在原生按钮基础上增加点击效果
 *
 * @author lijingwei
 */
public class Button extends android.widget.Button {

    private Animation mAnimationAlpha;

    public Button(Context context) {
        super(context);

    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getBackground().setAlpha(100);
                invalidate();
                // mAnimationAlpha=new AlphaAnimation(1.0f,0.5f);
                // mAnimationAlpha.setDuration(1000);
                // startAnimation(mAnimationAlpha);
                // mAnimationAlpha.setAnimationListener(new AnimationListener() {
                //
                // @Override
                // public void onAnimationStart(Animation animation) {
                //
                //
                // }
                //
                // @Override
                // public void onAnimationRepeat(Animation animation) {
                //
                //
                // }
                //
                // @Override
                // public void onAnimationEnd(Animation animation) {
                //
                // getBackground().setAlpha(100);
                // invalidate();
                // }
                // });

                break;
            case MotionEvent.ACTION_UP:
                getBackground().setAlpha(255);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

}
