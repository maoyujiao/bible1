/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.sqlite.mode;

import java.io.Serializable;

/**
 * 通用新闻
 *
 * @author 陈彤
 */
public class CommonNews implements Serializable {
    public int id;
    public String title;
    public String content;
    public String time;
    public String picUrl;
    public String musicUrl;
}
