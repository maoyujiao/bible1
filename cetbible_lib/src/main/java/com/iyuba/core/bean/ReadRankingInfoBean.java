package com.iyuba.core.bean;

import com.iyuba.core.me.sqlite.mode.ReadRankUser;

import java.util.List;

/**
 * Created by diaojw on 2018/4/17.
 */

public class ReadRankingInfoBean {

    /**
     * mywpm : 0
     * result : 10
     * myimgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
     * myid : 53991172
     * myranking : 0
     * data : [{"uid":368494,"wpm":296,"name":"Alex yunfeng","cnt":36,"words":11893,"ranking":1,"sort":1,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"uid":5439054,"wpm":54,"name":"Wzd369","cnt":22,"words":9973,"ranking":2,"sort":2,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"}]
     */

    private int mywpm;
    private int result;
    private String myimgSrc;
    private int myid;
    private int myranking;
    private List<ReadRankUser> data;

    public int getMywpm() {
        return mywpm;
    }

    public void setMywpm(int mywpm) {
        this.mywpm = mywpm;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMyimgSrc() {
        return myimgSrc;
    }

    public void setMyimgSrc(String myimgSrc) {
        this.myimgSrc = myimgSrc;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getMyranking() {
        return myranking;
    }

    public void setMyranking(int myranking) {
        this.myranking = myranking;
    }

    public List<ReadRankUser> getData() {
        return data;
    }

    public void setData(List<ReadRankUser> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 368494
         * wpm : 296
         * name : Alex yunfeng
         * cnt : 36
         * words : 11893
         * ranking : 1
         * sort : 1
         * imgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
         */

        private int uid;
        private int wpm;
        private String name;
        private int cnt;
        private int words;
        private int ranking;
        private int sort;
        private String imgSrc;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getWpm() {
            return wpm;
        }

        public void setWpm(int wpm) {
            this.wpm = wpm;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        public int getWords() {
            return words;
        }

        public void setWords(int words) {
            this.words = words;
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

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }
    }
}
