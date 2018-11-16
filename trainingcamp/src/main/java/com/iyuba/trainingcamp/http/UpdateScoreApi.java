package com.iyuba.trainingcamp.http;

import com.iyuba.trainingcamp.bean.AddCreditModule;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.http
 * @class describe
 * @time 2018/9/13 17:35
 * @change
 * @chang time
 * @class describe
 */
public interface UpdateScoreApi {
    @GET("credits/updateScore.jsp")
    Call<AddCreditModule> addCredit(@Query("uid") String uid,
//                                    @Query("idindex") String idindex,
                                    @Query("srid") String srid,
                                    @Query("flag") String flag,
                                    @Query("mobile") String mobile
    );
}
