package com.iyuba.CET4bible.strategy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.iyuba.CET4bible.R;
import com.iyuba.headlinelibrary.Constant;
import com.iyuba.headlinelibrary.ui.title.MixNative;
import com.iyuba.sdk.data.iyu.IyuNative;
import com.iyuba.sdk.data.youdao.YDNative;
import com.iyuba.sdk.nativeads.NativeAdPositioning;
import com.iyuba.sdk.nativeads.NativeAdRenderer;
import com.iyuba.sdk.nativeads.NativeRecyclerAdapter;
import com.iyuba.sdk.nativeads.NativeViewBinder;
import com.youdao.sdk.nativeads.RequestParameters;

import java.util.EnumSet;

public final class ContentMixStrategy extends ContentNonVipStrategy {

    private int mHolderType;
    private int mStart;
    private int mInterval;
    private int[] mStreamTypes;

    public ContentMixStrategy(int start, int interval, int[] streamTypes, int holderType) {
        mStart = start;
        mInterval = interval;
        mStreamTypes = streamTypes;
        mHolderType = holderType;
    }

    @Override
    public RecyclerView.Adapter buildWorkAdapter(Context context, RecyclerView.Adapter originalAdapter) {
        NativeAdPositioning.ClientPositioning cp = new NativeAdPositioning.ClientPositioning();
        cp.addFixedPosition(mStart);
        cp.enableRepeatingPositions(mInterval);
        NativeRecyclerAdapter nativeAdapter = new NativeRecyclerAdapter(context, originalAdapter, cp);

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);
        RequestParameters requestParameters = new RequestParameters.Builder()
                .location(null)
                .keywords(null)
                .desiredAssets(desiredAssets)
                .build();
        YDNative ydNative = new YDNative(context, Constant.YOUDAO_STREAM_ID, requestParameters);

        IyuNative iyuNative = new IyuNative(context,
                com.iyuba.configation.Constant.APP_CONSTANT.APPID());

        MixNative mixNative = new MixNative(ydNative, iyuNative);
        mixNative.setStreamSource(mStreamTypes);

        nativeAdapter.setAdSource(mixNative);
        nativeAdapter.setAdViewTypeMax(56);

        return nativeAdapter;
    }

    @Override
    public void init(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        NativeRecyclerAdapter nativeAdapter = (NativeRecyclerAdapter) adapter;
        NativeAdRenderer nativeAdRenderer = makeAdRenderer();
        nativeAdapter.registerAdRenderer(nativeAdRenderer);

        recyclerView.setAdapter(adapter);
    }

    private NativeAdRenderer makeAdRenderer() {
        NativeViewBinder viewBinder ;
        switch (mHolderType) {
            case HolderType.HOME:
                viewBinder = new NativeViewBinder.Builder(R.layout.native_ad_row)
                        .titleId(R.id.native_title)
                        .mainImageId(R.id.native_main_image)
                        .build();
                return new NativeAdRenderer(viewBinder);
            case HolderType.MIDDLE: {
                viewBinder = new NativeViewBinder.Builder(com.iyuba.headlinelibrary.R.layout.headline_youdao_ad_row_middle)
                        .titleId(com.iyuba.headlinelibrary.R.id.headline_native_title)
                        .iconImageId(com.iyuba.headlinelibrary.R.id.headline_native_icon_image)
                        .mainImageId(com.iyuba.headlinelibrary.R.id.headline_native_ad_image)
                        .build();
                return new NativeAdRenderer(viewBinder);
            }
            case HolderType.SMALL:
            default: {
                viewBinder = new NativeViewBinder.Builder(com.iyuba.headlinelibrary.R.layout.headline_youdao_ad_row_small)
                        .titleId(com.iyuba.headlinelibrary.R.id.native_title)
                        .mainImageId(com.iyuba.headlinelibrary.R.id.native_main_image)
                        .build();
                return new NativeAdRenderer(viewBinder);
            }
        }
    }

    @Override
    public int getOriginalAdapterPosition(RecyclerView.Adapter adapter, int position) {
        return ((NativeRecyclerAdapter) adapter).getOriginalPosition(position);
    }

    @Override
    public void loadAd(RecyclerView.Adapter adapter) {
        ((NativeRecyclerAdapter) adapter).loadAds();
    }

    @Override
    public void refreshAd(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        ((NativeRecyclerAdapter) adapter).refreshAds();
    }

}
