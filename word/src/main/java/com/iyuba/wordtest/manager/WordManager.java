package com.iyuba.wordtest.manager;

public class WordManager {

    public static String username ;
    public static String userid ;
    public static String appid ;
    public static String type ;
    public static int vip ;

    public static void init(String name ,String id ,String appId , String appType
    ,int vipStatus){
        username = name ;
        userid = id ;
        appid = appId ;
        type = appType ;
        vip =  vipStatus ;
    }

}
