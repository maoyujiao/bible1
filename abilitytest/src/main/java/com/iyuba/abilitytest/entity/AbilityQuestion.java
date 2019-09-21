package com.iyuba.abilitytest.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 试题内容 每一个题目的字段
 * Created by liuzhenli on 2017/5/16.
 */

public class AbilityQuestion implements Serializable {
    /**
     * result : 1
     * Total : 2542
     * Time : 20
     * "lesson": "GZ1",
     * TestList :
     * [
     * {
     * "TestId":"1",
     * "Sounds":"representative.mp3",
     * "Answer":"representative",
     * "Category":"单词拼写",
     * "Question":"Write down the correct word, according to the recording. ",
     * "id":"11614",
     * "TestType":"8",
     * "Tags":"单词识记"
     * }
     * ]
     */

    private String lesson;
    private String result;
    private int Total;
    private int Time;// min---the time for the user to do the exam
    private List<TestListBean> TestList;

    public String getResult() {
        return result;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getLesson() {
        return lesson;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public List<TestListBean> getTestList() {
        return TestList;
    }

    public void setTestList(List<TestListBean> TestList) {
        this.TestList = TestList;
    }

    public static class TestListBean implements Serializable {
        /**
         * TestId : 1
         * Sounds : representative.mp3
         * Answer : representative
         * Category : 单词拼写
         * Question : Write down the correct word, according to the recording.
         * id : 11614
         * TestType : 8
         * Tags : 单词识记
         * Answer4 : 神圣的
         * Answer2 : 可爱的
         * Answer3 : 细致的
         * Answer1 : 无数的；种种的
         */

        private int TestId;//同一个类型的题目从1开始  例如使用同一个音频的几个题目
        private String Sounds;//音频文件名称
        private String Answer;//正确答案
        private String Category;//第二个界面的纬度
        private String Question;//问题
        private String Attach;//txt文档
        private String Pic;//图片
        private int id;//题目的唯一id
        private int TestType;//题目的类型  单选 多选 判断 填空等
        private String Tags;//每个题目的考察类型
        private String Answer5;
        private String Answer4;
        private String Answer2;
        private String Answer3;
        private String Answer1;
        private String result;//用户是否答对该题目
        private String userAnswer;//用户的答案
        private int Lessonid;//20170629 新增字段 区分该题目属于第几个单元
        public String Explains;//20170919 新增字段 解析

        public boolean flag_ever_do;//开关 标记进入题目的时候是否显示答案

        public void setLessonId(int lessonid) {
            this.Lessonid = lessonid;
        }

        public int getLessonId() {
            return Lessonid;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public void setImage(String image) {
            this.Pic = image;
        }

        public String getImage() {
            return Pic;
        }

        public String getAttach() {
            return Attach;
        }

        public void setAttach(String Attach) {
            this.Attach = Attach;
        }

        public int getTestId() {
            return TestId;
        }

        public void setTestId(int TestId) {
            this.TestId = TestId;
        }

        public String getSounds() {
            return Sounds;
        }

        public void setSounds(String Sounds) {
            this.Sounds = Sounds;
        }

        public String getAnswer() {
            return Answer;
        }

        public void setAnswer(String Answer) {
            this.Answer = Answer;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String Question) {
            this.Question = Question;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTestType() {
            return TestType;
        }

        public void setTestType(int TestType) {
            this.TestType = TestType;
        }

        public String getTags() {
            return Tags;
        }

        public void setTags(String Tags) {
            this.Tags = Tags;
        }

        public void setAnswer5(String Answer4) {
            this.Answer5 = Answer5;
        }

        public String getAnswer5() {
            return Answer5;
        }

        public String getAnswer4() {
            return Answer4;
        }

        public void setAnswer4(String Answer4) {
            this.Answer4 = Answer4;
        }

        public String getAnswer2() {
            return Answer2;
        }

        public void setAnswer2(String Answer2) {
            this.Answer2 = Answer2;
        }

        public String getAnswer3() {
            return Answer3;
        }

        public void setAnswer3(String Answer3) {
            this.Answer3 = Answer3;
        }

        public String getAnswer1() {
            return Answer1;
        }

        public void setAnswer1(String Answer1) {
            this.Answer1 = Answer1;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        @Override
        public String toString() {
            return "TestListBean{" +
                    "TestId=" + TestId +
                    ", Sounds='" + Sounds + '\'' +
                    ", Answer='" + Answer + '\'' +
                    ", Category='" + Category + '\'' +
                    ", Question='" + Question + '\'' +
                    ", Attach='" + Attach + '\'' +
                    ", Pic='" + Pic + '\'' +
                    ", id=" + id +
                    ", TestType=" + TestType +
                    ", Tags='" + Tags + '\'' +
                    ", Answer5='" + Answer5 + '\'' +
                    ", Answer4='" + Answer4 + '\'' +
                    ", Answer2='" + Answer2 + '\'' +
                    ", Answer3='" + Answer3 + '\'' +
                    ", Answer1='" + Answer1 + '\'' +
                    ", result='" + result + '\'' +
                    ", userAnswer='" + userAnswer + '\'' +
                    ", Lessonid=" + Lessonid +
                    ", Explains='" + Explains + '\'' +
                    ", flag_ever_do=" + flag_ever_do +
                    '}';
        }
    }
}
