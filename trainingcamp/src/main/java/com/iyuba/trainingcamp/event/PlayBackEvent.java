package com.iyuba.trainingcamp.event;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.event
 * @class describe
 * @time 2018/11/20 13:46
 * @change
 * @chang time
 * @class describe
 */
public class PlayBackEvent {

    public int getPlayPercent() {
        return PlayPercent;
    }

    public void setPlayPercent(int playPercent) {
        PlayPercent = playPercent;
    }

    int PlayPercent;

    public PlayBackEvent(int playPercent) {
        PlayPercent = playPercent;
    }
}
