package com.iyuba.core.me.sqlite.mode;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */

public class TestRankUser implements Serializable {

    private String uid = "";
    private String sort = "";
    private String totalTest = "";
    private String imgSrc = "";
    private String name = "";
    private String totalRight = "";
    private String ranking = "";

    public TestRankUser() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(String totalTest) {
        this.totalTest = totalTest;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getName() {
        if (TextUtils.isEmpty(name) || "null".equals(name)) {
            return uid + "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(String totalRight) {
        this.totalRight = totalRight;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        return "TestRankUser{" +
                "uid='" + uid + '\'' +
                ", sort='" + sort + '\'' +
                ", totalTest='" + totalTest + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", name='" + name + '\'' +
                ", totalRight='" + totalRight + '\'' +
                ", ranking='" + ranking + '\'' +
                '}';
    }
}
