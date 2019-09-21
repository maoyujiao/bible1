package com.iyuba.abilitytest.entity;

public class TestRecord {
    public String uid;
    public String Id;//题目的唯一编号
    public String BeginTime;//测试的开始时间
    public int TestNumber;     //题号
    public String UserAnswer = "";    //用户答案
    public String RightAnswer;    //正确答案
    public int AnswerResult;    //正确与否：错误；1：正确
    public String TestTime;    //测试时间
    public boolean IsUpload;
    public String Categroy;//请再增加一项“Category" 代表“ 中英力”等内容

    public String deviceId;//设备Id
    public String appId;//app在爱语吧平台的Id
    public String TestCategory;//测试模式 W--单词 L--听力
    public int mode;//测评   2练习

    public int index;//测试模式
}
