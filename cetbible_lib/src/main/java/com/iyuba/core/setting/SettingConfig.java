package com.iyuba.core.setting;

/**
 * 属性设置服务
 *
 * @author 陈彤
 * @version 1.0
 * 可通用 可增添属性
 */

import com.iyuba.configation.ConfigManager;

public class SettingConfig {

    private final static String LIGHT_TAG = "light";
    private final static String PUSH_TAG = "push";
    private final static String NIGHT_TAG = "night";
    private final static String HIGH_SPEED_LIT_TAG = "highspeed";
    private final static String SYNCHO_TAG = "syncho";
    private final static String AUTO_LOGIN_TAG = "autoLogin";
    private final static String AUTO_PLAY_TAG = "autoplay";
    private final static String AUTO_STOP_TAG = "autostop";
    private final static String AUTO_DOWNLOAD_TAG = "autoDownload";
    private volatile static SettingConfig instance;
    private boolean highspeed;
    private boolean syncho;
    private boolean autoLogin;
    private boolean autoPlay;
    private boolean autoStop;
    private boolean light;
    private boolean push;
    private boolean night;
    private boolean auto_download;

    private SettingConfig() {

    }

    public static SettingConfig Instance() {

        if (instance == null) {
            synchronized (SettingConfig.class) {
                if (instance == null) {
                    instance = new SettingConfig();
                }
            }

        }
        return instance;
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isLight() {
        try {
            light = ConfigManager.Instance().loadBoolean(LIGHT_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            light = true;
        }
        return light;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setLight(boolean light) {
        ConfigManager.Instance().putBoolean(LIGHT_TAG, light);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isPush() {
        try {
            push = ConfigManager.Instance().loadBoolean(PUSH_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            push = true;
        }
        return push;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setPush(boolean push) {
        ConfigManager.Instance().putBoolean(PUSH_TAG, push);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isNight() {
        try {
            night = ConfigManager.Instance().loadBoolean(NIGHT_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            night = true;
        }
        return night;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setNight(boolean night) {
        ConfigManager.Instance().putBoolean(NIGHT_TAG, night);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isDownload() {
        try {
            auto_download = ConfigManager.Instance().loadBoolean(
                    AUTO_DOWNLOAD_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            auto_download = true;
        }
        return auto_download;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setAutoDownload(boolean download) {
        ConfigManager.Instance().putBoolean(AUTO_DOWNLOAD_TAG, download);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isHighSpeed() {
        try {
            highspeed = ConfigManager.Instance()
                    .loadBoolean(HIGH_SPEED_LIT_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            highspeed = true;
        }
        return highspeed;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setHighSpeed(boolean screenLit) {
        ConfigManager.Instance().putBoolean(HIGH_SPEED_LIT_TAG, screenLit);
    }

    /**
     * 获取是否播放时同步
     *
     * @return
     */
    public boolean isSyncho() {
        try {
            syncho = ConfigManager.Instance().loadBoolean(SYNCHO_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            syncho = true;
        }
        return syncho;
    }

    /**
     * 设置屏幕常亮
     *
     * @param automaticDownload
     */
    public void setSyncho(boolean syncho) {
        ConfigManager.Instance().putBoolean(SYNCHO_TAG, syncho);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoLogin() {
        try {
            autoLogin = ConfigManager.Instance().loadBoolean(AUTO_LOGIN_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            autoLogin = false;
        }
        return autoLogin;
    }

    /**
     * 设置是否自动登录
     *
     * @param automaticDownload
     */
    public void setAutoLogin(boolean AutoLogin) {
        ConfigManager.Instance().putBoolean(AUTO_LOGIN_TAG, AutoLogin);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoPlay() {
        try {
            autoPlay = ConfigManager.Instance().loadBoolean(AUTO_PLAY_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            autoPlay = false;
        }
        return autoPlay;
    }

    /**
     * 设置是否自动登录
     *
     * @param automaticDownload
     */
    public void setAutoPlay(boolean AutoPlay) {
        ConfigManager.Instance().putBoolean(AUTO_PLAY_TAG, AutoPlay);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoStop() {
        try {
            autoStop = ConfigManager.Instance().loadBoolean(AUTO_STOP_TAG);
        } catch (Exception e) {

            e.printStackTrace();
            autoStop = false;
        }
        return autoStop;
    }

    /**
     * 设置是否自动登录
     *
     * @param automaticDownload
     */
    public void setAutoStop(boolean AutoStop) {
        ConfigManager.Instance().putBoolean(AUTO_STOP_TAG, AutoStop);
    }

}
