package com.iyuba.core.discover.listener;

public interface OnPlayStateChangedListener {
    void playSuccess();

    void setPlayTime(String currTime, String allTime);

    void playFaild();

    void playCompletion();
}
