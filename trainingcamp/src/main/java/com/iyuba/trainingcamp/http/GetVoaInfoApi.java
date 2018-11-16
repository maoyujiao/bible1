package com.iyuba.trainingcamp.http;

import com.iyuba.trainingcamp.bean.AddCreditModule;
import com.iyuba.trainingcamp.bean.BBCInfoBean;
import com.iyuba.trainingcamp.bean.VoaInfoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.http
 * @class describe
 * @time 2018/11/6 18:31
 * @change
 * @chang time
 * @class describe
 */
public interface GetVoaInfoApi {
    @Headers("Cache-Control: public, max-age=" +  3600)
    @GET("/iyuba/titleOneApi.jsp?type=android&parentID=json")
    Call<VoaInfoBean> getVoaInfo(@Query("voaids") String voaids);
//                                    @Query("idindex") String idindex,);

    @GET("/minutes/titleOneApi.jsp?type=android&parentID=0")
    Call<BBCInfoBean> getBBCInfo(@Query("bbcids") String voaids);
}
