package com.iyuba.core.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;

import com.iyuba.biblelib.R;
import com.iyuba.core.listener.OnActivityGroupKeyDown;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 处理ActivityGroup控制的基类
 *
 * @author chentong
 * @version 1.1
 */

public class Base extends ListActivity {
    private Context mContext;
    private OnActivityGroupKeyDown onActivityGroupKeyDown;
    private boolean isExit = false;// 是否点过退出
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
    }

    public void setOnActivityGroupKeyDown(
            OnActivityGroupKeyDown onActivityGroupKeyDown) {
        this.onActivityGroupKeyDown = onActivityGroupKeyDown;
    }

    /**
     * 两次点击处理事件 点击间隔设置为1.5s
     *
     * @param
     * @return
     */
    public void doExitInOneSecond() {
        isExit = true;
        HandlerThread thread = new HandlerThread("doTask");
        thread.start();
        new Handler(thread.getLooper()).postDelayed(task, 1500);// 1.5秒内再点有效
    }

    public void onKeyBackEvent(KeyEvent event) {
        if (onActivityGroupKeyDown != null) {
            if (onActivityGroupKeyDown.onSubActivityKeyDown(event.getKeyCode(),
                    event)) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    pressAgainExit();
                }
            }
        } else {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                pressAgainExit();
            }
        }
    }

    /**
     * 点击后的处理
     *
     * @param isexit : true（1.后台播放返回桌面2.未播放直接退出）false进入doExitInOneSecond
     * @return
     */
    private void pressAgainExit() {
        if (isExit) {
            if (BackgroundManager.Instace().bindService != null
                    && BackgroundManager.Instace().bindService.getPlayer()
                    .isPlaying()) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addCategory(Intent.CATEGORY_HOME);
                startActivity(i);
            } else {
                Intent intent = new Intent();
                intent.setAction("gotosleep");
                sendBroadcast(intent);
            }
        } else {
            if (BackgroundManager.Instace().bindService != null
                    && BackgroundManager.Instace().bindService.getPlayer()
                    .isPlaying()) {
                CustomToast.showToast(getApplicationContext(),
                        R.string.alert_home);
            } else {
                CustomToast.showToast(getApplicationContext(),
                        R.string.alert_press);
            }
            doExitInOneSecond();
        }
    }

    public void onKeyDellEvent(KeyEvent event) {
        onActivityGroupKeyDown.onSubActivityKeyDown(event.getKeyCode(), event);
    }
}
