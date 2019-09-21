package com.iyuba.core.http;

import android.os.Handler;
import android.os.Looper;

import com.iyuba.core.util.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class HttpCallback implements Callback {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public abstract void onSucceed(Call call, String response);

    public abstract void onError(Call call, Exception e);

    @Override
    public void onFailure(final Call call, final IOException e) {
        LogUtils.e("HTTP REQUEST ERROR : " + e.getMessage());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, Response response) {
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

        LogUtils.e("http response body : " + res);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSucceed(call, res);
            }
        });
    }
}
