package com.iyuba.core.widget;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.iyuba.core.listener.OnPlayStateChangedListener;

import java.io.IOException;

/**
 * 简化播放器
 *
 * @author 陈彤
 */
public class Player implements OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    public MediaPlayer mediaPlayer;
    private OnPlayStateChangedListener opscl;
    private String audioUrl;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(audioUrl);
                        new Thread() {

                            @Override
                            public void run() {

                                super.run();
                                mediaPlayer.prepareAsync();

                            }
                        }.start();

                    } catch (IllegalArgumentException e) {

                        if (opscl != null) {
                            opscl.playFaild();
                        }
                        e.printStackTrace();
                    } catch (IllegalStateException e) {

                        if (opscl != null) {
                            opscl.playFaild();
                        }
                        e.printStackTrace();
                    } catch (IOException e) {

                        if (opscl != null) {
                            opscl.playFaild();
                        }
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    public Player(Context context, OnPlayStateChangedListener opscl) {
        this.opscl = opscl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
        } catch (Exception e) {
        }
    }

    public void playUrl(final String videoUrl) {
        this.audioUrl = videoUrl;
        handler.sendEmptyMessage(1);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void reset() {
        mediaPlayer.reset();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false ;
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (opscl != null) {
            opscl.playCompletion();
        }
    }

    public String getTimes() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mediaPlayer != null) {
            int musicTime = mediaPlayer.getCurrentPosition() / 1000;

            String minu = String.valueOf(musicTime / 60);
            if (minu.length() == 1) {
                minu = "0" + minu;
            }
            String sec = String.valueOf(musicTime % 60);
            if (sec.length() == 1) {
                sec = "0" + sec;
            }

            timeBuffer.append(minu).append(":").append(sec);
        }
        return timeBuffer.toString();
    }

    public int getTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /**
     * 获取音频总长
     *
     * @return
     */
    public String getDurations() {
        StringBuffer timeBuffer = new StringBuffer("");
        if (mediaPlayer != null) {
            int musicTime = mediaPlayer.getDuration() / 1000;

            String minu = String.valueOf(musicTime / 60);
            if (minu.length() == 1) {
                minu = "0" + minu;
            }
            String sec = String.valueOf(musicTime % 60);
            if (sec.length() == 1) {
                sec = "0" + sec;
            }

            timeBuffer.append(minu).append(":").append(sec);
        }
        return timeBuffer.toString();
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        Log.d("hahahaha", what + ":::" + extra);
        return false;
    }
}
