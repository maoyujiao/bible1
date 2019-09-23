package com.iyuba.abilitytest.network;



import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {
    @GET("iyuba/searchApiNew.jsp")
    Observable<SearchVoaResult> getSearchResult(@Query("format") String format,
                                                @Query("key") String key,
                                                @Query("pages") int pages,
                                                @Query("pageNum") int pageNum,
                                                @Query("parentId") int parentId,
                                                @Query("type") String type,
                                                @Query("flg") String flg);
}
