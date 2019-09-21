package com.iyuba.core.sqlite.mode;

/**
 * 单词
 *
 * @author 陈彤
 */
public class Word {
    public String userid;
    public String key = ""; // 关键�?
    public String lang = "";
    public String audioUrl = ""; // 多媒体网络路�?
    public String pron = ""; // 音标
    public String def = ""; // 解释
    public String examples = ""; // 例句，多条以�?”分�?
    public String createDate = ""; // 创建时间
    public boolean isDelete = false;
    public String delete;
}
