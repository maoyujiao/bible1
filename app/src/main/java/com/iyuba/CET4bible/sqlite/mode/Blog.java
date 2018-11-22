/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.sqlite.mode;

import java.io.Serializable;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Blog implements Serializable {
    public int id;
    public String title;
    public String title_en;
    public String source;
    public String author;
    public String tag;
    public String desccn;
    public String url;
    public String createtime;
    public String essay;
    public String category;
    public String readcount;
}
