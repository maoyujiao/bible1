package com.iyuba.abilitytest.protocol;

import com.iyuba.core.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadTestRecordRequest implements GetResponse {
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String responseString = "";

    public UploadTestRecordRequest(String jsonStr, String requestUrl) {
        postJson(requestUrl, jsonStr);
    }


    private void postJson(String url, String json) {
        LogUtils.e("url---   " + url + "\njson" + json);
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        //json为String类型的json数据
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if (response.isSuccessful()) {
                //打印服务端返回结果
                String res = response.body().string();
                LogUtils.e("返回结果:   " + res);
                this.responseString = res.substring(res.indexOf("{"), res.lastIndexOf("}") + 1);

            }
        } catch (IOException e) {

            e.printStackTrace();
            LogUtils.e("上传大数据时候,链接网络出错了");
        }

    }


    @Override
    public String getResultByName(String name) {
        String result = "-2";
        try {
            JSONObject job = new JSONObject(this.responseString);
            LogUtils.e("result :  " + job.toString());
            if (job.has(name)) result = job.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}

