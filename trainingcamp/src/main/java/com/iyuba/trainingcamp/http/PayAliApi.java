package com.iyuba.trainingcamp.http;

import com.iyuba.trainingcamp.bean.AbilityQuestion;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.http
 * @class describe
 * @time 2018/10/27 16:47
 * @change
 * @chang time
 * @class describe
 */
interface  PayAliApi {

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("vdyweb/ws/rest/Login")
    Call<ResponseBody>getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型



    @POST("jewel-api/api/security/login")
    Call<ResponseBody> Login(@Body RequestBody loginReq);
}
