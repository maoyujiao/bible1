package com.iyuba.trainingcamp.bean;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.bean
 * @class describe
 * @time 2018/9/13 11:20
 * @change
 * @chang time
 * @class describe
 */
public class RecordWithType {
    String type ;
    String score ;
    String category ;
    String testCount ;

    public RecordWithType(String type, String score, String category, String testCount) {
        this.type = type;
        this.score = score;
        this.category = category;
        this.testCount = testCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTestCount() {
        return testCount;
    }

    public void setTestCount(String testCount) {
        this.testCount = testCount;
    }
}
