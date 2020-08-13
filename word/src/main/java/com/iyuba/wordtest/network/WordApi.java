package com.iyuba.wordtest.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordApi {

    /*setAbsoluteURI("http://word.iyuba.cn/words/updateWord.jsp?userId="
                           + this.userId + "&mod=" + update_mode + "&groupName="
                           + groupname + "&word=" + this.word);*/
    @GET("words/updateWord.jsp?")
    Observable<ResponseBody> operateWord(
            @Query("word") String word,
            @Query("mod") String mod,
            @Query("groupName") String groupname,
            @Query("userId") String userId,
            @Query("format") String format
    );



}
