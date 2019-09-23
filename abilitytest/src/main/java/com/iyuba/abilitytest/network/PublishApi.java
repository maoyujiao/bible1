package com.iyuba.abilitytest.network;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.abilitytest.network
 * @class describe
 * @time 2019/1/19 18:47
 * @change
 * @chang time
 * @class describe
 */
public interface PublishApi {

//    @Multipart
//    @POST("UnicomApi")
//    Call<PublishResponse> publishVoice(@PartMap Map<String, RequestBody> optionMap,
//                                    @Part MultipartBody.Part file);

//    @Multipart
    @GET("UnicomApi")
    Call<PublishVoiceResponse> publishVoice(@QueryMap Map<String, String> optionMap);
    @Multipart
    @POST("UnicomApi")
    Call<PublishResponse> publishMerge(@PartMap Map<String, RequestBody> optionMap);
//
////    String ENDPOINT = "http://voa.iyuba.com/";
//
    @GET("getWorksByUserId.jsp")
    Call<GetUserWorks> getUserWorks(@Query("uid") int userId,
                                    @Query("topic") String topic,
                                    @Query("shuoshuoType") String type,
                                    @Query("sign") String sign,
                                    @QueryMap Map<String, String> map);
//
    @GET("UnicomApi")
    Call<SendGoodResponse> sendGood(@Query("id") int id, @Query("protocol") String protocol);
//
//    @GET("UnicomApi")
//    Call<SendGoodResponse> sendBad(@Query("id") int id, @Query("protocol") String protocol);

}
