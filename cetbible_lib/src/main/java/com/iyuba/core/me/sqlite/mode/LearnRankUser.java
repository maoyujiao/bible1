package com.iyuba.core.me.sqlite.mode;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */

public class LearnRankUser implements Serializable {

    private String uid = "";
    private String totalEssay = "";
    private String sort = "";
    private String imgSrc = "";
    private String name = "";
    private String totalWord = "";
    private String ranking = "";
    private String totalTime = "";

    public LearnRankUser() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTotalEssay() {
        return totalEssay;
    }

    public void setTotalEssay(String totalEssay) {
        this.totalEssay = totalEssay;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public String getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(String totalWord) {
        this.totalWord = totalWord;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "LearnRankUser{" +
                "uid='" + uid + '\'' +
                ", totalEssay='" + totalEssay + '\'' +
                ", sort='" + sort + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", name='" + name + '\'' +
                ", totalWord='" + totalWord + '\'' +
                ", ranking='" + ranking + '\'' +
                ", totalTime='" + totalTime + '\'' +
                '}';
    }
}
