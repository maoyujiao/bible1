package com.iyuba.CET4bible.fragment;

import java.util.List;

public class MicroClassListBean {

    /**
     * result : 1
     * uid : 0
     * firstPage : 1
     * lastPage : 3
     * data : [{"price":"320","name":"日语N1听力新题型","id":1,"pic":"1-1","classNum":5,"viewCount":40699,"ownerid":"1","realprice":"400","desc":"万能听力法宝有没有？方法决定分数。日语QQ群：363376722"},{"price":"480","name":"日语N1语法","id":9,"pic":"1-9","classNum":7,"viewCount":24386,"ownerid":"1","realprice":"600","desc":"过五关斩六将，逐个击破N1语法。日语QQ群：363376722"},{"price":"0","name":"N1词汇考前须知","id":302,"pic":"1-302","classNum":3,"viewCount":27046,"ownerid":"1","realprice":"100","desc":"茫茫词海，无从入手？掌握方法，考试不愁。方法=分数 日语交流群：363376722"},{"price":"180","name":"发音规则大整理","id":303,"pic":"1-303","classNum":5,"viewCount":6174,"ownerid":"1","realprice":"200","desc":"想要轻松玩转各种发音题吗？贺老师来帮你引路。日语交流群：363376722"},{"price":"280","name":"考前汉字词精讲","id":307,"pic":"1-307","classNum":8,"viewCount":4314,"ownerid":"1","realprice":"358","desc":"怎样逃离汉字词的陷阱，做到百战百胜？日语交流群：363376722"}]
     * nextPage : 2
     * appid : 1
     * prevPage : 1
     * currentPage : 1
     */

    private int result;
    private int uid;
    private int firstPage;
    private int lastPage;
    private int nextPage;
    private int appid;
    private int prevPage;
    private int currentPage;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : 320
         * name : 日语N1听力新题型
         * id : 1
         * pic : 1-1
         * classNum : 5
         * viewCount : 40699
         * ownerid : 1
         * realprice : 400
         * desc : 万能听力法宝有没有？方法决定分数。日语QQ群：363376722
         */

        private String price;
        private String name;
        private int id;
        private String pic;
        private int classNum;
        private int viewCount;
        private String ownerid;
        private String realprice;
        private String desc;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getClassNum() {
            return classNum;
        }

        public void setClassNum(int classNum) {
            this.classNum = classNum;
        }

        public int getViewCount() {
            return viewCount;
        }

        public void setViewCount(int viewCount) {
            this.viewCount = viewCount;
        }

        public String getOwnerid() {
            return ownerid;
        }

        public void setOwnerid(String ownerid) {
            this.ownerid = ownerid;
        }

        public String getRealprice() {
            return realprice;
        }

        public void setRealprice(String realprice) {
            this.realprice = realprice;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
