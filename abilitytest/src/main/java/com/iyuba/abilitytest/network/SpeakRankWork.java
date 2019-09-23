package com.iyuba.abilitytest.network;

import com.google.gson.annotations.SerializedName;

public class SpeakRankWork implements Comparable<SpeakRankWork>{

    @SerializedName("id")
    public int id;
    @SerializedName("agreeCount")
    public int agreeCount = 0;
    @SerializedName("againstCount")
    public int againstCount = 0;
    @SerializedName("ShuoShuo")
    public String shuoshuo = "";
    @SerializedName("shuoshuotype")
    public int shuoshuoType;
    @SerializedName("CreateDate")
    public String createdate;
    @SerializedName("score")
    public int score = 0; // fuck
    @SerializedName("paraid")
    public int paraid = 0; // fuck
    @SerializedName("idIndex")
    public String idindex; // fuck
    @SerializedName("TopicId")
    public int voaId;

    public String imgsrc = "";
    public String title = "";
    public String titleCn = "";
    public String description = "";

    public int userid;
    public String username = "none";

    public String readText = "";

    public boolean isAudioCommentPlaying = false;

    public String getShuoShuoUrl() {
        return "http://voa.iyuba.com/voa/" + shuoshuo;
    }

    public String getUsername() {
        return username;
    }

    public int getUpvoteCount() {
        return agreeCount;
    }

    public int getDownvoteCount() {
        return againstCount;
    }

    public String getScore() {
        return score + "分";
    }


    @Override
    public int compareTo(SpeakRankWork o) {
        if (this.shuoshuoType == 4) return -1 ;
        if (Integer.parseInt(this.idindex) < Integer.parseInt(o.idindex)) {
            return -1;
        }
        return o.shuoshuoType;
    }
}
