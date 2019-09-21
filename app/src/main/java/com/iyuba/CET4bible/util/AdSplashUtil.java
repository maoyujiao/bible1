package com.iyuba.CET4bible.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.bean.IyubaADBean;
import com.iyuba.configation.Constant;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class AdSplashUtil {
    private YouDaoNative youdaoNative;
    private Context mContext;
    private ImageView adView;
    private boolean isAdClick = false;
    private boolean isRequestAdEnd = false;

    public AdSplashUtil(final Context context, ImageView view) {
        this.mContext = context;
        //安卓VOA常速启动页开屏广告_9755487e03c2ff683be4e2d3218a2f2b
        //安卓VOA慢速启动页开屏广告_ a710131df1638d888ff85698f0203b46
        youdaoNative = new YouDaoNative(context, "a710131df1638d888ff85698f0203b46", youDaoAdListener);
        adView = view;
    }


    public void requestAd() {
        if (AdSplashUtil.isNoAdTime()) {
            if (myCallback != null) {
                myCallback.startTimer();
            }
            return;
        }


        String userId = AccountManager.Instace(mContext).getId();
        if (TextUtils.isEmpty(userId)) {
            userId = "0";
        }
        String url = "http://dev.iyuba.cn/getAdEntryAll.jsp?uid=" + userId
                + "&appId=" + //"148"
                Constant.APPID //148
                + "&flag=1";
        Request request = new Request.Builder().url(url).build();

        Log.e("http request url : ", url);
        Http.getOkHttpClient3().newCall(request).enqueue(new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                if (myCallback != null) {
                    myCallback.startTimer();
                }
                isRequestAdEnd = true;
                List<IyubaADBean> list = null;
                try {
                    list = new Gson().fromJson(response, new TypeToken<List<IyubaADBean>>() {
                    }.getType());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if(list != null) {
//                    Log.i("diaodebug", list.size() + "::::" + list.get(0).getData().getStartuppic_Url());
                }
                if (list == null || list.size() < 1) {
                    showYDSplash();
                    return;
                }

                try {
                    IyubaADBean adBean = list.get(0);

                    if ("1".equals(adBean.getResult())) {
                        if ("youdao".equals(adBean.getData().getType())) {
                            showYDSplash();
                        } else if ("web".equals(adBean.getData().getType())) {
                            if (myCallback != null) {
                                myCallback.loadLocal();
                            }
                        } else {
                            if (myCallback != null) {
                                myCallback.loadLocal();
                            }
                        }
                    } else {
                        showYDSplash();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showYDSplash();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (myCallback != null) {
                    myCallback.startTimer();
                }
                isRequestAdEnd = true;
                showYDSplash();
            }
        });

    }

    public void showYDSplash() {
        Log.e("有道开屏广告加载", "有道开屏广告加载");
        RequestParameters requestParameters = new RequestParameters.Builder().build();
        youdaoNative.makeRequest(requestParameters);
    }

    public void destroy() {
        youdaoNative.destroy();
    }

    public void setCallback(SCallback callback) {
        this.myCallback = callback;
    }

    public boolean isRequestEnd() {
        return isRequestAdEnd;
    }

    public boolean isClick() {
        return isAdClick;
    }

    public interface SCallback {
        void loadLocal();

        void onAdClick();

        void startTimer();
    }

    private SCallback myCallback;

    private YouDaoNative.YouDaoNativeNetworkListener youDaoAdListener = new YouDaoNative.YouDaoNativeNetworkListener() {
        @Override
        public void onNativeLoad(final NativeResponse nativeResponse) {
            Log.e("有道开屏广告加载成功", "有道开屏广告加载成功");
            if (mContext == null) {
                return;
            }
            List<String> imageUrls = new ArrayList<>();
            imageUrls.add(nativeResponse.getMainImageUrl());
            adView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeResponse.handleClick(adView);
                    isAdClick = true;
                    if (myCallback != null) {
                        myCallback.onAdClick();
                    }
                }
            });
            ImageService.get(mContext, imageUrls, new ImageService.ImageServiceListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final Map<String, Bitmap> bitmaps) {
                    if (nativeResponse.getMainImageUrl() != null) {
                        Bitmap bitMap = bitmaps.get(nativeResponse.getMainImageUrl());
                        if (bitMap != null) {
                            adView.setImageBitmap(bitMap);
                            nativeResponse.recordImpression(adView);
                        }
                    }
                }

                @Override
                public void onFail() {
                }
            });
        }

        @Override
        public void onNativeFail(NativeErrorCode nativeErrorCode) {
            Log.e("有道广告加载失败onNativeFail", nativeErrorCode.name());
        }
    };

    public static boolean isNoAdTime() {
        if (BuildConfig.DEBUG) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year == 2018 && month == 2 && day <= 14;
    }
}
