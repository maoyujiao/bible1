package com.iyuba.trainingcamp.bean;

/**
 * @author yq QQ:1032006226
 */
public class TestResultBean {

    public int wordScore ;
    public int sentenceScore ;
    public int examScore ;

    public static TestResultBean bean ;
    private TestResultBean(){}

    public synchronized static TestResultBean getBean() {
        if (null == bean){
            bean = new TestResultBean();
        }
        return bean;
    }

}
