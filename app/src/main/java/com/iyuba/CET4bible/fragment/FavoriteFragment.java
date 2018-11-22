package com.iyuba.CET4bible.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.FavoriteAdapter;
import com.iyuba.CET4bible.adapter.FavoriteFillInBlankAdapter;
import com.iyuba.CET4bible.adapter.FavoriteParagraphMatchingAdapter;
import com.iyuba.CET4bible.adapter.FavoriteReadingAdapter;
import com.iyuba.CET4bible.adapter.FavoriteTranslateAdapter;
import com.iyuba.CET4bible.sqlite.mode.FillInBlankBean;
import com.iyuba.CET4bible.sqlite.mode.PackInfo;
import com.iyuba.CET4bible.sqlite.mode.ParagraphMatchingBean;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.sqlite.op.FillInBlankOp;
import com.iyuba.CET4bible.sqlite.op.ParagraphMatchingOp;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.CET4bible.sqlite.op.TranslateOp;
import com.iyuba.CET4bible.sqlite.op.WriteOp;
import com.iyuba.CET4bible.util.AdInfoFlowUtil;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.base.BaseFragment;
import com.iyuba.base.BaseRecyclerViewAdapter;
import com.iyuba.base.util.SimpleLineDividerDecoration;
import com.iyuba.core.manager.AccountManager;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * FavoriteFragment
 *
 * @author wayne
 * @date 2017/12/12
 */
