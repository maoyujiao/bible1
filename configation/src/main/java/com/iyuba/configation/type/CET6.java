package com.iyuba.configation.type;

/**
 * 英语六级
 */
public class CET6 implements IAPP {
    @Override
    public boolean isEnglish() {
        return true;
    }

    @Override
    public String TYPE() {
        return "6";
    }

    @Override
    public String APPName() {
        return "英语六级";
    }

    @Override
    public String APP() {
        return APPName();
    }

    @Override
    public String PACKAGE_NAME() {
        return "com.iyuba.CET6bible";
    }

    @Override
    public String AppName() {
        return "Valuablecet6";
    }

    @Override
    public String APPID() {
        return "242";
    }

    @Override
    public String presentId() {
        return "54";
    }

    @Override
    public String book() {
        return "免费赠送六级真题书";
    }

    @Override
    public String courseTypeId() {
        return "4";
    }

    @Override
    public String courseTypeTitle() {
        return "CET6课程";
    }

    @Override
    public String mListen() {
        return "cet6";
    }

    @Override
    public String SMSAPPID() {
        return "1be094844c6ed";
    }

    @Override
    public String SMSAPPSECRET() {
        return "3170b969df30def7a5507affeb4be57e";
    }

    @Override
    public String APP_DATA_PATH() {
        return "cet6bible";
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
        return "242145";
    }

    @Override
    public String ADDAM_APPKEY() {
        return "9c4f2886cda8b379b25b1b67a5400dd9";
    }

    @Override
    public String MOB_APPKEY() {
        return "1be094844c6ed";
    }

    @Override
    public String MOB_APP_SECRET() {
        return "3170b969df30def7a5507affeb4be57e";
    }

    @Override
    public String HEAD() {
        return "BDCET6NewScroll";
    }

    @Override
    public int VIP_STATUS() {
        return 4;
    }

}
