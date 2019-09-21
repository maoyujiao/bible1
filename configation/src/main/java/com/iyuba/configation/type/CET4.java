package com.iyuba.configation.type;

/**
 * 英语四级
 */
public class CET4 implements IAPP {

    @Override
    public boolean isEnglish() {
        return true;
    }

    @Override
    public String TYPE() {
        return "4";
    }

    @Override
    public String APPName() {
        return "英语四级";
    }

    @Override
    public String APP() {
        return APPName();
    }

    @Override
    public String PACKAGE_NAME() {
        return "com.iyuba.CET4bible";
    }

    @Override
    public String AppName() {
        return "Valuablecet4";
    }

    @Override
    public String APPID() {
        return "246";
    }

    @Override
    public String presentId() {
        return "52";
    }

    @Override
    public String book() {
        return "免费赠送四级真题书";
    }

    @Override
    public String courseTypeId() {
        return "2";
    }

    @Override
    public String courseTypeTitle() {
        return "CET4课程";
    }

    @Override
    public String mListen() {
        return "cet4";
    }

    @Override
    public String SMSAPPID() {
        return "1be04a3f42f60";
    }

    @Override
    public String SMSAPPSECRET() {
        return "0608ce4f6d9cda803453c1aaf3335d26";
    }

    @Override
    public String APP_DATA_PATH() {
        return "cet4bible";
    }

    @Override
    public int cetDatabaseLastVersion() {
        return 13;
    }

    @Override
    public int cetDatabaseCurVersion() {
        return 14;
    }

    @Override
    public String BLOG_ACCOUNT_ID() {
        return "242141";
    }

    @Override
    public String ADDAM_APPKEY() {
        return "b927f9fcd8a715420017371239d08a8c";
    }

    @Override
    public String MOB_APPKEY() {
        return "1be04a3f42f60";
    }

    @Override
    public String MOB_APP_SECRET() {
        return "0608ce4f6d9cda803453c1aaf3335d26";
    }

    @Override
    public String HEAD() {
        return "BDCET4NewScroll";
    }

    @Override
    public int VIP_STATUS() {
        return 2;
    }

}
