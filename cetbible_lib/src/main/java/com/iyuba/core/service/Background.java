package com.iyuba.core.service;
/**
 * 后台播放服务
 *
 * @author 陈彤
 * @version 1.1
 * 更新内容 AudioManager管理与其他 播放器播放冲突问题
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.iyuba.core.widget.BackPlayer;

public class Background extends Service {
    private AudioManager mAm;
    private BackPlayer vv;
    private int voaid;
    private BroadcastReceiver mBroadcastReceiver;
    private MyBinder myBinder = new MyBinder();
    private MyOnAudioFocusChangeListener mListener;
    private boolean mPausedByTransientLossOfFocus;

    @Override
    public IBinder onBind(Intent intent) {

        registerBoradcastReceiver();
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        unRegisterBoradcastReceiver();
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAm = (AudioManager) getApplicationContext().getSystemService(
                Context.AUDIO_SERVICE);
        mListener = new MyOnAudioFocusChangeListener();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBoradcastReceiver();
        mAm.abandonAudioFocus(mListener);
        vv.stopPlayback();
    }

    public void init(Context mContext) {
        vv = new BackPlayer(mContext);
        mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    public BackPlayer getPlayer() {
        return vv;
    }

    public int getTag() {
        return voaid;
    }

    public void setTag(int id) {
        voaid = id;
    }

    public void next() {
    }

    public void before() {
    }

    public void registerBoradcastReceiver() {
        PhoneStateListener p = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if (vv != null && vv.isPlaying()) {
                            vv.pause();
                        }
                        break;
                    default:
                        break;
                }
            }

        };
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(p, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void unRegisterBoradcastReceiver() {

        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    public class MyBinder extends Binder {

        public Background getService() {
            return Background.this;
        }
    }

    private class MyOnAudioFocusChangeListener implements
            OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    if (vv.isPlaying()) {
                        vv.pause();// 因为会长时间失去，所以直接暂停
                    }
                    mPausedByTransientLossOfFocus = false;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (vv.isPlaying()) {
                        // 短暂失去焦点，先暂停。同时将标志位置成重新获得焦点后就开始播放
                        vv.pause();
                        mPausedByTransientLossOfFocus = true;
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    // 重新获得焦点，且符合播放条件，开始播放
                    if (!vv.isPlaying() && mPausedByTransientLossOfFocus) {
                        mPausedByTransientLossOfFocus = false;
                        vv.start();
                    }
                    break;
            }
        }
    }
}
