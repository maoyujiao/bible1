package com.iyuba.abilitytest.network;

import com.iyuba.abilitytest.entity.ExamDetail;
import com.iyuba.abilitytest.entity.ExamScore;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 获取用户练习测试结果  做过的每一个题目的详情
 * Created by liuzhenli on 2017/5/16.
 */

public interface ExamScoreApi {
  //http://daxue.iyuba.cn/ecollege/getExamScore.jsp?
  // appId=248
  // &uid=3212989
  // &lesson=Toefl
  // &testMode=
  // &flg=1
  // &sign=6e3a91f8d6df8e551b4d055a538e2253
  // &format=json
    @GET("ecollege/getExamScore.jsp?")
    Call<ExamScore> exampleScore(@Query("appId") String appId,
                                 @Query("uid") String uid,
                                 @Query("lesson") String lesson,
                                 @Query("testMode") String TestMode,
                                 @Query("flg") int flg,
                                 @Query("sign") String sign,
                                 @Query("format") String format
    );
}
