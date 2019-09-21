package com.iyuba.core.widget.subtitle;

/**
 * 文章内容
 *
 * @author ct
 */
public class Subtitle {

    public static final int LANG_EN = 0;
    public static final int LANG_CH = 1;

    public long millisecond; // 毫秒
    public double pointInTime; // 时间点
    public int language = LANG_EN; // 字幕语言
    public int paragraph; // 段落
    public String content; // 内容
    public String english;
}
