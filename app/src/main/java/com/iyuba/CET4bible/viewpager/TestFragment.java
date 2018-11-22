package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.ReadingTest;
import com.iyuba.CET4bible.adapter.NewTypeAdapter;
import com.iyuba.CET4bible.adapter.ReadingAdapter;
import com.iyuba.CET4bible.adapter.WriteListAdapter;
import com.iyuba.CET4bible.listening.TestListActivity;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.manager.ReadingDataManager;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.CET4bible.sqlite.op.TranslateOp;
import com.iyuba.CET4bible.sqlite.op.WriteOp;
import com.iyuba.CET4bible.write.WriteActivity;
import com.iyuba.configation.ConfigManager;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.nativeads.YouDaoAdAdapter;
import com.youdao.sdk.nativeads.YouDaoNativeAdPositioning;
import com.youdao.sdk.nativeads.YouDaoNativeAdRenderer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TestFragment extends Fragment implements OnItemClickListener {
    private static String[] CATE_STRINGS;
    private int mContent;
    private Context mContext;
    private View root;
    private ListView listView;
    private ReadingInfoOp readingInfoOp;
    private List<String> packNames;
    private String type;
    private NewTypeAdapter listenAdapter;
    private ReadingAdapter readingAdapter;
    private WriteListAdapter translateAdapter;
    private WriteListAdapter writeAdapter;
    private YouDaoAdAdapter youdaoAdapter;
    private RequestParameters mRequestParameters;

    public static TestFragment newInstance(Context context, int content) {
        TestFragment fragment = new TestFragment();
        fragment.mContent = content;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.test_fragment, container, false);
        readingInfoOp = new ReadingInfoOp(mContext);
        initData();
        CATE_STRINGS = getResources().getStringArray(R.array.test_tpye);
        init();
        return root;
    }

    private void initData() {
        if (!(ReadingDataManager.getInstance().packNames.size() > 0)) {
            ReadingDataManager.getInstance().packNames = readingInfoOp.findPackName();
        }
        packNames = ReadingDataManager.getInstance().packNames;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        if (ConfigManager.Instance().loadInt("isvip") == 0) {
            setAdView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    private void init() {
        listView = root.findViewById(R.id.list);
        type = CATE_STRINGS[mContent];
        if (type.equals("听力")) {
            listenAdapter = new NewTypeAdapter(mContext);
            listenAdapter.setList(ListenDataManager.getTestNewType());
            listView.setAdapter(listenAdapter);
            View footer = View.inflate(mContext, R.layout.listen_foot, null);
            listView.addFooterView(footer);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, TestListActivity.class);
                    startActivity(intent);
                }
            });
        } else if (type.equals("阅读")) {
            readingAdapter = new ReadingAdapter(packNames, mContext);
            listView.setAdapter(readingAdapter);
        } else if (type.equals("翻译")) {
            WriteDataManager.Instance().writeList = new TranslateOp(mContext).selectData();
            translateAdapter = new WriteListAdapter(mContext);
            listView.setAdapter(translateAdapter);
        } else if (type.equals("写作")) {
            WriteDataManager.Instance().writeList = new WriteOp(mContext).selectData();
            writeAdapter = new WriteListAdapter(mContext);
            listView.setAdapter(writeAdapter);
        }
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Intent intent = new Intent();
        if (type.equals("阅读")) {
            if (youdaoAdapter != null && youdaoAdapter.getCount() > packNames.size()) {
                if (position > 2) {
                    position--;
                }
            }
            String packName = packNames.get(position);
            intent.putExtra("PackName", packName);
            intent.setClass(mContext, ReadingTest.class);
            startActivity(intent);
        } else if (type.equals("翻译")) {
            WriteDataManager.Instance().writeList = new TranslateOp(mContext)
                    .selectData();
            if (youdaoAdapter != null && youdaoAdapter.getCount() > WriteDataManager.Instance().writeList.size()) {
                if (position > 2) {
                    position--;
                }
            }
            intent.setClass(mContext, WriteActivity.class);
            intent.putExtra("type", "translate");
            ArrayList<Write> writes = new TranslateOp(mContext)
                    .selectData(translateAdapter.getItem(position).num);
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
            intent.putExtra("write", WriteDataManager.Instance().write);
            startActivity(intent);

        } else if (type.equals("写作")) {
            WriteDataManager.Instance().writeList = new WriteOp(mContext).selectData();

            if (youdaoAdapter != null && youdaoAdapter.getCount() > WriteDataManager.Instance().writeList.size()) {
                if (position > 2) {
                    position--;
                }
            }
            intent.setClass(mContext, WriteActivity.class);
            intent.putExtra("type", "write");
            WriteDataManager.Instance().write = WriteDataManager.Instance().writeList.get(position);
            intent.putExtra("write", WriteDataManager.Instance().write);

            startActivity(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        readingInfoOp.close();
    }

    /**
     * 接入有道广告
     * 功能：set ListView for year
     */
    private void setAdView() {
        if (type.equals("听力")) {
            youdaoAdapter = new YouDaoAdAdapter(mContext, listenAdapter,
                    YouDaoNativeAdPositioning.newBuilder()
                            .addFixedPosition(2)
                            .build());
        } else if (type.equals("阅读")) {
            youdaoAdapter = new YouDaoAdAdapter(mContext, readingAdapter,
                    YouDaoNativeAdPositioning.newBuilder()
                            .addFixedPosition(2)
                            .build());
        } else if (type.equals("翻译")) {
            youdaoAdapter = new YouDaoAdAdapter(mContext, translateAdapter,
                    YouDaoNativeAdPositioning.newBuilder()
                            .addFixedPosition(2)
                            .build());
        } else if (type.equals("写作")) {
            youdaoAdapter = new YouDaoAdAdapter(mContext, writeAdapter,
                    YouDaoNativeAdPositioning.newBuilder()
                            .addFixedPosition(2)
                            .build());
        }

        // 绑定界面组件与广告参数的映射关系，用于渲染广告
        final YouDaoNativeAdRenderer adRenderer = new YouDaoNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_row)
                        .titleId(R.id.native_title)
                        .mainImageId(R.id.native_main_image)
                        .build());
        youdaoAdapter.registerAdRenderer(adRenderer);
        final Location location = null;
        final String keywords = null;
        // 声明app需要的资源，这样可以提供高质量的广告，也会节省网络带宽
        final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE, RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE, RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);
        mRequestParameters = new RequestParameters.Builder()
                .location(location).keywords(keywords)
                .desiredAssets(desiredAssets).build();
        listView.setAdapter(youdaoAdapter);
        youdaoAdapter.loadAds("5542d99e63893312d28d7e49e2b43559",
                mRequestParameters);
    }
}
