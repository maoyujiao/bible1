package com.iyuba.CET4bible.util;

import android.content.Context;

import com.iyuba.base.util.L;
import com.youdao.sdk.nativeads.NativeAds;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.YouDaoMultiNative;

import java.util.ArrayList;
import java.util.List;

/**
 * AdInfoFlowUtil
 *
 * @author wayne
 * @date 2017/11/30
 */
public class AdInfoFlowUtil {
    private final int START_POSITION = -2;
    private int requestSize = 5;
    private int videoRequestSize = 1;

    private boolean supportVideo = false;

    private YouDaoMultiNative multiNative;
    private YouDaoMultiNative multiVideoNative;

    private List allAds = new ArrayList();
    public List adList = new ArrayList<>();
    public List videoAdList = new ArrayList<>();

    private Callback mCallback;
    private boolean isVip = false;
    public int lastAdPosition = START_POSITION;
    private boolean isAdReponse = false;
    private boolean isVideoAdReponse = false;

    public static synchronized int insertAD(List mDataList, List ads, AdInfoFlowUtil util) {
        if (mDataList.size() > util.lastAdPosition) {
            int index = util.lastAdPosition;
            // 有广告，且有足够的数据
            while (ads.size() > 0 && (mDataList.size() >= index + 4)) {
                index += 4;
                mDataList.add(index, ads.get(0));
                ads.remove(0);
            }
            util.lastAdPosition = mDataList.size() - 2;
        }
        return util.lastAdPosition;
    }

    private static int insertVideoAd(List mDataList, AdInfoFlowUtil util) {
        List ads;//= new ArrayList();
        ads = util.adList;

        List videoAds;//= new ArrayList();
        videoAds = util.videoAdList;

        if (mDataList.size() > util.lastAdPosition) {
            int index = util.lastAdPosition;
            int count = 0;
            // 有广告，且有足够的数据
            while (((ads.size() + videoAds.size()) > 0) && (mDataList.size() >= index + 4)) {
                index += 4;
                if ((count == 1 || count == 3) && videoAds.size() > 0) {
                    mDataList.add(index, videoAds.get(0));
                    videoAds.remove(0);
                    L.e("=== ad position =11111=  " + index);
                } else if (ads.size() > 0) {
                    mDataList.add(index, ads.get(0));
                    ads.remove(0);
                    L.e("=== ad position =22222=  " + index);
//                } else if (videoAds.size() > 0) {
//                    mDataList.add(index, videoAds.get(0));
//                    videoAds.remove(0);
//                    L.e("=== ad position =33333=  " + index);
                }
                count += 1;
            }
            util.lastAdPosition = mDataList.size() - 2;
        }
        return util.lastAdPosition;
    }

    public interface Callback {
        void onADLoad(List ads);
    }

    public AdInfoFlowUtil(Context context, boolean vip, final Callback callback) {
        this.isVip = vip;
        this.mCallback = callback;
        // 47a9a23756450e1f3c79aa21899885eb  慢速原生信息流视频样式
        // 3438bae206978fec8995b280c49dae1e
        multiNative = new YouDaoMultiNative(context, "3438bae206978fec8995b280c49dae1e", new YouDaoMultiNative.YouDaoMultiNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAds nativeAds) {
                List<NativeResponse> responses = nativeAds.getNativeResponses();
                int count = responses.size();
                for (int i = 0; i < count; i++) {
                    adList.add(responses.get(i));
                    L.e(responses.get(i).toString());
                }
                isAdReponse = true;
                L.e("info flow youDao ad  onNativeLoad ------");
                callback(callback);
            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {
                L.e("info flow youDao: onNativeFail :  " + nativeErrorCode.name() + "  --  Message : " + nativeErrorCode.toString());
                isAdReponse = true;
                callback(callback);
            }
        });
        multiVideoNative = new YouDaoMultiNative(context, "47a9a23756450e1f3c79aa21899885eb", new YouDaoMultiNative.YouDaoMultiNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAds nativeAds) {
                List<NativeResponse> responses = nativeAds.getNativeResponses();
                int count = responses.size();
                for (int i = 0; i < count; i++) {
                    videoAdList.add(responses.get(i));
                    L.e(responses.get(i).toString());
                }
                L.e("info flow youDao ad  onNativeLoad ------");
                isVideoAdReponse = true;
                callback(callback);
            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {
                L.e("info flow youDao: onNativeFail :  " + nativeErrorCode.name() + "  --  Message : " + nativeErrorCode.toString());
                isVideoAdReponse = true;
                callback(callback);
            }
        });
    }

    private void callback(Callback callback) {
        if ((isAdReponse && isVideoAdReponse) || (isAdReponse && !supportVideo)) {
            List list = new ArrayList();
            if (adList.size() > 0 && videoAdList.size() == 0) {
                list.addAll(adList);
            } else if (adList.size() == 0 && videoAdList.size() > 0) {
                list.addAll(videoAdList);
            } else if (adList.size() > 0 && videoAdList.size() > 0) {
                try {
                    while (adList.size() > 0 || videoAdList.size() > 0) {
                        if (adList.size() > 0) {
                            list.add(adList.get(0));
                            adList.remove(0);
                        }
                        if (videoAdList.size() > 0) {
                            list.add(videoAdList.get(0));
                            videoAdList.remove(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            allAds.addAll(list);
            callback.onADLoad(list);
        }
    }

    public List getAdList() {
        return allAds;
    }

    public void refreshAd() {
        if (isVip) {
            return;
        }
        L.e("info flow refresh ad ------");
        isAdReponse = false;
        isVideoAdReponse = false;
        adList.clear();
        videoAdList.clear();
        multiNative.refreshRequest(requestSize);
        if (supportVideo) {
            multiVideoNative.makeRequest(videoRequestSize);
        }
    }

    public void destroy() {
        multiNative.destroy();
        multiVideoNative.destroy();
    }

    public AdInfoFlowUtil resetLastPosition() {
        lastAdPosition = START_POSITION;
        return this;
    }

    public void reset() {
        resetLastPosition();
        adList.clear();
        videoAdList.clear();
        allAds.clear();
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
        if (!isVip) {
            lastAdPosition = START_POSITION;
        }
    }

    public AdInfoFlowUtil setAdRequestSize(int size) {
        this.requestSize = size;
        return this;
    }

    public AdInfoFlowUtil setVideoAdRequestSize(int size) {
        this.videoRequestSize = size;
        return this;
    }

    public AdInfoFlowUtil setSupportVideo(boolean supportVideo) {
        this.supportVideo = supportVideo;
        if (supportVideo) {
            return setAdRequestSize(1);
        } else {
            return setAdRequestSize(5);
        }
    }
}
