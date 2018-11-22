package com.iyuba.CET4bible.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.addam.library.api.AddamBanner;
import com.addam.library.api.AddamError;
import com.addam.library.api.AddamManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.bean.IyubaADBean;
import com.iyuba.adsdk.extra.common.AdWebBrowser;
import com.iyuba.base.util.L;
import com.iyuba.configation.Constant;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.miaoze.sdk.dsp.DspFailInto;
import com.miaoze.sdk.dsp.dsp_out.BannerAdDsp;
import com.youdao.sdk.nativeads.ImageService;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private AddamBanner addamBanner;
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

    public void setView(View view, ImageView imageView) {
        this.adImageView = imageView;
        this.adView = view;
    }

    public void setAddamView(View view) {
        addamBanner = (AddamBanner) view;
        addamBanner.setAdSize(AddamBanner.Size.BannerAuto);
        addamBanner.setAdUnitID(Constant.ADDAM_APPKEY); // 设置广告位id
        addamBanner.setCallback(new AddamBanner.Callback() {
            @Override
            public void bannerDidDisplayed(AddamBanner addamBanner, int errorCode) {
                if (errorCode == AddamError.NO_ERROR) {
                    Log.e(TAG, "嗒萌加载成功");
                    setADViewVisibility();
                    addamBanner.setVisibility(View.VISIBLE);
                } else {
                    loadYouDaoAD();
                    Log.e(TAG, "addam error: " + errorCode);
                }
            }

            @Override
            public void bannerDidSelected(AddamBanner addamBanner) {
            }
        });
    }

    public void setMiaozeView(ViewGroup viewGroup) {
        this.adMiaozeParent = viewGroup;
    }

    /**
     * iyuba三秒超时，失败后加载有道广告
     */
    public void loadAd() {
        if (context == null || AccountManager.isVip()) {
            return;
        }

        String userId = AccountManager.Instace(context).getId();
        if (TextUtils.isEmpty(userId)) {
            userId = "0";
        }
        String url = "http://dev.iyuba.com/getAdEntryAll.jsp?uid=" + userId
                + "&appId=" + Constant.APPID //104 224
                + "&flag=4";
        Request request = new Request.Builder().url(url).build();

        Log.e(TAG, url);
        Http.getOkHttpClient3().newCall(request).enqueue(new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
//        String response = "[{\"result\":\"1\",\"data\":{\"id\":\"676\",\"adId\":\"VOA慢速英语华尔街banneriOSLearn-Word8-30\",\"startuppic_StartDate\":\"2017-08-30\",\"startuppic_EndDate\":\"2017-09-05\",\"startuppic\":\"upload/1504064808441.jpg\"," +
//                "\"type\":\"ssp\",\"startuppic_Url\":\"http://dev.iyuba.com/ad.jsp?adId=676&uid=0&appId=104\",\"classNum\":\"0\"}}]";

//        String response = "[{\"result\":\"1\",\"data\":{\"id\":\"811\",\"adId\":\"淼森信息安卓banner\",\"startuppic_StartDate\":\"2017-11-01\",\"startuppic_EndDate\":\"2017-11-29\",\"startuppic\":\"\",\"type\":\"ssp\",\"startuppic_Url\":\"http://app.iyuba.com/android/index.jsp\",\"classNum\":\"0\"}}]";
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

//            adBean.getData().setType(ads[new Random().nextInt(100) % 4]);

                    if ("1".equals(adBean.getResult())) {
                        Log.e(TAG, "type = " + adBean.getData().getType());
                        Log.e(TAG, "last type = " + lastAD);

                        setAdIntervalTime(adBean.getData().getClassNum());

                        if (TYPE_YOUDAO.equals(adBean.getData().getType())) {
                            loadYouDaoAD();
                        } else if (addamBanner != null && TYPE_DAM.equals(adBean.getData().getType())) { // 嗒萌广告
                            loadDamAD();
                        } else if (adMiaozeParent != null && TYPE_MIAOZE.equals(adBean.getData().getType())) {  // 淼泽广告
                            Observable.interval(0, 10, TimeUnit.SECONDS)
                                    .take(6)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Long>() {
                                        @Override
                                        public void call(Long aLong) {
                                            Log.e(TAG, "加载Miaoze Banner广告");
//                                            loadMiaozeAd();
                                            loadBanner(adMiaozeParent);
                                        }
                                    });
                        } else if (TYPE_IYUBA.equals(adBean.getData().getType().toLowerCase())) {
                            if (adBean.getData() != null
                                    && !TextUtils.isEmpty(adBean.getData().getStartuppic_Url())
                                    && !TextUtils.isEmpty(adBean.getData().getStartuppic())
                                    ) {

                                loadIyubaAD("http://static3.iyuba.com/dev/" + adBean.getData().getStartuppic(), adBean.getData().getStartuppic_Url());
                            } else {
                                loadYouDaoAD();
                            }
                        } else {
                            loadYouDaoAD();
                        }

                        lastAD = adBean.getData().getType();
                        if (!lastAD.equals(TYPE_DAM) && addamBanner != null) {
                            addamBanner.unLoad();
                        }
//                        if (!lastAD.equals(TYPE_MIAOZE) && miaozeBannerAdView != null) {
//                            adMiaozeParent.removeView(miaozeBannerAdView);
//                            miaozeBannerAdView = null;
//                        }
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
     * 淼泽广告
     */
    /*
    private void loadMiaozeAd() {
        if (context == null) {
            return;
        }
        Log.e(TAG, "加载Miaoze Banner广告");

        setADViewVisibility();
        adMiaozeParent.setVisibility(View.VISIBLE);

        View view = adMiaozeParent;
        final ImageView imageView = (ImageView) view.findViewById(R.id.ad_miaoze_image);
        final TextView title = (TextView) view.findViewById(R.id.ad_miaoze_title);
        final TextView desc = (TextView) view.findViewById(R.id.ad_miaoze_desc);

        //初始化淼擇的API

        //英语四级   s0022001
        //英语六级   s0023001

        AdvancedApi advancedApii = new AdvancedApi(context, "s1a5c176",
                AdSize.Interstitial, null);
        advancedApii.setListener(getAdvancedApiListener(advancedApii, imageView, title, desc));
        advancedApii.register(context, adMiaozeParent);
        // 测试sa3a22b4
    }*/
    private void loadBanner(final ViewGroup viewGroup){

        Log.e(TAG, "加载Miaoze Banner广告");
        viewGroup.removeAllViews();
        BannerAdDsp adMiaozeDSP =new BannerAdDsp(context, new BannerAdDsp.BannerAdDspListener() {
            @Override
            public void onAdReady() {

            }

            @Override
            public void onAdFailed(DspFailInto dspFailInto) {
                loadYouDaoAD();
            }

            @Override
            public void onAdClick(JSONObject jsonObject) {

            }

            @Override
            public void onAdShow(JSONObject jsonObject) {

            }
        });
        adMiaozeDSP.loadBanner("s0022001");
        viewGroup.addView(adMiaozeDSP);
    }
    //淼擇sdk的接口
//    @NonNull
//    private AdvancedApi.AdvancedApiListener getAdvancedApiListener(final AdvancedApi advancedApii,
//                                                                   final ImageView imageView,
//                                                                   final TextView title,
//                                                                   final TextView desc) {
//        return new AdvancedApi.AdvancedApiListener() {
//            @Override
//            public void onAdReady(AdvancedApi advancedApi) {
//                final String titleStr = advancedApi.getTitle();
//                final String descStr = advancedApi.getDesc1();
//                String logoStr = advancedApi.getLogoUrl();
//                String imgStr = advancedApi.getImgUrl();
//                Log.e(TAG, "onAdReady  "
//                        + "\ntitle: " + titleStr
//                        + "\ndesc: " + descStr
//                        + "\nimageUrl: " + imgStr
//                        + "\nlogoUrl: " + logoStr);
//                if (context == null) {
//                    return;
//                }
//                String imageUrl = logoStr;
//                if (TextUtils.isEmpty(imageUrl)) {
//                    imageUrl = imgStr;
//                }
//                final String finalImageUrl = imageUrl;
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (context != null) {
//                            try {
//                                if (context instanceof Activity) {
//                                    if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()) {
//                                        return;
//                                    }
//                                }
//                                title.setText(titleStr);
//                                Glide.with(context).load(finalImageUrl).placeholder(R.drawable.nearby_no_icon2).into(imageView);
//                                desc.setText(descStr);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onAdFailed(JSONObject jsonObject) {
//                Log.e(TAG, "onAdFailed   " + jsonObject.toString());
//                loadYouDaoAD();
//            }
//
//            @Override
//            public void onAdLPFinish(JSONObject jsonObject) {
//                Log.e(TAG, "onAdLPFinish   " + jsonObject.toString());
//            }
//
//            @Override
//            public void onAdClick(JSONObject jsonObject) {
//                Log.e(TAG, "onAdClick   " + jsonObject.toString());
//            }
//
//            @Override
//            public void onAdShow(JSONObject jsonObject) {
//                Log.e(TAG, "onAdShow   " + jsonObject.toString());
//            }
//        };
//    }

    /**
     * 嗒萌广告
     */
    private void loadDamAD() {
        if (context == null) {
            return;
        }
        Log.e(TAG, "加载嗒萌广告");
        Log.e(TAG, "加载嗒萌name:  " + AddamManager.getSDKVersionName());
        // Log.e(TAG, "加载嗒萌code:  " + AddamManager.getSDKVersionCode());

        setADViewVisibility();
        addamBanner.setVisibility(View.VISIBLE);

        if (TYPE_DAM.equals(lastAD)) {
            return;
        }

        addamBanner.load(); // 开始加载
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
        if (addamBanner != null) {
            addamBanner.setVisibility(View.GONE);
        }
    }

    public void destroy() {
        Log.e(TAG, "onDestroy");
        context = null;
        youdaoNative.destroy();
        if (addamBanner != null) {
            addamBanner.setCallback(null);
            addamBanner.unLoad();
        }
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
}
