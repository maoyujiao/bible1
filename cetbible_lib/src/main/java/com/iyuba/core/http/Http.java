package com.iyuba.core.http;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class Http {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    private static final String TAG = "Http";
    private volatile static OkHttpClient okHttpClient;
    private volatile static OkHttpClient okHttpClient3;

    /**
     * 超时时间10秒
     */
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (Http.class) {
                if (okHttpClient == null) {

                    okHttpClient = new OkHttpClient()
                            .newBuilder()
                            .addInterceptor(getInterceptor())
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 超时时间3秒
     */
    public static OkHttpClient getOkHttpClient3() {
        if (okHttpClient3 == null) {
            synchronized (Http.class) {
                if (okHttpClient3 == null) {

                    okHttpClient3 = new OkHttpClient()
                            .newBuilder()
                            .addInterceptor(getInterceptor())
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build();
                }
            }
        }
        return okHttpClient3;
    }

    public static Interceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static void post(String url, Map map, Callback callback) {
        post(getOkHttpClient(), url, map, callback);
    }

    public static void post(OkHttpClient client, String url, Map map, Callback callback) {
        try {
            String json = new Gson().toJson(map);
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void get(String url, Callback callback) {
        get(getOkHttpClient(), url, callback);
    }

    public static void get(OkHttpClient client, String url, Callback callback) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date());
    }

}
