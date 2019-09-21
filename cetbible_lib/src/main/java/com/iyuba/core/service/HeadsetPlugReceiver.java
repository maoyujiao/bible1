package com.iyuba.core.service;

/**
 * 耳机监听广播
 *
 * @author 陈彤
 * @version 1.0
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iyuba.configation.ConfigManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.widget.BackPlayer;

public class HeadsetPlugReceiver extends BroadcastReceiver {
    private BackPlayer vv = null;
    private boolean first = true;

    public HeadsetPlugReceiver() {
        vv = BackgroundManager.Instace().bindService
                .getPlayer();
    }

    public HeadsetPlugReceiver(BackPlayer player) {
        vv = player;
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.hasExtra("state")) {
            if (intent.getIntExtra("state", 0) == 1) {
                boolean isAutoPlay = ConfigManager.Instance().loadBoolean(
                        "autoplay");
                if (isAutoPlay && !vv.isPlaying()) {
                    vv.start();
                }
                first = false;
            } else if (intent.getIntExtra("state", 0) == 0) {
                if (!first) {
                    boolean isAutoStop = ConfigManager.Instance().loadBoolean(
                            "autostop");
                    if (isAutoStop && vv.isPlaying()) {
                        vv.pause();
                    }
                }
            }
        }
    }

}
