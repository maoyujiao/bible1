package com.iyuba.abilitytest.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2019/1/23 13:41
 * @change
 * @chang time
 * @class describe
 */
public class GetUserWorks {
    @SerializedName("result")
    public boolean result;
    @SerializedName("message")
    public String message;
    @SerializedName("count")
    public int count;
    @SerializedName("data")
    public List<SpeakRankWork> data;
}
