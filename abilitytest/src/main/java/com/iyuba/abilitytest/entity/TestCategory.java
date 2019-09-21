package com.iyuba.abilitytest.entity;

/**
 * Created by Administrator on 2016/10/27.
 */
public class TestCategory {
    /**类型 音义力等*/
    public String type;
    /**每个类型对应的总数*/
    public int count;
    /**每个类型正确的总数*/
    public int right;
    /**每个类型未答的题数数量*/
    public int undo;
    /**每个类型答错的题数数量*/
    public int wrong;
}
