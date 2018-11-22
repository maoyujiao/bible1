package com.iyuba.CET4bible.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;

import com.iyuba.base.util.L;
import com.youdao.sdk.nativeads.NativeAds;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoMultiNative;

import java.util.ArrayList;
import java.util.List;

/**
 * AdYoudaoMultiUtil
 *
 * @author wayne
 * @date 2017/11/28
 */
public class AdYoudaoMultiUtil {
    private int requestSize = 6;
    private YouDaoMultiNative multiNative;
    private RequestParameters mRequestParameters;
    private List<NativeResponse> adList = new ArrayList<>();
    private Callback mCallback;
    private boolean isVip = false;

    public interface Callback {
        void onADLoad();
    }

    public AdYoudaoMultiUtil(Context context, boolean vip, Callback callback) {
        this.isVip = vip;
        if (isVip) {
            return;
        }
        this.mCallback = callback;
        multiNative = new YouDaoMultiNative(context, "3438bae206978fec8995b280c49dae1e", new YouDaoMultiNative.YouDaoMultiNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAds nativeAds) {
                List<NativeResponse> responses = nativeAds.getNativeResponses();
                int count = responses.size();
                for (int i = 0; i < count; i++) {
                    adList.add(responses.get(i));
                    L.e(responses.get(i).toString());
                }
                mCallback.onADLoad();
            }

            @Override
            public void onNativeFail(NativeErrorCode nativeErrorCode) {
                L.e("youDao: onNativeFail :  " + nativeErrorCode.name() + "  --  Message : " + nativeErrorCode.toString());
            }
        });
    }

    public List<NativeResponse> getAdList() {
        return adList;
    }

    public void requestAd() {
        if (isVip) {
            return;
        }
        mRequestParameters = new RequestParameters.Builder().build();
        // 发起广告请求, 代表一次获取的广告个数
        multiNative.makeRequest(mRequestParameters, requestSize);
    }

    public void refreshAd() {
        if (isVip) {
            return;
        }
        if (mRequestParameters == null) {
            requestAd();
        } else {
            multiNative.refreshRequest(mRequestParameters, requestSize);
        }
    }

    public void destroy() {
        if (isVip) {
            return;
        }
        multiNative.destroy();
    }

    public static int getItemViewType(int position, int adListSize, int defaultValue) {
        int aa = position % 10;
        int page = position / 10;
        // 如果是广告位
        if (aa == 2) {
            // 如果有广告
            if (adListSize >= (page * 3) + 1) {
                return 1;
            }
        }
        if (aa == 6) {
            // 如果有广告
            if (adListSize >= (page * 3) + 2) {
                return 1;
            }
        }
        if (aa == 9) {
            // 如果有广告
            if (adListSize >= (page * 3) + 3) {
                return 1;
            }
        }
        return defaultValue;
    }

    public static int getRealPosition(int position, ListAdapter adapter) {
        // 如果是广告位
        if (adapter.getItemViewType(position) == 1) {
            int realPosition = -1;
            for (int i = 0; i <= position; i++) {
                if (adapter.getItemViewType(i) == 1) {
                    realPosition += 1;
                }
            }
            return realPosition;
        } else {
            int realPosition = -1;
            for (int i = 0; i <= position; i++) {
                if (adapter.getItemViewType(i) == 0) {
                    realPosition += 1;
                }
            }
            return realPosition;
        }
    }

    public static int getRealPosition(int position, RecyclerView.Adapter adapter) {

        // 如果是广告位
        if (adapter.getItemViewType(position) == 1) {
            int realPosition = -1;
            for (int i = 0; i <= position; i++) {
                if (adapter.getItemViewType(i) == 1) {
                    realPosition += 1;
                }
            }
            return realPosition;
        } else {
            int realPosition = -1;
            for (int i = 0; i <= position; i++) {
                if (adapter.getItemViewType(i) == 0) {
                    realPosition += 1;
                }
            }
            return realPosition;
        }
    }

    public static int getCount(int dataSize, int adSize) {
        if (dataSize == 0) {
            return 0;
        }
        int aa = dataSize % 7;
        int adPage = adSize % 3;
        if (aa >= adPage) {
            return dataSize + adPage * 3;
        } else {
            return dataSize + aa * 3;
        }
    }
}
