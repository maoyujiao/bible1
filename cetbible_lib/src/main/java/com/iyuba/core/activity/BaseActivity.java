package com.iyuba.core.activity;

import android.content.Intent;
import android.os.Bundle;

import com.iyuba.core.manager.AccountManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用基类
 */
public abstract class BaseActivity extends com.iyuba.base.BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 返回用于显示界面的id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化变量,包括intent携带的数据和activity内的变量
     */
    protected abstract void initVariables();

    /**
     * 加载layout布局,初始化控件,为事件挂上事件的方法
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 调用mobileAPI
     */
    protected abstract void loadData();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 判断用户有没有登录的方法,没有登录去登录呀
     */
    public boolean checkUserLoginAndLogin() {
        // 用户是否登录
        boolean isLogIn = AccountManager.Instace(getApplicationContext())
                .checkUserLogin();
        if (isLogIn) {
            return true;
        } else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), Login.class);
            startActivity(intent);
            return false;
        }
    }

    /**
     * 判断用户有没有登录的方法
     */
    public boolean isUserLogin() {
        // 用户是否登录
        boolean isLogIn = AccountManager.Instace(getApplicationContext()).checkUserLogin();
        return isLogIn;
    }

}
