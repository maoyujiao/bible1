package com.iyuba.trainingcamp.widget;

import android.media.MediaPlayer;
import android.provider.MediaStore;

import com.iflytek.thirdparty.P;
import com.iyuba.trainingcamp.R;

import java.io.IOException;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.widget
 * @class describe
 * @time 2018/8/16 16:50
 * @change
 * @chang time
 * @class describe
 */
public class GoldMediaPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer player;

    public MediaPlayer getPlayer() {
        return player;
    }

    public GoldMediaPlayer(){
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
    }

    public void stop(){
        if (null != player && player.isPlaying()){
            player.stop();
        } else{
            player = null;
        }
    }

    public void start(String url){
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }

    public void seekTo(int sec){
        player.seekTo(sec);
        player.start();
    }

    public void stopRestart(String url){
        player.pause();
        player.stop();
        try {
            player.reset();
            player.setDataSource(url);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();

            }
        });

    }

    public void start(){
        player.start();
    }

    public void pause(){
        player.pause();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
