package com.iyuba.trainingcamp.db.dbclass;

import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.bean.WordHistory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.db.dbclass
 * @class describe
 * @time 2018/8/17 11:24
 * @change
 * @chang time
 * @class describe
 */
public class GoldDateRecord {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId ;

    private String date1;

    private String word_score;

    private String sentence_score;

    private String exam_score;

    private String step;

    private String lessonid;

    public List<WordHistory> getWordResults() {
        return wordResults;
    }

    public void setWordResults(List<WordHistory> wordResults) {
        this.wordResults = wordResults;
    }

    private List<WordHistory> wordResults = new ArrayList<>();

    public String getDate() {
        return date1;
    }

    public void setDate(String date) {
        this.date1 = date;
    }

    public String getWord_score() {
        return word_score;
    }

    public void setWord_score(String word_score) {
        this.word_score = word_score;
    }

    public String getSentence_score() {
        return sentence_score;
    }

    public void setSentence_score(String sentence_score) {
        this.sentence_score = sentence_score;
    }

    public String getExam_score() {
        return exam_score;
    }

    public void setExam_score(String exam_score) {
        this.exam_score = exam_score;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }

}
