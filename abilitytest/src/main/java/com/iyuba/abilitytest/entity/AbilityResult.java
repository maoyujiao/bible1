package com.iyuba.abilitytest.entity;

import java.io.Serializable;

/**
 * 存储答题记录的字段 上传到大数据用
 * Created by Administrator on 2016/8/18.
 */
public class AbilityResult implements Serializable {
    /**
     * 存储的答题记录的数量 在数据库中自增
     */
    public int TestId;
    /**
     * 标记题目的类型  写作0 单词1 语法2 听力3 口语4 阅读5
     */
    public int TypeId;
    public String Score1 = "-1";//题目总数+正确个数+题目分类
    public String Score2 = "-1";
    public String Score3 = "-1";
    public String Score4 = "-1";
    public String Score5 = "-1";
    public String Score6 = "-1";
    public String Score7 = "-1";
    public String Score8 = "-1";
    public String Score9 = "-1";
    public String Score10 = "-1";
    /**
     * 一套题目的总数量
     */
    public int Total;
    /**
     * 没有答得题目数量
     */
    public int UndoNum;
    /***
     * 回答的总题目数
     */
    public int DoRight;
    /**
     * 开始答题时间
     */
    public String beginTime;
    /**
     * 结束答题时间
     */
    public String endTime;
    /**
     * 是否上传服务器  未上传0  已上传1
     */
    public int isUpload = 0;


    //从服务器获取的数据
    // "TestTime": "2016-09-24 14:05:53.0",
    //"Score": 18,
    // "TestMode": "W"
    public String testTime;
    public String score;
    public String testMode;

    public String category;

    /**
     * 用户的账号
     */
    public String uid;


}
