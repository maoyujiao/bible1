package com.iyuba.trainingcamp.http;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.NetworkUtils;
import com.iyuba.trainingcamp.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工厂
 * Created by liuzhenli on 2017/5/16.
 */

public class VipRequestFactory {
    private static final String TAG = "VipRequestFactory";
    private static OkHttpClient okHttpClient;
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.e(TAG, message);
        }
    });

    private static Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            //有网的时候,读接口上的@Headers里的注解配置
            String cacheControl = request.cacheControl().toString();
            //没有网络并且添加了注解,才使用缓存.
            if (!NetworkUtils.isConnected()&&!TextUtils.isEmpty(cacheControl)){
                //重置请求体;
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            //如果没有添加注解,则不缓存
            if (TextUtils.isEmpty(cacheControl) || "no-store" .contains(cacheControl)) {
                //响应头设置成无缓存
                cacheControl = "no-store";
            } else if (NetworkUtils.isConnected()) {
                //如果有网络,则将缓存的过期事件,设置为0,获取最新数据
                cacheControl = "public, max-age=" + 0;
            }else {
                //...如果无网络,则根据@headers注解的设置进行缓存.
            }
            Response response = chain.proceed(request);
            LogUtils.i("httpInterceptor", cacheControl);
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    static Context mContext;

    private static void initHttpClient() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
//                .cache(new Cache(mContext.getCacheDir(), 10240 * 1024))
                .build();
    }
/*
    private static ExamDetailApi examDetailApi;

    public static ExamDetailApi getExamDetailApi() {
        if (examDetailApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue.iyuba.com/")
                    .build();
            examDetailApi = retrofit.create(ExamDetailApi.class);
        }
        return examDetailApi;
    }*/

    private static GetExamDetailApi mGetExamDetailApi ;

    private static TestQuestionApi testQuestionApi;

    private static UpdateScoreApi updateScoreApi;

    private static PayAliApi payAliApi;

    private static GetVoaInfoApi getVoaInfoApi ;


    public static TestQuestionApi getTestQuestionApi() {
        if (testQuestionApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://class.iyuba.com/")
                    .build();
            testQuestionApi = retrofit.create(TestQuestionApi.class);
        }
        return testQuestionApi;
    }

    public static UpdateScoreApi getupdateScoreApi() {
        if (updateScoreApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://api.iyuba.com/")
                    .build();
            updateScoreApi = retrofit.create(UpdateScoreApi.class);
        }
        return updateScoreApi;

    }

    public static PayAliApi getPayAliApi() {
        if (payAliApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://vip.iyuba.com/")
                    .build();
            payAliApi = retrofit.create(PayAliApi.class);
        }
        return payAliApi;
    }


    public static GetVoaInfoApi getVoaInfoApi() {
        if (getVoaInfoApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://apps.iyuba.com/")
                    .build();
            getVoaInfoApi = retrofit.create(GetVoaInfoApi.class);
        }
        return getVoaInfoApi;
    }

    public static GetExamDetailApi getExamDetailApi() {
        if (updateScoreApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://api.iyuba.com/")
                    .build();
            updateScoreApi = retrofit.create(UpdateScoreApi.class);
        }
        return mGetExamDetailApi;

    }

}

/*
    private static ExamScoreApi examScoreApi;

    public static ExamScoreApi getExamScoreApi() {
        if (examScoreApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue.iyuba.com/")
                    .build();
            examScoreApi = retrofit.create(ExamScoreApi.class);
        }
        return examScoreApi;
    }

    private static AddCreditApi addCreditApi;

    public static AddCreditApi addCredit() {
        if (addCreditApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue.iyuba.com/")
                    .build();
            addCreditApi = retrofit.create(AddCreditApi.class);
        }
        return addCreditApi;
    }
*/
//}
