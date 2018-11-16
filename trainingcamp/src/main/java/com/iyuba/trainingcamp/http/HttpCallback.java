package com.iyuba.trainingcamp.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.training.http
 * @class describe
 * @time 2018/7/4 15:08
 * @change
 * @chang time
 * @class describe
 */
public abstract class HttpCallback implements Callback{
    public abstract void onSucceed(Call call, String response);

    public abstract void onError(Call call, Exception e);

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        Log.e("HttpCallback","HTTP REQUEST ERROR : " + e.getMessage());
        Log.e("HttpCallback","HTTP REQUEST ERROR : " + call.request().url().toString());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, Response response)  {
        if (!response.isSuccessful()) {
            onFailure(call, new IOException("HTTP REQUEST ERROR"));
            return;
        }
        final String res;
        try {
            res = response.body().string().trim();
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(call, new IOException("HTTP REQUEST ERROR"));
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSucceed(call, res);
            }
        });
        response.close();
    }
}
