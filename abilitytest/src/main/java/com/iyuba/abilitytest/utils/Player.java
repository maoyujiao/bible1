package com.iyuba.abilitytest.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

import com.iyuba.abilitytest.listener.OnPlayStateChangedListener;
import com.iyuba.configation.Constant;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private Context mContext;
    private Timer mTimer = new Timer();
    private OnPlayStateChangedListener opscl;
    private String audioUrl;

    public Player(Context context, OnPlayStateChangedListener opscl) {
        this.mContext = context;
        this.opscl = opscl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player(Context context, OnPlayStateChangedListener opscl,
                  SeekBar skbProgress) {
        this.skbProgress = skbProgress;
        skbProgress.setEnabled(false);
        this.mContext = context;
        this.opscl = opscl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                if (mediaPlayer == null)
                    return;
                if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                    handler.sendEmptyMessage(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static final int SEEK_NEXT = 5000;

    public void play() {
        mediaPlayer.start();
        if (opscl != null) {
            opscl.playResume();
        }

    }

    public void playUrl(final String videoUrl) {
        this.audioUrl = videoUrl;
        handler.sendEmptyMessage(1);
    }

    public void playAnother(final String videoUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(videoUrl);
                mediaPlayer.prepare();
                play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        mediaPlayer.pause();
        if (opscl != null) {
            opscl.playPause();
        }

    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (skbProgress != null) {
            mTimer.cancel();
            skbProgress.setEnabled(false);
            handler.removeMessages(3);
        }
        if (opscl != null) {
            opscl.playStop();
        }
    }


    public void nextSpeed() {
        int seekNextTime = getCurrentPosition() + Constant.SEEK_NEXT;
        if (seekNextTime > getDur()) {
            seekNextTime = getDur();
        }
        seekTo(seekNextTime);

    }

    public void preSpeed() {
        int seekPreTime = getCurrentPosition() + Constant.SEEK_PRE;
        if (seekPreTime < 0) {
            seekPreTime = 0;
        }
        seekTo(seekPreTime);
    }

    public boolean isPlaying() {
        if (mediaPlayer == null) {
            return false;
        }
        try {
            return mediaPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getDur() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int msec) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(msec);
        }
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            play();
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        if (opscl != null) {
            opscl.playSuccess();
        }
        // Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (skbProgress != null) {
            skbProgress.setProgress(0);
        }
        if (opscl != null) {
            opscl.playCompletion();
        }
        mediaPlayer.pause();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        if (skbProgress != null) {
            skbProgress.setSecondaryProgress(bufferingProgress);
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        if (mediaPlayer.isPlaying()) {
                            if (opscl != null) {
                                opscl.playFaild();
                            }
                        }
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(audioUrl);
                        mediaPlayer.prepare();
                        new Thread() {

                            @Override
                            public void run() {

                                super.run();
                                try {
                                    if (opscl != null) {
                                        opscl.setPlayTime(getAudioCurrTime(),
                                                getAudioAllTime());
                                    }
                                    if (skbProgress != null) {
                                        handler.sendEmptyMessage(2);
                                    }
                                } catch (IllegalStateException e) {

                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    } catch (Exception e) {

                        if (opscl != null) {
                            opscl.playFaild();
                        }
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    mTimer.cancel();
                    mTimer = new Timer();
                    if (mTimerTask != null) {
                        mTimerTask.cancel();
                    }
                    mTimerTask = new TimerTask() {

                        @Override
                        public void run() {
                            // Log.e("dingshiqi", "!!!!!!!!!!!");
                            try {
                                if (mediaPlayer == null)
                                    return;
                                if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                                    handler.sendEmptyMessage(3);
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }

                    };
                    mTimer.schedule(mTimerTask, 0, 1000);
                    skbProgress.setEnabled(true);
                    break;

                case 3:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int position = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        if (duration > 0) {
                            long pos = skbProgress.getMax() * position / duration;
                            skbProgress.setProgress((int) pos);
                        }
                        if (opscl != null) {
                            opscl.setPlayTime(getAudioCurrTime(), getAudioAllTime());
                        }
                    }
                    break;
            }
        }
    };

    /**
     * 获取音频总长
     *
     * @return
     */
    public String getAudioAllTime() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mediaPlayer != null) {
            int musicTime = mediaPlayer.getDuration() / 1000;// �?
            String minit = "00";// �?
            String second = "00";// �?
            if ((musicTime / 60) < 10)// �?
            {
                minit = "0" + String.valueOf(musicTime / 60);
                // timeBuffer.append("0").append(musicTime / 60).append(":")
                // .append(musicTime % 60);
            } else {
                minit = String.valueOf(musicTime / 60);
            }
            if ((musicTime % 60) < 10)// �?
            {
                second = "0" + String.valueOf(musicTime % 60);
            } else {
                second = String.valueOf(musicTime % 60);
            }
            timeBuffer.append(minit).append(":").append(second);

        }
        return timeBuffer.toString();
    }

    /**
     * 获取音频当前播放进度时间
     *
     * @return
     */
    public String getAudioCurrTime() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mediaPlayer != null) {
            int musicTime = mediaPlayer.getCurrentPosition() / 1000;
            String minit = "00";// �?
            String second = "00";// �?
            if ((musicTime / 60) < 10)// �?
            {
                minit = "0" + String.valueOf(musicTime / 60);
                // timeBuffer.append("0").append(musicTime / 60).append(":")
                // .append(musicTime % 60);
            } else {
                minit = String.valueOf(musicTime / 60);
            }
            if ((musicTime % 60) < 10)// �?
            {
                second = "0" + String.valueOf(musicTime % 60);
            } else {
                second = String.valueOf(musicTime % 60);
            }
            timeBuffer.append(minit).append(":").append(second);
        }
        return timeBuffer.toString();
    }


}
