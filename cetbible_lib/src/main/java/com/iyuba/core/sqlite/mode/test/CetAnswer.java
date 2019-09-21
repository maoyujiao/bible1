/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.sqlite.mode.test;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class CetAnswer {
    public String id;
    public String question;
    public String a1;
    public String a2;
    public String a3;
    public String a4;
    public String rightAnswer;
    public String sound;
    public String qsound;
    public String flag;
    public String yourAnswer;

    @Override
    public String toString() {
        return "CetAnswer{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", a1='" + a1 + '\'' +
                ", a2='" + a2 + '\'' +
                ", a3='" + a3 + '\'' +
                ", a4='" + a4 + '\'' +
                ", rightAnswer='" + rightAnswer + '\'' +
                ", sound='" + sound + '\'' +
                ", qsound='" + qsound + '\'' +
                ", flag='" + flag + '\'' +
                ", yourAnswer='" + yourAnswer + '\'' +
                '}' + "\n";
    }
}
