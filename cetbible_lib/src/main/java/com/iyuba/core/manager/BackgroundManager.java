package com.iyuba.core.manager;

/**
 * 后台播放管理
 *
 * @author chentong
 * @version 1.0
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.service.Background;
import com.iyuba.core.service.Background.MyBinder;

public class BackgroundManager {
    public static Context mContext;
    private static BackgroundManager instance;
    public Background bindService;
    public ServiceConnection conn;

    private BackgroundManager() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                MyBinder binder = (MyBinder) service;
                bindService = binder.getService();
                bindService.init(mContext);
            }
        };
    }

    public static synchronized BackgroundManager Instace() {
        mContext = RuntimeManager.getContext();
        if (instance == null) {
            instance = new BackgroundManager();
        }
        return instance;
    }
}
