package com.iyuba.CET4bible.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.iyuba.CET4bible.bean.IyubaADBean;
import com.iyuba.adsdk.extra.common.AdWebBrowser;
import com.iyuba.base.util.L;
import com.iyuba.configation.Constant;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Request;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 加载banner广告
 */
public class AdBannerUtil {
    private static final String TAG = "Ad : Banner : ";
    private Context context;
    private YouDaoNative youdaoNative;
    private View adView;
    private ImageView adImageView;
//    private AddamBanner addamBanner;
    private ViewGroup adMiaozeParent;
//    private AdView miaozeBannerAdView;

    private String lastAD;
    private String TYPE_DAM = "addam";
    private String TYPE_YOUDAO = "youdao";
    private String TYPE_IYUBA = "web";
    private String TYPE_MIAOZE = "ssp";
    // iyuba广告一分钟加载一次
    private int intervalTime = 60 * 1000;

    // 广告自己切换的时间
    private int adIntervalTime = 10;

    private TextView close ;
    private boolean isIyubaAdTimerStarted = false;
    private Handler iyubaAdHandler = new Handler();
    private Runnable iyubaAdRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isIyubaAdTimerStarted) {
                return;
            }
            loadAd();
            iyubaAdHandler.postDelayed(iyubaAdRunnable, intervalTime);
        }
    };

    public AdBannerUtil(Context context) {
        this.context = context;
        youdaoNative = new YouDaoNative(context, "230d59b7c0a808d01b7041c2d127da95", youDaoAdListener);
    }

    public void setView(View view, ImageView imageView ,TextView textView) {
        this.adImageView = imageView;
        this.adView = view;
        this.close =textView;
    }


    public void setMiaozeView(ViewGroup viewGroup) {
        this.adMiaozeParent = viewGroup;
    }

    /**
     * iyuba三秒超时，失败后加载有道广告
     */
    public void loadAd() {
        if (context == null || AccountManager.isVip()||forCheck("2018-12-15")) {
            return;
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adView.setVisibility(View.GONE);
            }
        });

        String userId = AccountManager.Instace(context).getId();
        if (TextUtils.isEmpty(userId)) {
            userId = "0";
        }
        String url = "http://dev.iyuba.cn/getAdEntryAll.jsp?uid=" + userId
                + "&appId=" + Constant.APPID //104 224
                + "&flag=4";
        Request request = new Request.Builder().url(url).build();

        Log.e(TAG, url);
        Http.getOkHttpClient3().newCall(request).enqueue(new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                if (context == null) {
                    return;
                }
                startIyubaADTimer();

                List<IyubaADBean> list = null;
                try {
                    list = new Gson().fromJson(response, new TypeToken<List<IyubaADBean>>() {
                    }.getType());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (list == null || list.size() < 1) {
                    loadYouDaoAD();
                    return;
                }
//        String[] ads = new String[]{"ssp", "youdao", "addam", "web"};
                try {
                    IyubaADBean adBean = list.get(0);

                    if ("1".equals(adBean.getResult())) {
                        Log.e(TAG, "type = " + adBean.getData().getType());
                        Log.e(TAG, "last type = " + lastAD);

                        setAdIntervalTime(adBean.getData().getClassNum());

                        if (TYPE_YOUDAO.equals(adBean.getData().getType())) {
                            loadYouDaoAD();
                        } else if (TYPE_DAM.equals(adBean.getData().getType())) { // 嗒萌广告
                            loadDamAD();
                        } else if (adMiaozeParent != null && TYPE_MIAOZE.equals(adBean.getData().getType())) {  // 淼泽广告
                            Observable.interval(0, 10, TimeUnit.SECONDS)
                                    .take(6)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Long>() {
                                        @Override
                                        public void call(Long aLong) {

                                        }
                                    });
                        } else if (TYPE_IYUBA.equals(adBean.getData().getType().toLowerCase())) {
                            if (adBean.getData() != null
                                    && !TextUtils.isEmpty(adBean.getData().getStartuppic_Url())
                                    && !TextUtils.isEmpty(adBean.getData().getStartuppic())
                                    ) {

                                loadIyubaAD("http://static3.iyuba.cn/dev/" + adBean.getData().getStartuppic(), adBean.getData().getStartuppic_Url());
                            } else {
                                loadYouDaoAD();
                            }
                        } else {
                            loadYouDaoAD();
                        }

                        lastAD = adBean.getData().getType();
                        if (!lastAD.equals(TYPE_DAM) ) {
//                            addamBanner.unLoad();
                        }
                    } else {
                        loadYouDaoAD();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loadYouDaoAD();
                }


            }

            @Override
            public void onError(Call call, Exception e) {
                startIyubaADTimer();

                loadYouDaoAD();
            }
        });
    }

    public void setAdIntervalTime(String classNum) {
        try {
            int time = Integer.parseInt(classNum);
            if (time > 4) {
                adIntervalTime = time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.e(TAG + " interval time : " + adIntervalTime);
    }

    private int getAdIntervalTime() {
        if (adIntervalTime > 60) {
            return 60;
        }
        return adIntervalTime;
    }

    /**
     * 嗒萌广告
     */
    private void loadDamAD() {
        if (context == null) {
            return;
        }
        Log.e(TAG, "加载嗒萌广告");

        setADViewVisibility();

        if (TYPE_DAM.equals(lastAD)) {
            return;
        }
    }

    private void loadIyubaAD(String picUrl, final String adUrl) {
        if (context == null) {
            return;
        }
        setADViewVisibility();
        adView.setVisibility(View.VISIBLE);

        Glide.with(context).load(picUrl).into(adImageView);
        adView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context == null || TextUtils.isEmpty(adUrl)) {
                    return;
                }
                Intent intent = new Intent(context, AdWebBrowser.class);
                intent.putExtra("URL", adUrl);
                context.startActivity(intent);
            }
        });
    }

    private void loadYouDaoAD() {
        if (context == null) {
            return;
        }
        Log.e(TAG, "加载有道广告");
        setADViewVisibility();
        adView.setVisibility(View.VISIBLE);

        RequestParameters requestParameters = new RequestParameters.Builder().build();
        youdaoNative.makeRequest(requestParameters);
    }

    private void setADViewVisibility() {
        adView.setVisibility(View.GONE);
        if (adMiaozeParent != null) {
            adMiaozeParent.setVisibility(View.GONE);
        }
    }

    public void destroy() {
        Log.e(TAG, "onDestroy");
        context = null;
        youdaoNative.destroy();
        stopIyubaAdTimer();
    }

    /**
     * 一分钟刷新一次广告
     */
    private void startIyubaADTimer() {
        if (isIyubaAdTimerStarted) {
            return;
        }
        isIyubaAdTimerStarted = true;
        iyubaAdHandler.postDelayed(iyubaAdRunnable, intervalTime);
    }

    private void stopIyubaAdTimer() {
        isIyubaAdTimerStarted = false;
        iyubaAdHandler.removeCallbacksAndMessages(null);
    }

    private YouDaoNative.YouDaoNativeNetworkListener youDaoAdListener = new YouDaoNative.YouDaoNativeNetworkListener() {
        @Override
        public void onNativeLoad(final NativeResponse nativeResponse) {
            Log.e(TAG, "有道广告加载成功");
            if (context == null) {
                return;
            }
            List<String> imageUrls = new ArrayList<>();
            imageUrls.add(nativeResponse.getMainImageUrl());
            adView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeResponse.handleClick(adView);
                }
            });
            ImageService.get(context, imageUrls, new ImageService.ImageServiceListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final Map<String, Bitmap> bitmaps) {
                    if (nativeResponse.getMainImageUrl() != null) {
                        Bitmap bitMap = bitmaps.get(nativeResponse.getMainImageUrl());
                        if (bitMap != null) {
                            adImageView.setImageBitmap(bitMap);
                            adImageView.setVisibility(View.VISIBLE);
                            nativeResponse.recordImpression(adImageView);
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
            Log.e(TAG, "有道广告加载失败onNativeFail:  " + nativeErrorCode.name());
        }
    };

    private boolean forCheck(String s){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        if (System.currentTimeMillis()<timestamp){
            return true;
        }else {
            return false ;
        }
    }
}
