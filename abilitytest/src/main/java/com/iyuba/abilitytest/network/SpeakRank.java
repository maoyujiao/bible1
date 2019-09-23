package com.iyuba.abilitytest.network;

import com.google.gson.annotations.SerializedName;

public class SpeakRank {
    @SerializedName("uid")
    public int uid;
    @SerializedName("vip")
    public String vip;
    @SerializedName("sort")
    public int sort;
    @SerializedName("count")
    public int count;
    @SerializedName("scores")
    public int score;
    @SerializedName("imgSrc")
    public String imgSrc;
    @SerializedName("name")
    public String name = "";
    @SerializedName("ranking")
    public int ranking;

    public boolean isVip() {
        return !vip.equals("0");
    }

    public int getAverageScore() {
        if (count <= 0) {
            return 0;
        } else {
            return score / count;
        }
    }



}
