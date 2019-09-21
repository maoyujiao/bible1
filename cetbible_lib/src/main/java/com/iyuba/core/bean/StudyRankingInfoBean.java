package com.iyuba.core.bean;

import com.iyuba.core.me.sqlite.mode.LearnRankUser;

import java.util.List;

public class StudyRankingInfoBean {



    private int result;
    private String myimgSrc;
    private int myid;
    private int myranking;
    private int totalTime;
    private int totalWord;
    private int totalEssay;
    private String message;
    private List<LearnRankUser> data;

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

    public int getTotalEssay() {
        return totalEssay;
    }

    public void setTotalEssay(int totalEssay) {
        this.totalEssay = totalEssay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LearnRankUser> getData() {
        return data;
    }

    public void setData(List<LearnRankUser> data) {
        this.data = data;
    }

//    public static class DataBean {
//        /**
//         * uid : 4358935
//         * totalTime : 36694
//         * totalWord : 2089
//         * name : gk56055
//         * ranking : 1
//         * sort : 1
//         * totalEssay : 142
//         * imgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
//         */
//
//        private int uid;
//        private int totalTime;
//        private int totalWord;
//        private String name;
//        private int ranking;
//        private int sort;
//        private int totalEssay;
//        private String imgSrc;
//
//        public int getUid() {
//            return uid;
//        }
//
//        public void setUid(int uid) {
//            this.uid = uid;
//        }
//
//        public int getTotalTime() {
//            return totalTime;
//        }
//
//        public void setTotalTime(int totalTime) {
//            this.totalTime = totalTime;
//        }
//
//        public int getTotalWord() {
//            return totalWord;
//        }
//
//        public void setTotalWord(int totalWord) {
//            this.totalWord = totalWord;
//        }
//
//        public String getName() {
//            if (TextUtils.isEmpty(name) || "null".equals(name)) {
//                return uid + "";
//            }
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getRanking() {
//            return ranking;
//        }
//
//        public void setRanking(int ranking) {
//            this.ranking = ranking;
//        }
//
//        public int getSort() {
//            return sort;
//        }
//
//        public void setSort(int sort) {
//            this.sort = sort;
//        }
//
//        public int getTotalEssay() {
//            return totalEssay;
//        }
//
//        public void setTotalEssay(int totalEssay) {
//            this.totalEssay = totalEssay;
//        }
//
//        public String getImgSrc() {
//            return imgSrc;
//        }
//
//        public void setImgSrc(String imgSrc) {
//            this.imgSrc = imgSrc;
//        }
//    }
}
