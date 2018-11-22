/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.write;

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
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.WriteListAdapter;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.sqlite.op.TranslateOp;
import com.iyuba.CET4bible.sqlite.op.WriteOp;
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

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WriteListActivity extends BasisActivity {
    private Context mContext;
    private Button backBtn;
    private ListView list;
    private WriteListAdapter writeListAdapter;
    private String type;
    private TextView title;
    private BaseAdapter mListviewAdapter;
    private RequestParameters mRequestParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        title = findViewById(R.id.title_info);
        type = getIntent().getStringExtra("category");
        if (type.equals("write")) {
            title.setText(R.string.write_text);
            WriteDataManager.Instance().writeList = new WriteOp(mContext)
                    .selectData();
        } else {
            title.setText(R.string.trans_text);
            WriteDataManager.Instance().writeList = new TranslateOp(mContext)
                    .selectData();
        }
        writeListAdapter = new WriteListAdapter(mContext);
        list = findViewById(R.id.list);
        list.setAdapter(writeListAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(mContext, WriteActivity.class);
                if (type.equals("write")) {
                    WriteDataManager.Instance().write = WriteDataManager
                            .Instance().writeList.get(arg2);
                } else {
                    ArrayList<Write> writes = new TranslateOp(mContext)
                            .selectData(writeListAdapter.getItem(arg2).num);
                    if (writes.size() != 1) {
                        Write writeGroup = new Write();
                        StringBuffer text = new StringBuffer(), question = new StringBuffer();
                        for (int i = 0; i < writes.size(); i++) {
                            text.append(i + 1).append(".")
                                    .append(writes.get(i).text).append("\n");
                            question.append(i + 1).append(".")
                                    .append(writes.get(i).question)
                                    .append("\n");
                        }
                        writeGroup.num = writes.get(0).num;
                        writeGroup.index = writes.get(0).index;
                        writeGroup.name = writes.get(0).name;
                        writeGroup.comment = writes.get(0).comment;
                        writeGroup.image = writes.get(0).image;
                        writeGroup.text = text.toString();
                        writeGroup.question = question.toString();
                        WriteDataManager.Instance().write = writeGroup;
                    } else {
                        WriteDataManager.Instance().write = writes.get(0);
                    }
                }
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        initAd();
    }

    private void initAd() {
        mListviewAdapter = new YouDaoAdAdapter(WriteListActivity.this, writeListAdapter,
                YouDaoNativeAdPositioning.newBuilder()
                        .addFixedPosition(4)
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
        list.setAdapter(mListviewAdapter);

        ((YouDaoAdAdapter) mListviewAdapter).loadAds("b932187c3ec9f01c9ef45ad523510edd",
                mRequestParameters);
    }
}
