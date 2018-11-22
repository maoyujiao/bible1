package com.iyuba.CET4bible.sqlite.mode;

import java.io.Serializable;

/**
 * FillInBlankBean
 *
 * @author wayne
 * @date 2017/12/20
 */
public class FillInBlankBean implements Serializable {
    public String year;
    public int index;
    public String title;
    public String original;
    public String word;
    public String explanation;
    public String chinese;
    public boolean isSelected = false;
}
