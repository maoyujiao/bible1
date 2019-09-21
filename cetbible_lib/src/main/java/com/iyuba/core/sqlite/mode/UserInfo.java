package com.iyuba.core.sqlite.mode;

/**
 * 用户信息
 *
 * @author 陈彤
 */
public class UserInfo {
    public String icoins;
    public String uid;
    public String username;// 用户名
    public String doings;// 发布的心情数
    public String views;// 访客数
    public String gender;// 性别
    public String text;// 最近的心情签名
    public String follower;// 粉丝
    public String relation;// 与当前用户关系 百位我是否关注他十位特别关注 个位他是否关注我
    public String following;// 关注
    public String iyubi;
    public String vipStatus;
    public String distance;
    public String notification;
    public int studytime;
    public String position;
    public String deadline;
    public String isteacher;
    public String money;

    public UserInfo() {
        icoins = "0";
        uid = "0";
        username = "";
        doings = "0";
        views = "0";
        gender = "0";
        follower = "0";// 粉丝
        relation = "0";// 与当前用户关系 百位我是否关注他十位特别关注 个位他是否关注我
        following = "0";// 关注
        iyubi = "0";
        vipStatus = "0";
        notification = "0";
        studytime = 0;
        position = "100000";
        deadline = "";
        isteacher = "";
    }
}
