package com.iyuba.core.me.protocol;

import java.util.List;

class ListenDetailResponseBean {
    /**
     * result : 1
     * data : [{"EndTime":"2017-08-21 15:18:36.0","LessonId":"2017061701","BeginTime":"2017-08-21 15:18:14.0","TestNumber":"19","Lesson":"英语四级听力","TestWords":"0"}]
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
         * EndTime : 2017-08-21 15:18:36.0
         * LessonId : 2017061701
         * BeginTime : 2017-08-21 15:18:14.0
         * TestNumber : 19
         * Lesson : 英语四级听力
         * TestWords : 0
         */

        private String EndTime;
        private String LessonId;
        private String BeginTime;
        private String TestNumber;
        private String Lesson;
        private String TestWords;

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getLessonId() {
            return LessonId;
        }

        public void setLessonId(String LessonId) {
            this.LessonId = LessonId;
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

        public String getLesson() {
            return Lesson;
        }

        public void setLesson(String Lesson) {
            this.Lesson = Lesson;
        }

        public String getTestWords() {
            return TestWords;
        }

        public void setTestWords(String TestWords) {
            this.TestWords = TestWords;
        }
    }
}

