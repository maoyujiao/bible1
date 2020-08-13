package com.iyuba.wordtest.network;

import com.google.gson.Gson;

import java.io.File;
import java.security.Signature;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    static  OkHttpClient okHttpClient ;

    static Retrofit retrofit ;
    private static SearchApi searchApi;
    private static SignApi signApi;

    public static EvaluateApi getEvaluateApi(){
        buildOkHttpClent();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://ai.iyuba.cn/test/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EvaluateApi.class);
    }

    public static WordApi getWordApi(){
        buildOkHttpClent();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://word.iyuba.cn/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WordApi.class);

    }

    public static SearchApi getSearchApi() {
        if (searchApi == null) {
            buildOkHttpClent();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://apps.iyuba.cn/")
                    .build();
            searchApi = retrofit.create(SearchApi.class);
        }
        return searchApi;
    }


    private static void buildOkHttpClent() {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .build();
    }
    public static RequestBody fromString(String text) {
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

    public static MultipartBody.Part fromFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }


    public static SignApi getSignApi() {
        if (signApi == null) {
            buildOkHttpClent();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://apps.iyuba.cn/")
                    .build();
            signApi = retrofit.create(SignApi.class);
        }
        return signApi;
    }
}
