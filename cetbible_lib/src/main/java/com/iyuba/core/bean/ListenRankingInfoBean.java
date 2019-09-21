package com.iyuba.core.bean;

import com.iyuba.core.me.sqlite.mode.ListenRankUser;

import java.util.List;

/**
 * Created by 15730 on 2018/4/17.
 */

public class ListenRankingInfoBean {


    /**
     * result : 10
     * myimgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
     * myid : 53991172
     * myranking : 0
     * data : [{"uid":50003072,"totalTime":86024,"totalWord":178322,"name":"","ranking":1,"sort":1,"totalEssay":370,"imgSrc":""}]
     */

    private int result;
    private String myimgSrc;
    private int myid;
    private int myranking;
    private List<ListenRankUser> data;

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

    public List<ListenRankUser> getData() {
        return data;
    }

    public void setData(List<ListenRankUser> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 50003072
         * totalTime : 86024
         * totalWord : 178322
         * name :
         * ranking : 1
         * sort : 1
         * totalEssay : 370
         * imgSrc :
         */

        private int uid;
        private int totalTime;
        private int totalWord;
        private String name;
        private int ranking;
        private int sort;
        private int totalEssay;
        private String imgSrc;

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
    }
}
