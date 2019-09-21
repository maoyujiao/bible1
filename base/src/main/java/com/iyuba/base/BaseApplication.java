package com.iyuba.base;

import android.app.Application;

/**
 * BaseApplication
 *
 * @author wayne
 * @date 2017/11/18
 */
public class BaseApplication extends Application {
    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static BaseApplication getInstance() {
        return application;
    }

    public static void setApplication(BaseApplication application) {
        BaseApplication.application = application;
    }
}
