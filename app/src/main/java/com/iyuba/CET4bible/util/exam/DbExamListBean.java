package com.iyuba.CET4bible.util.exam;

import com.google.gson.Gson;

/**
 * DbExanListBean
 *
 * @author wayne
 * @date 2018/1/19
 */
public class DbExamListBean {
    public String year;
    public String title;
    public String image;
    public String version;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
