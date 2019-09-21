package com.iyuba.CET4bible.widget.subtitle;

import com.iyuba.abilitytest.entity.SendEvaluateResponse;

import java.util.List;

/**
 * 文章内容
 *
 * @author ct
 */
public class Subtitle {
    public long millisecond; // 毫秒
    public int pointInTime; // 时间点
    public int paragraph; // 段落
    public String content; // 内容
    public String testTime; // 内容
    public boolean isHtml = false;
    public List<Integer> badList;
    public List<Integer> goodList;
    public int Number;
    public int NumberIndex;
    public String record_url;
    public int shuoshuoid;
    public int readScore;
    public int endTiming;
    public boolean isRead;
    public List<SendEvaluateResponse.DataBean.WordsBean> mDataBean;
}
