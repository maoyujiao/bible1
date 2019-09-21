package com.iyuba.abilitytest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页测试结果
 * Created by liuzhenli on 2017/5/17.
 */

public class ExamScore implements Serializable {

    /**
     * result : 1
     * msg : Success
     * uid : 3212989
     * total : 3
     * data : [{"TestTime":"2017-03-23 10:46:57.0","Score":34,"TestMode":"L"},{"TestTime":"2017-02-23 18:15:38.0","Score":60,"TestMode":"R"},{"TestTime":"2017-03-23 10:48:46.0","Score":27,"TestMode":"X"}]
     * testMode :
     * lesson : Toefl
     */

    private int result;
    private String msg;
    private int uid;
    private int total;
    private String testMode;
    private String lesson;
    private ArrayList<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * TestTime : 2017-03-23 10:46:57.0
         * Score : 34
         * TestMode : L
         */

        private String TestTime;
        private int Score;
        private String TestMode;
        private String Category;

        public void setCategory(String category){
            this.Category=category;
        }
        public String getCategory(){
            return Category;
        }
        public String getTestTime() {
            return TestTime;
        }

        public void setTestTime(String TestTime) {
            this.TestTime = TestTime;
        }

        public int getScore() {
            return Score;
        }

        public void setScore(int Score) {
            this.Score = Score;
        }

        public String getTestMode() {
            return TestMode;
        }

        public void setTestMode(String TestMode) {
            this.TestMode = TestMode;
        }
    }
}
