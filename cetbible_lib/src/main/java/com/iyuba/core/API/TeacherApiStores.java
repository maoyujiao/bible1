package com.iyuba.core.API;


import com.iyuba.core.bean.QuestionListBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 作者：renzhy on 16/4/26 16:44
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface TeacherApiStores {

    @GET("question/getQuestionList.jsp")
    Call<QuestionListBean> getQuesList(@Query("format") String format, @Query("type") int type,
                                       @Query("category1") int category1, @Query("category2") int category2,
                                       @Query("pageNum") int pageNum, @Query("isanswered") int isanswered);
}
