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
public class Write implements Serializable {
    public String num;
    public String index;
    public String name;
    public String text;
    public String comment;
    public String question;
    public String title;
    public String cateid;
    public String catename;
    public String image;
}
