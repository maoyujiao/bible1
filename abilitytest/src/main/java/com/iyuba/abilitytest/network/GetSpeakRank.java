package com.iyuba.abilitytest.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.cet4.activity.fragment
 * @class describe
 * @time 2019/1/22 11:08
 * @change
 * @chang time
 * @class describe
 */
public class GetSpeakRank {


    @SerializedName("result")
    public int result;
    @SerializedName("message")
    public String message = "";

    @SerializedName("myid")
    public int myId;
    @SerializedName("myname")
    public String myName;
    @SerializedName("vip")
    public String vip;
    @SerializedName("mycount")
    public int myCount;
    @SerializedName("myscores")
    public int myScore;
    @SerializedName("myranking")
    public int myRanking;
    @SerializedName("myimgSrc")
    public String myImgSrc;
    @SerializedName("data")
    public List<SpeakRank> data;

}

