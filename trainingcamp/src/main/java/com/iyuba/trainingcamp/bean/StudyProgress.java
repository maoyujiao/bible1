package com.iyuba.trainingcamp.bean;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.bean
 * @class describe
 * @time 2018/11/14 11:36
 * @change
 * @chang time
 * @class describe
 */
public class StudyProgress {

    /**
     * result : 1
     * total : 3
     * data : [{"readingtime":0,"flg":1,"titleid":4085,"updateTime":"2018-09-03 14:03:14.0","readingwords":0,"uid":4729911,"score":0,"times":0,"studytime":62,"finishday":17777,"id":74,"planday":17777,"categoryid":3},{"readingtime":0,"flg":1,"titleid":4086,"updateTime":"2018-09-03 14:05:08.0","readingwords":0,"uid":4729911,"score":0,"times":0,"studytime":13,"finishday":17777,"id":75,"planday":17777,"categoryid":3},{"readingtime":81,"flg":3,"titleid":6802,"updateTime":"2018-09-03 13:49:01.0","readingwords":89,"uid":4729911,"score":0,"times":4,"studytime":191,"finishday":17777,"id":73,"planday":17777,"categoryid":3}]
     * message : ok
     */

    private int result;
    private int total;
    private String message;
    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * readingtime : 0
         * flg : 1
         * titleid : 4085
         * updateTime : 2018-09-03 14:03:14.0
         * readingwords : 0
         * uid : 4729911
         * score : 0
         * times : 0
         * studytime : 62
         * finishday : 17777
         * id : 74
         * planday : 17777
         * categoryid : 3
         */

        private int readingtime;
        private int flg;
        private int titleid;
        private String updateTime;
        private int readingwords;
        private int uid;
        private int score;
        private int times;
        private int studytime;
        private int finishday;
        private int id;
        private int planday;
        private int categoryid;

        public int getReadingtime() {
            return readingtime;
        }

        public void setReadingtime(int readingtime) {
            this.readingtime = readingtime;
        }

        public int getFlg() {
            return flg;
        }

        public void setFlg(int flg) {
            this.flg = flg;
        }

        public int getTitleid() {
            return titleid;
        }

        public void setTitleid(int titleid) {
            this.titleid = titleid;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getReadingwords() {
            return readingwords;
        }

        public void setReadingwords(int readingwords) {
            this.readingwords = readingwords;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public int getStudytime() {
            return studytime;
        }

        public void setStudytime(int studytime) {
            this.studytime = studytime;
        }

        public int getFinishday() {
            return finishday;
        }

        public void setFinishday(int finishday) {
            this.finishday = finishday;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlanday() {
            return planday;
        }

        public void setPlanday(int planday) {
            this.planday = planday;
        }

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }
    }
}
