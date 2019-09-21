package com.iyuba.core.bean;

import com.iyuba.core.me.sqlite.mode.TestRankUser;

import java.util.List;

public class TestRankingInfoBean {

    /**
     * totalRight : 0
     * result : 10
     * totalTest : 0
     * myimgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
     * myid : 50000038
     * myranking : 0
     * data : [{"totalRight":130,"uid":4770294,"totalTest":200,"name":"461278117","ranking":1,"sort":1,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":119,"uid":4255486,"totalTest":119,"name":"papakele","ranking":2,"sort":2,"imgSrc":"http://static1.iyuba.cn/uc_server/head/2017/3/3/11/59/10/4c1c9af7-d5d6-4917-bed4-5b40fca8d98d-m.jpg"},{"totalRight":118,"uid":4757817,"totalTest":200,"name":"mily222","ranking":3,"sort":3,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":117,"uid":50059944,"totalTest":150,"ranking":4,"sort":4,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":117,"uid":4341864,"totalTest":120,"name":"Cherise二次方","ranking":5,"sort":5,"imgSrc":"http://static1.iyuba.cn/uc_server/head/2017/4/2/12/29/1/44aeef89-7386-4066-bbd3-a08613b3129c-m.jpg"},{"totalRight":114,"uid":50058837,"totalTest":128,"ranking":6,"sort":6,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":111,"uid":50059043,"totalTest":150,"ranking":7,"sort":7,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":102,"uid":4410173,"totalTest":102,"name":"雨爱*","ranking":8,"sort":8,"imgSrc":"http://static1.iyuba.cn/uc_server/head/2017/4/19/6/24/45/431b3864-e525-4c74-9aa3-cc6dd4bef8e4-m.jpg"},{"totalRight":100,"uid":50058555,"totalTest":125,"ranking":9,"sort":9,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"},{"totalRight":96,"uid":4771695,"totalTest":201,"name":"醉棒。。","ranking":10,"sort":10,"imgSrc":"http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg"}]
     * message : Success
     */

    private int totalRight;
    private int result;
    private int totalTest;
    private String myimgSrc;
    private int myid;
    private int myranking;
    private String message;
    private List<TestRankUser> data;

    public int getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(int totalRight) {
        this.totalRight = totalRight;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TestRankUser> getData() {
        return data;
    }

    public void setData(List<TestRankUser> data) {
        this.data = data;
    }

//    public static class DataBean {
//        /**
//         * totalRight : 130
//         * uid : 4770294
//         * totalTest : 200
//         * name : 461278117
//         * ranking : 1
//         * sort : 1
//         * imgSrc : http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg
//         */
//
//        private int totalRight;
//        private int uid;
//        private int totalTest;
//        private String name;
//        private int ranking;
//        private int sort;
//        private String imgSrc;
//
//        public int getTotalRight() {
//            return totalRight;
//        }
//
//        public void setTotalRight(int totalRight) {
//            this.totalRight = totalRight;
//        }
//
//        public int getUid() {
//            return uid;
//        }
//
//        public void setUid(int uid) {
//            this.uid = uid;
//        }
//
//        public int getTotalTest() {
//            return totalTest;
//        }
//
//        public void setTotalTest(int totalTest) {
//            this.totalTest = totalTest;
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
//        public String getImgSrc() {
//            return imgSrc;
//        }
//
//        public void setImgSrc(String imgSrc) {
//            this.imgSrc = imgSrc;
//        }
//    }
}
