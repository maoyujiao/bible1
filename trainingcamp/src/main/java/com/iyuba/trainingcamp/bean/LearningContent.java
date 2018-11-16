package com.iyuba.trainingcamp.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.bean
 * @class describe
 * @time 2018/7/17 16:27
 * @change
 * @chang time
 * @class describe
 */
public class LearningContent extends DataSupport implements Serializable {

//    public static final Parcelable.Creator<LearningContent> CREATOR = new Parcelable.Creator<LearningContent>() {
//        @Override
//        public LearningContent createFromParcel(Parcel in) {
//            return new LearningContent(in);
//        }
//
//        @Override
//        public LearningContent[] newArray(int size) {
//            return new LearningContent[size];
//        }
//    };

    public int index;
    public String en; //英文原文
    public String cn; //汉语释义
    public String pro; // 音标, 若句子则为句子音频的url
    public String cn_detail; //单词特有, 单词的详细翻译
    public List<String> phrases; //单词特有 引申含义
    /**
     * 是否记住了
     */
    public boolean remembered; //
    /**
     * 是否答对了了
     */
    public boolean checkPassed;

    public String score; //句子的评分

    public int id;

    public AbilityQuestion.TestListBean question;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getCn_detail() {
        return cn_detail;
    }

    public void setCn_detail(String cn_detail) {
        this.cn_detail = cn_detail;
    }

    public List<String> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<String> phrases) {
        this.phrases = phrases;
    }

    public boolean isRemembered() {
        return remembered;
    }

    public void setRemembered(boolean remembered) {
        this.remembered = remembered;
    }

    public boolean isCheckPassed() {
        return checkPassed;
    }

    public void setCheckPassed(boolean checkPassed) {
        this.checkPassed = checkPassed;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AbilityQuestion.TestListBean getQuestion() {
        return question;
    }

    public void setQuestion(AbilityQuestion.TestListBean question) {
        this.question = question;
    }

    //    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
////        dest.writeByte(this.isAudience ? (byte) 1 : (byte) 0);
//        dest.writeInt(this.id);
//        dest.writeInt(this.index);
//        dest.writeString(this.en);
//        dest.writeString(this.cn);
//        dest.writeString(this.cn_detail);
//        dest.writeString(this.cn_detail);
//    }
    @Override
    public String toString() {
        return "Learingcontents{" +
                "index=" + index +
                ", en='" + en + '\'' +
                ", cn='" + cn + '\'' +
                ", pro='" + pro + '\'' +
                ", cn_detail='" + cn_detail + '\'' +
                '}';
    }
}
