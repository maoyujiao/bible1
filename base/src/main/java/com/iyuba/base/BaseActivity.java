package com.iyuba.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iyuba.base.util.L;
import com.iyuba.base.util.SimpleNightMode;
import com.iyuba.base.util.T;
import com.jaeger.library.StatusBarUtil;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Activity mContext;
    protected SwipeBackHelper swipeBackHelper;
    protected SimpleNightMode simpleNightMode;

    protected ProgressDialog dialog ;

    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);//循环滚动
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);//false不能取消显示，true可以取消显示

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        initDialog();
        if (isSwipeBackEnable()) {
            swipeBackHelper = new SwipeBackHelper(this);
        }
        super.onCreate(savedInstanceState);
        simpleNightMode = new SimpleNightMode( this);
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    public void showShort(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showShort(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void showLong(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showLong(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void e(String msg) {
        L.e(msg);
    }

    public void w(String msg) {
        L.w(msg);
    }

    public void d(String msg) {
        L.d(msg);
    }

    protected boolean isSwipeBackEnable() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (swipeBackHelper != null && isSwipeBackEnable()) {
            swipeBackHelper.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleNightMode.onResume();
    }

    @Override
    protected void onDestroy() {
        simpleNightMode.close();
        super.onDestroy();
    }

    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColor(this, color);
    }

    protected void setSwipeBStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColorForSwipeBack(this, color);
    }

}
