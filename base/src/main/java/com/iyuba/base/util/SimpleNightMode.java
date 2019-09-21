package com.iyuba.base.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

import com.iyuba.base.R;

/**
 * SimpleNightMode
 *
 * @author wayne
 * @date 2017/12/8
 */
public class SimpleNightMode {
    private boolean isNight = false;
    private WindowManager mWindowManager;
    private View mNightView;
    private Context mContext;

    public SimpleNightMode(Activity context) {
        this.mContext = context;
        mWindowManager = context.getWindowManager();
    }

    public boolean isNightMode() {
        try {
            return (boolean) SP.get(mContext, "sp_night_mode", false);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void open() {
        if (isNight) {
            return;
        }
        isNight = true;

        try {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            mNightView = new View(mContext);
            mNightView.setBackgroundColor(mContext.getResources().getColor(R.color.base_night_float_color));
            mWindowManager.addView(mNightView, lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (!isNight) {
            return;
        }
        L.e("night close");
        try {
            isNight = false;
            mWindowManager.removeViewImmediate(mNightView);
            mContext = null;
            mNightView = null;
            mWindowManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        if (isNightMode()) {
            open();
        } else {
            close();
        }
    }

    public void setNightMode(boolean value) {
        try {
            SP.put(mContext, "sp_night_mode", value);
            onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