public class FavoriteFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List dataList = new ArrayList<>();

    private BaseRecyclerViewAdapter mAdapter;

    private int type;

    ReadingInfoOp readingInfoOp;
    FavoriteUtil favoriteUtil;
    String[] prefix = {"listening", "reading", "translate", "write"};
    List<String> keyList = new ArrayList<>();

    private AdInfoFlowUtil adInfoFlowUtil;

    private List<PackInfo> packInfoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext).setColor(R.color.darkgray));

        favoriteUtil = new FavoriteUtil(type);

        if (type == FavoriteUtil.Type.reading) {
            // 阅读
            setReadingAdapter();
        } else if (type == FavoriteUtil.Type.translate) {
            // 翻译
            setTranslateAdapter();
        } else if (type == FavoriteUtil.Type.write) {
            // 写作
            setWriteAdapter();
        } else if (type == FavoriteUtil.Type.listening) {
            // 听力
            setListeningAdapter();
        } else if (type == FavoriteUtil.Type.fillInBlack) {
            // 完形填空
            setFillInBlankAdapter();
        } else if (type == FavoriteUtil.Type.paragraph) {
            // 段落匹配
            setParagraphMatchingAdapter();
        }
        if (mAdapter == null) {
            return;
        }
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLongClickListener(new BaseRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(View v, final int pos) {
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("确定要删除收藏吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteFavorite(pos);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
        // 插入广告
        adInfoFlowUtil = new AdInfoFlowUtil(mContext, AccountManager.isVip(), new AdInfoFlowUtil.Callback() {
            @Override
            public void onADLoad(List ads) {
                AdInfoFlowUtil.insertAD(dataList, ads, adInfoFlowUtil);
                mAdapter.notifyDataSetChanged();
            }
        });
        adInfoFlowUtil.setAdRequestSize(5).refreshAd();
    }

    private void setParagraphMatchingAdapter() {
        dataList = new ParagraphMatchingOp(mContext).selectData();
        List<String> favoriteList = favoriteUtil.getData();
        List tempList = new ArrayList<>();
        for (Object o : dataList) {
            ParagraphMatchingBean bean = (ParagraphMatchingBean) o;
            if (!favoriteList.contains("paragraph_" + bean.year + bean.index)) {
                tempList.add(bean);
            }
        }
        dataList.removeAll(tempList);

        mAdapter = new FavoriteParagraphMatchingAdapter(mContext, dataList);
    }

    private void setFillInBlankAdapter() {
        dataList = new FillInBlankOp(mContext).selectData();
        List<String> favoriteList = favoriteUtil.getData();
        List tempList = new ArrayList<>();
        for (Object o : dataList) {
            FillInBlankBean bean = (FillInBlankBean) o;
            if (!favoriteList.contains("fillInBlank_" + bean.year + bean.index)) {
                tempList.add(bean);
            }
        }
        dataList.removeAll(tempList);

        mAdapter = new FavoriteFillInBlankAdapter(mContext, dataList);
    }

    private void setListeningAdapter() {
        dataList = new ArrayList();

        List<String> favoriteList = favoriteUtil.getData();

        for (int i = 0; i < favoriteList.size(); i++) {
            // listening + "_" + year + "_" + section + "_" + sound
            String[] array = favoriteList.get(i).split("_");
            if (array[1].compareTo("2015") >= 0) {
                dataList.add(array);
            }
        }

        Collections.sort(dataList, new Comparator<String[]>() {
            @Override
            public int compare(String[] s1, String[] s2) {
                // 按年份和题目顺序排序
                int result = s2[1].compareTo(s1[1]);
                if (result == 0) {
                    String[] aa = s1[3].split("-");
                    String[] bb = s2[3].split("-");
                    for (int i = 0; i < aa.length; i++) {
                        aa[0] = aa[0].length() == 1 ? "0" + aa[0] : aa[0];
                        aa[1] = aa[1].length() == 1 ? "0" + aa[1] : aa[1];
                    }
                    for (int i = 0; i < bb.length; i++) {
                        bb[0] = bb[0].length() == 1 ? "0" + bb[0] : bb[0];
                        bb[1] = bb[1].length() == 1 ? "0" + bb[1] : bb[1];
                    }
                    return (aa[0] + aa[1]).compareTo(bb[0] + bb[1]);
                } else {
                    return result;
                }
            }
        });

        for (int i = 0; i < dataList.size(); i++) {
            String[] key = (String[]) dataList.get(i);
            StringBuilder builder = new StringBuilder();
            for (int xxxx = 0; xxxx < key.length; xxxx++) {
                if (xxxx == 0) {
                    builder.append(key[xxxx]);
                } else {
                    builder.append("_").append(key[xxxx]);
                }
            }
            keyList.add(builder.toString());
        }

        mAdapter = new FavoriteAdapter(mContext, dataList);
    }

    private void setWriteAdapter() {
        dataList = new WriteOp(mContext).selectData();
        filterData(dataList, "write");

        mAdapter = new FavoriteTranslateAdapter(mContext, dataList);
        ((FavoriteTranslateAdapter) mAdapter).setWrite(true);
    }

    private void setTranslateAdapter() {
        dataList = new TranslateOp(mContext).selectData();
        filterData(dataList, "translate");

        mAdapter = new FavoriteTranslateAdapter(mContext, dataList);
        ((FavoriteTranslateAdapter) mAdapter).setWrite(false);
    }

    private void setReadingAdapter() {
        readingInfoOp = new ReadingInfoOp(mContext);

        List<PackInfo> readingList = readingInfoOp.findAll();
        List<PackInfo> tempList = new ArrayList<>();
        List<String> favoriteList = favoriteUtil.getData();

        for (int i = 0; i < favoriteList.size(); i++) {
            String key = favoriteList.get(i);

            for (int k = 0; k < readingList.size(); k++) {
                PackInfo packInfo = readingList.get(k);
                if (key.startsWith("reading_" + packInfo.TitleNum)) {
                    if (!tempList.contains(packInfo)) {
                        tempList.add(packInfo);
                    }
                    break;
                }
            }
        }
        packInfoList = tempList;

        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < tempList.size(); i++) {
            set.add(tempList.get(i).PackName);
        }

        dataList.addAll(set);
        Collections.sort(dataList, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                return rhs.toString().compareTo(lhs.toString());
            }
        });
        mAdapter = new FavoriteReadingAdapter(mContext, (List<String>) dataList);
    }

    private void deleteFavorite(int pos) {
        if (dataList.get(pos) instanceof NativeResponse) {
            return;
        }
        // 翻译和写作
        if (type == FavoriteUtil.Type.translate
                || type == FavoriteUtil.Type.write) {
            favoriteUtil.setFavorite(false, prefix[type] + ((Write) dataList.get(pos)).num + ((Write) dataList.get(pos)).index);
        } else if (type == FavoriteUtil.Type.reading) {
            for (int i = 0; i < packInfoList.size(); i++) {
                PackInfo packInfo = packInfoList.get(i);
                // 根据packName 找到题目，删除所有的收藏
                if (packInfo.PackName.equals(dataList.get(pos))) {
                    for (int k = 0; k < 5; k++) {
                        favoriteUtil.setFavorite(false, "reading_" + packInfo.TitleNum + "_" + (k + 1));
                    }
                }
            }
        } else if (type == FavoriteUtil.Type.listening) {
            favoriteUtil.setFavorite(false, keyList.get(getRealPosition(pos)));
        } else if (type == FavoriteUtil.Type.fillInBlack) {
            favoriteUtil.setFavorite(false, "fillInBlank_" +
                    ((FillInBlankBean) dataList.get(pos)).year + ((FillInBlankBean) dataList.get(pos)).index);
        } else if (type == FavoriteUtil.Type.paragraph) {
            favoriteUtil.setFavorite(false, "paragraph_" +
                    ((ParagraphMatchingBean) dataList.get(pos)).year + ((ParagraphMatchingBean) dataList.get(pos)).index);
        }

        dataList.remove(pos);
        mAdapter.notifyDataSetChanged();
    }

    private int getRealPosition(int pos) {
        int result = 0;
        for (int i = 0; i <= pos; i++) {
            if (dataList.get(pos) instanceof NativeResponse) {
                result += 1;
            }
        }
        return pos - result;
    }

    private void filterData(List dataList, String prefix) {
        List<Write> writeList = dataList;
        List tempList = new ArrayList();
        List<String> favoriteList = favoriteUtil.getData();

        for (int i = 0; i < writeList.size(); i++) {
            Write write = writeList.get(i);
            if (!favoriteList.contains(prefix + write.num + write.index)) {
                tempList.add(write);
            }
        }
        writeList.removeAll(tempList);
        tempList.clear();
    }

    public static Fragment getInstance(int position) {
        Fragment fragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
