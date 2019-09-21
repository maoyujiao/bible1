package com.iyuba.abilitytest.network;

import com.iyuba.abilitytest.entity.ExamDetail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 获取用户练习测试结果  做过的每一个题目的详情
 * Created by liuzhenli on 2017/5/16.
 */

public interface ExamDetailApi {
    //http://daxue.iyuba.cn/ecollege/getExamDetail.jsp?appId=220&uid=2561832&lesson=Toefl&TestMode=X&mode=2&sign=cad457a80e549f81c9f834e28332aa60&format=json
    @GET("ecollege/getExamDetail.jsp?")
    Call<ExamDetail> exampleDetail(@Query("appId")String appId,
                                      @Query("uid")String uid,
                                      @Query("lesson")String lesson,
                                      @Query("TestMode")String TestMode,
                                      @Query("mode")int mode,
                                      @Query("sign")String sign,
                                      @Query("format") String format
    );

    @GET("ecollege/getTopicRanking.jsp")
    Call<GetSpeakRank> getSpeakRank(@Query("type") String type,
                                    @Query("uid") int userId,
                                    @Query("topic") String topic,
                                    @Query("topicid") int topicId,
                                    @Query("start") int start,
                                    @Query("total") int total,
                                    @Query("sign") String sign,
                                    @QueryMap Map<String, String> extra);

    @GET("ecollege/getTopicRanking.jsp")
    Call<GetSpeakRank> getSpeakRank2(@Query("type") String type,
                                     @Query("uid") int userId,
                                     @Query("topic") String topic,
                                     @Query("topicid") int topicId,
                                     @Query("start") int start,
                                     @Query("total") int total,
                                     @QueryMap Map<String, String> extra);
}
