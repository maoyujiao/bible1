package com.iyuba.trainingcamp.bean;

import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.bean
 * @class describe
 * @time 2018/10/12 09:55
 * @change
 * @chang time
 * @class describe
 */
public class WordHistory extends DataSupport {
    String en ;
    String cn ;
    String passed ;

    public GoldDateRecord getRecord() {
        return mRecord;
    }

    public void setRecord(GoldDateRecord record) {
        mRecord = record;
    }

    private GoldDateRecord mRecord ;
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

    public String isPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }
}
