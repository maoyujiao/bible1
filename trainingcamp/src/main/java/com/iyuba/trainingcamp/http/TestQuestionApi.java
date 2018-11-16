package com.iyuba.trainingcamp.http;

import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LessonIdBean;
import com.iyuba.trainingcamp.bean.SimpleResultBean;
import com.iyuba.trainingcamp.bean.StudyProgress;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * 试题详情 每一个题目
 * Created by liuzhenli on 2017/5/16.
 */

public interface TestQuestionApi {
    /**
     * http://class.iyuba.com/getClass.iyuba?&protocol=20000&lesson=Toefl&category=W
     * &sign=6d2f6c0396e4799027e2cb2ceb226d5c&format=json&mode=2&uid=2561832
     */
    @Headers("Cache-Control: public, max-age=" + 3600)

    @GET("getClass.iyuba?")
    Call<AbilityQuestion> testQuestionApi(@Query("protocol") String protocol,
                                          @Query("lesson") String lesson,
                                          @Query("category") String category,
                                          @Query("sign") String sign,
                                          @Query("format") String format,
                                          @Query("mode") int mode,
                                          @Query("uid") String uid,
                                          @Query("lessonid") int lessonid
    );

    @GET("getClass.iyuba?")
    Call<StudyProgress> getProgressApi(@Query("protocol") String protocol,
                                       @Query("categoryid") String category,
                                       @Query("sign") String sign,
                                       @Query("format") String format,
                                       @Query("uid") String uid
    );


    //获取lessonId
    @Headers("Cache-Control: public, max-age=" + 3600)
    @GET("getClass.iyuba?")
    Call<LessonIdBean> getLessonIdApi(@Query("protocol") String protocol,
                                      @Query("lesson") String lesson,
                                      @Query("sign") String sign,
                                      @Query("format") String format,
                                      @Query("uid") String uid
    );

    //上传学习进度
//    @Headers("Cache-Control: public, max-age=" +  3600)

    @GET("getClass.iyuba?")
    Call<SimpleResultBean> uploadStudyProgress(@Query("protocol") String protocol,
                                               @Query("categoryid") String categaryid,
                                               @Query("sign") String sign,
                                               @Query("format") String format,
                                               @Query("uid") String uid,
                                               @Query("titleid") String titleid,
                                               @Query("score") String score,
                                               @Query("day") String day,
                                               @Query("flg") String flag,
                                               @Query("time") String time
    );

}
