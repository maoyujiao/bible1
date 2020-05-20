package com.iyuba.wordtest.manager;

public class WordManager {


    public static WordManager wordManager ;
    public  String username ;
    public  String userid ;
    public  String appid ;
    public  String type ;
    public  int vip ;

    public static WordManager get(){
        if (wordManager == null){
            return new WordManager();
        }
        return wordManager;
    }

    public void init(String name ,String id ,String appId , String appType,int vipStatus){
        username = name ;
        userid = id ;
        appid = appId ;
        type = appType ;
        vip =  vipStatus ;
    }

}
