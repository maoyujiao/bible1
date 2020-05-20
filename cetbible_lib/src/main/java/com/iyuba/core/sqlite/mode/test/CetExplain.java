/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.sqlite.mode.test;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class CetExplain implements Comparable<Object> {
    public String id;
    public String keys;
    public String explain;
    public String knowledge;


    @Override
    public int compareTo(@NonNull Object o) {
        return Integer.parseInt(((CetExplain)this).id) > Integer.parseInt(((CetExplain)o).id) ? 1:-1;
    }
}
