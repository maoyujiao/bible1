/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ReadingAdapter;
import com.iyuba.CET4bible.manager.ReadingDataManager;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.RequestParameters.NativeAdAsset;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.nativeads.YouDaoAdAdapter;
import com.youdao.sdk.nativeads.YouDaoNativeAdPositioning;
import com.youdao.sdk.nativeads.YouDaoNativeAdRenderer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Remain extends BasisActivity implements OnItemClickListener {
    private Context mContext;
    private Button backBtn;
    private ListView iv_testyear;
    private ReadingInfoOp readingInfoOp;
    private List<String> packNames = new ArrayList<String>();
    private BaseAdapter mListviewAdapter, adapter;
    private RequestParameters mRequestParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        mContext = this;
        readingInfoOp = new ReadingInfoOp(mContext);
        CrashApplication.getInstance().addActivity(this);
        initData();
        initView();
        initAd();
    }

    private void initData() {
        if (!(ReadingDataManager.getInstance().packNames.size() > 0)) {
            ReadingDataManager.getInstance().packNames = readingInfoOp.findPackName();
        }
        packNames = ReadingDataManager.getInstance().packNames;
    }

    private void initView() {
        iv_testyear = findViewById(R.id.iv_testyear);
        adapter = new ReadingAdapter(packNames, mContext);
        iv_testyear.setAdapter(adapter);
        iv_testyear.setOnItemClickListener(this);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initAd() {
        mListviewAdapter = new YouDaoAdAdapter(Remain.this, adapter,
                YouDaoNativeAdPositioning.newBuilder()
                        .addFixedPosition(3)
                        .build());
        // 绑定界面组件与广告参数的映射关系，用于渲染广告
        final YouDaoNativeAdRenderer adRenderer = new YouDaoNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_row)
                        .titleId(R.id.native_title)
                        .mainImageId(R.id.native_main_image)
                        .build());
        ((YouDaoAdAdapter) mListviewAdapter).registerAdRenderer(adRenderer);
        final Location location = null;
        final String keywords = null;
        // 声明app需要的资源，这样可以提供高质量的广告，也会节省网络带宽
        final EnumSet<NativeAdAsset> desiredAssets = EnumSet.of(
                NativeAdAsset.TITLE, NativeAdAsset.TEXT,
                NativeAdAsset.ICON_IMAGE, NativeAdAsset.MAIN_IMAGE,
                NativeAdAsset.CALL_TO_ACTION_TEXT);
        mRequestParameters = new RequestParameters.Builder()
                .location(location).keywords(keywords)
                .desiredAssets(desiredAssets).build();
        iv_testyear.setAdapter(mListviewAdapter);

        ((YouDaoAdAdapter) mListviewAdapter).loadAds("b932187c3ec9f01c9ef45ad523510edd",
                mRequestParameters);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(mContext, ReadingTest.class);
        String packName = packNames.get(position);
        intent.putExtra("PackName", packName);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        readingInfoOp.close();
    }

}
