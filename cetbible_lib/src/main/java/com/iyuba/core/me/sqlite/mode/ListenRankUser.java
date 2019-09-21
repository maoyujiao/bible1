package com.iyuba.core.me.sqlite.mode;

import java.io.Serializable;

/**
 * Created by 15730 on 2018/4/17.
 */

public class ListenRankUser implements Serializable {
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalWord() {
        return totalWord;
    }

    public void setTotalWord(int totalWord) {
        this.totalWord = totalWord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTotalEssay() {
        return totalEssay;
    }

    public void setTotalEssay(int totalEssay) {
        this.totalEssay = totalEssay;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    private int uid;
    private int totalTime;
    private int totalWord;
    private String name;
    private int ranking;
    private int sort;
    private int totalEssay;
    private String imgSrc;

}
