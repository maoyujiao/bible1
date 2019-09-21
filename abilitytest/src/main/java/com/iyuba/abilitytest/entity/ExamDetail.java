package com.iyuba.abilitytest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuzhenli on 2017/5/16.
 */

public class ExamDetail implements Serializable {

    /**
     * result : 1
     * mode : 2
     * totalRight : 36
     * msg : Success
     * uid : 2561832
     * dataWrong : [{"score":0,"userAnswer":"A","testTime":"2016-12-30 11:19:10.0","id":11622},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:02.0","id":11831},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:16.0","id":11833},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:26.0","id":11834},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:03.0","id":11839},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:25.0","id":11842},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:18.0","id":11843},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:20.0","id":11847},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:28.0","id":11848},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:13.0","id":11853},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:22.0","id":11854},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:17.0","id":11855},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:17.0","id":11857},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:15.0","id":11861},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:04.0","id":11864},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:12.0","id":11865},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:14.0","id":11866},{"score":0,"userAnswer":"A","testTime":"2017-03-27 14:01:15.0","id":11870},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:06.0","id":11872},{"score":0,"userAnswer":"B","testTime":"2017-03-27 14:01:40.0","id":11874}]
     * testMode : X
     * lesson : Toefl
     * dataRight : [{"score":1,"userAnswer":"C","testTime":"2016-12-30 11:19:47.0","id":11623},{"score":1,"userAnswer":"A","testTime":"2016-12-30 11:19:12.0","id":11624},{"score":1,"userAnswer":"C","testTime":"2016-12-30 11:19:50.0","id":11625},{"score":1,"userAnswer":"A","testTime":"2016-12-30 11:19:26.0","id":11626},{"score":1,"userAnswer":"D","testTime":"2016-12-30 11:19:19.0","id":11627},{"score":1,"userAnswer":"D","testTime":"2016-12-30 11:19:22.0","id":11628},{"score":1,"userAnswer":"B","testTime":"2016-12-30 11:19:54.0","id":11629},{"score":1,"userAnswer":"B","testTime":"2017-01-06 11:08:05.0","id":11828},{"score":1,"userAnswer":"C","testTime":"2017-01-06 11:07:37.0","id":11829},{"score":1,"userAnswer":"A","testTime":"2017-01-07 16:22:04.0","id":11830},{"score":1,"userAnswer":"A","testTime":"2017-01-06 11:07:41.0","id":11832},{"score":1,"userAnswer":"B","testTime":"2017-03-27 14:01:24.0","id":11835},{"score":1,"userAnswer":"B","testTime":"2017-01-06 11:08:09.0","id":11836},{"score":1,"userAnswer":"D","testTime":"2017-01-06 11:07:34.0","id":11837},{"score":1,"userAnswer":"A","testTime":"2017-01-06 11:08:08.0","id":11838},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:18:53.0","id":11840},{"score":1,"userAnswer":"D","testTime":"2017-01-06 11:07:43.0","id":11841},{"score":1,"userAnswer":"B","testTime":"2017-03-27 14:01:21.0","id":11844},{"score":1,"userAnswer":"C","testTime":"2017-01-06 11:08:10.0","id":11845},{"score":1,"userAnswer":"A","testTime":"2017-01-06 11:08:10.0","id":11846},{"score":1,"userAnswer":"B","testTime":"2017-03-27 14:01:23.0","id":11849},{"score":1,"userAnswer":"A","testTime":"2017-01-06 11:07:55.0","id":11850},{"score":1,"userAnswer":"A","testTime":"2017-01-07 16:22:01.0","id":11851},{"score":1,"userAnswer":"B","testTime":"2017-03-27 14:01:19.0","id":11852},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:18:43.0","id":11856},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:20:31.0","id":11858},{"score":1,"userAnswer":"B","testTime":"2017-01-06 11:08:13.0","id":11859},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:20:17.0","id":11860},{"score":1,"userAnswer":"B","testTime":"2017-03-27 14:01:07.0","id":11862},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:20:34.0","id":11863},{"score":1,"userAnswer":"B","testTime":"2017-01-06 11:07:44.0","id":11867},{"score":1,"userAnswer":"A","testTime":"2017-01-06 11:07:31.0","id":11868},{"score":1,"userAnswer":"C","testTime":"2017-03-13 20:20:26.0","id":11869},{"score":1,"userAnswer":"B","testTime":"2017-03-13 20:20:38.0","id":11871},{"score":1,"userAnswer":"A","testTime":"2017-03-13 20:18:57.0","id":11873},{"score":1,"userAnswer":"B","testTime":"2017-01-06 11:08:00.0","id":11875}]
     * totalWrong : 20
     */

    private int result;
    private String mode;
    private int totalRight;
    private String msg;
    private int uid;
    private String testMode;
    private String lesson;
    private int totalWrong;
    private List<DataWrongBean> dataWrong;
    private List<DataRightBean> dataRight;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getTotalRight() {
        return totalRight;
    }

    public void setTotalRight(int totalRight) {
        this.totalRight = totalRight;
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

    public int getTotalWrong() {
        return totalWrong;
    }

    public void setTotalWrong(int totalWrong) {
        this.totalWrong = totalWrong;
    }

    public List<DataWrongBean> getDataWrong() {
        return dataWrong;
    }

    public void setDataWrong(List<DataWrongBean> dataWrong) {
        this.dataWrong = dataWrong;
    }

    public List<DataRightBean> getDataRight() {
        return dataRight;
    }

    public void setDataRight(List<DataRightBean> dataRight) {
        this.dataRight = dataRight;
    }

    public static class DataWrongBean extends DataBean {

    }

    public static class DataRightBean extends DataBean {

    }

    public static class DataBean implements Serializable{
        /**
         * score : 1
         * userAnswer : C
         * testTime : 2016-12-30 11:19:47.0
         * id : 11623
         */

        private int score;
        private String userAnswer;
        private String testTime;
        private int id;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getTestTime() {
            return testTime;
        }

        public void setTestTime(String testTime) {
            this.testTime = testTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
