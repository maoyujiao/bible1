package com.iyuba.core.me.protocol;

import android.text.TextUtils;

import java.util.List;

class TestDetailResponseBean {

    /**
     * result : 1
     * data : [{"TestTime":"2017-08-04 09:26:14","testindex":"0","UserAnswer":"","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:08.0","TestNumber":"6677","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:07","testindex":"0","UserAnswer":"C","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:07.0","TestNumber":"6662","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:08","testindex":"0","UserAnswer":"C","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:07.0","TestNumber":"6733","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:06","testindex":"0","UserAnswer":"D","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:06.0","TestNumber":"6693","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:05","testindex":"0","UserAnswer":"D","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:05.0","TestNumber":"6749","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:04","testindex":"0","UserAnswer":"B","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:04.0","TestNumber":"6694","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:03","testindex":"0","UserAnswer":"B","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:03.0","TestNumber":"6705","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:01","testindex":"0","UserAnswer":"C","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:01.0","TestNumber":"6743","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:02","testindex":"0","UserAnswer":"C","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:01.0","TestNumber":"6655","TestWords":"0","RightAnswer":""},{"TestTime":"2017-08-04 09:26:00","testindex":"0","UserAnswer":"C","LessonId":"60","UpdateTime":"2017-08-04 09:26:11.0","BeginTime":"2017-08-04 09:26:00.0","TestNumber":"6720","TestWords":"0","RightAnswer":""}]
     * message : success
     */

    private String result;
    private String message;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
         * TestTime : 2017-08-04 09:26:14
         * testindex : 0
         * UserAnswer :
         * LessonId : 60
         * UpdateTime : 2017-08-04 09:26:11.0
         * BeginTime : 2017-08-04 09:26:08.0
         * TestNumber : 6677
         * TestWords : 0
         * RightAnswer :
         */

        private String TestTime;
        private String testindex;
        private String UserAnswer;
        private String LessonId;
        private String UpdateTime;
        private String BeginTime;
        private String TestNumber;
        private String TestWords;
        private String RightAnswer;
        private String Score;

        public String getScore() {
            if (TextUtils.isEmpty(Score)) {
                return "";
            }
            return Score;
        }

        public void setScore(String score) {
            Score = score;
        }

        public String getTestTime() {
            return TestTime;
        }

        public void setTestTime(String TestTime) {
            this.TestTime = TestTime;
        }

        public String getTestindex() {
            return testindex;
        }

        public void setTestindex(String testindex) {
            this.testindex = testindex;
        }

        public String getUserAnswer() {
            return UserAnswer;
        }

        public void setUserAnswer(String UserAnswer) {
            this.UserAnswer = UserAnswer;
        }

        public String getLessonId() {
            return LessonId;
        }

        public void setLessonId(String LessonId) {
            this.LessonId = LessonId;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getTestNumber() {
            return TestNumber;
        }

        public void setTestNumber(String TestNumber) {
            this.TestNumber = TestNumber;
        }

        public String getTestWords() {
            return TestWords;
        }

        public void setTestWords(String TestWords) {
            this.TestWords = TestWords;
        }

        public String getRightAnswer() {
            return RightAnswer;
        }

        public void setRightAnswer(String RightAnswer) {
            this.RightAnswer = RightAnswer;
        }
    }
}
