package com.iyuba.trainingcamp.http;

import com.iyuba.trainingcamp.bean.ExamDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.http
 * @class describe
 * @time 2018/11/13 18:41
 * @change
 * @chang time
 * @class describe
 */
public interface GetExamDetailApi {
        //http://daxue.iyuba.com/ecollege/getExamDetail.jsp?appId=220&uid=2561832&lesson=Toefl&TestMode=X&mode=2&sign=cad457a80e549f81c9f834e28332aa60&format=json
        @GET("ecollege/getExamDetail.jsp?")
        Call<ExamDetail> exampleDetail(@Query("appId")String appId,
                                       @Query("uid")String uid,
                                       @Query("lesson")String lesson,
                                       @Query("TestMode")String TestMode,
                                       @Query("mode")int mode,
                                       @Query("sign")String sign,
                                       @Query("format") String format
        );

}
