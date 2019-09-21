package com.iyuba.abilitytest.listener;


public interface OnPlayStateChangedListener {
	void playSuccess();
	void setPlayTime(String currTime, String allTime);
	void playFaild();
	void playCompletion();
	void playPause();
	void playResume();
	void playStop();
}
