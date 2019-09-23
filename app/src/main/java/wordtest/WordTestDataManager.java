package wordtest;

import android.content.Context;

import com.iyuba.headlinelibrary.data.remote.CmsService;

import java.util.concurrent.TimeUnit;

import newDB.CetDataBase;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordTestDataManager {

    CetDataBase wordDB;

    CmsService cmsService;

    public WordTestDataManager(Context context) {
        initHttp();
        wordDB = CetDataBase.getInstance(context);
    }


    public void initHttp() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        RxJava2CallAdapterFactory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
        GsonConverterFactory converterFactory = GsonConverterFactory.create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .addConverterFactory(converterFactory)
                .build();

        cmsService = retrofit.create(CmsService.class);
    }

}
