package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.SectionA;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.sqlite.op.NewTypeExplainOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionAAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionBAnswerOp;
import com.iyuba.CET4bible.sqlite.op.NewTypeSectionCAnswerOp;
import com.iyuba.CET4bible.util.exam.ExamDataUtil;
import com.iyuba.CET4bible.util.exam.ExamListOp;
import com.iyuba.base.BaseRecyclerViewAdapter;
import com.iyuba.base.util.T;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.test.CetAnswer;
import com.iyuba.core.sqlite.mode.test.CetExplain;
import com.iyuba.core.widget.RoundProgressBar;
import com.youdao.sdk.nativeads.NativeResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FavoriteAdapter
 *
 * @author wayne
 * @date 2017/12/12
 */
public class FavoriteAdapter extends BaseRecyclerViewAdapter<FavoriteAdapter.Holder> {
    private Map<String, Integer> yearImageMap;
    private ExamListOp examListOp;
    /**
     * listening + "_" + year + "_" + section + "_" + sound
     */
    private List mList;

    public FavoriteAdapter(Context mContext, List list) {
        super(mContext);
        examListOp = new ExamListOp(mContext);
        if (BuildConfig.isCET4) {
            yearImageMap = new HashMap<>();
            yearImageMap.put("2017121601", R.drawable.ic_2017121601);
            yearImageMap.put("2017061702", R.drawable.ic_2017061702);
            yearImageMap.put("2017061701", R.drawable.ic_2017061701);
            yearImageMap.put("2016121702", R.drawable.ic_2016121702);
            yearImageMap.put("2016121701", R.drawable.ic_2016121701);
            yearImageMap.put("2016061802", R.drawable.ic_2016062);
            yearImageMap.put("2016061801", R.drawable.ic_2016061);
            yearImageMap.put("2015121903", R.drawable.ic_2015123);
            yearImageMap.put("2015121902", R.drawable.ic_2015122);
            yearImageMap.put("2015121901", R.drawable.ic_2015121);
            yearImageMap.put("2015061303", R.drawable.ic_2015063);
            yearImageMap.put("2015061302", R.drawable.ic_2015062);
            yearImageMap.put("2015061301", R.drawable.ic_2015061);
        } else {
            yearImageMap = new HashMap<>();
            yearImageMap.put("2017061702", R.drawable.ic_2017061702);
            yearImageMap.put("2017061701", R.drawable.ic_2017061701);
            yearImageMap.put("201612172", R.drawable.ic_2016121702);
            yearImageMap.put("201612171", R.drawable.ic_2016121701);
            yearImageMap.put("201606182", R.drawable.ic_2016062);
            yearImageMap.put("201606181", R.drawable.ic_2016061);
            yearImageMap.put("201512193", R.drawable.ic_2015123);
            yearImageMap.put("201512192", R.drawable.ic_2015122);
            yearImageMap.put("201512191", R.drawable.ic_2015121);
            yearImageMap.put("201506133", R.drawable.ic_2015063);
            yearImageMap.put("201506132", R.drawable.ic_2015062);
            yearImageMap.put("201506131", R.drawable.ic_2015061);
        }
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_quesiton_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder curViewHolder, final int position) {
        super.onBindViewHolder(curViewHolder, position);

        if (mList.get(position) instanceof NativeResponse) {
            final NativeResponse response = (NativeResponse) mList.get(position);
            response.recordImpression(curViewHolder.itemView);
            curViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(curViewHolder.itemView);
                }
            });
            Glide.with(mContext).load(response.getMainImageUrl())
                    .error(R.drawable.nearby_no_icon2)
                    .placeholder(R.drawable.nearby_no_icon2)
                    .into(curViewHolder.iv_year);

            curViewHolder.title.setText("(推广) " + response.getTitle());
            curViewHolder.ll_touch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(curViewHolder.itemView);
                }
            });
            curViewHolder.progressBar.setVisibility(View.GONE);
            curViewHolder.image.setVisibility(View.GONE);
            curViewHolder.functionView.setVisibility(View.GONE);
            curViewHolder.tv_question_number.setText("");
            return;
        }

        final String[] data = (String[]) mList.get(position);
        final String section = data[2];
        final String test = data[1];

        Glide.with(mContext).load(ExamDataUtil.getImageUrl(examListOp.findImageByID(test))).into(curViewHolder.iv_year);
        String[] years = new String[3];
        years[0] = test.substring(0, 4);
        years[1] = test.substring(4, 6);
        years[2] = test.substring(test.length() - 1, test.length());

        final StringBuffer sb = new StringBuffer();
        sb.append(years[0]).append(mContext.getString(R.string.year));
        sb.append(years[1]).append(mContext.getString(R.string.month));
        sb.append("(").append(years[2]).append(")");

        curViewHolder.title.setText(sb.toString());
        curViewHolder.tv_question_number.setText(
                String.format("Section %s  第%s题", section, data[3].replace(".mp3", "")));
        curViewHolder.functionView.setVisibility(View.GONE);
        curViewHolder.functionImage.setBackgroundResource(R.drawable.go);

        if (prepareDownload(test) == 1) {
            curViewHolder.image.setVisibility(View.VISIBLE);
        } else {
            curViewHolder.image.setVisibility(View.GONE);
        }
        curViewHolder.progressBar.setVisibility(View.GONE);


        curViewHolder.section_a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.e("TestListAdapter", "test>>>>>" + test);
                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    getData(0, test, data[3]);
                    intent.putExtra("section", "A");
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
                    mContext.startActivity(intent);
                } else {
                    T.showShort(mContext, "请先下载听力");
                }
            }
        });
        curViewHolder.section_b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    getData(1, test, data[3]);
                    intent.putExtra("section", "B");
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
                    mContext.startActivity(intent);
                } else {
                    T.showShort(mContext, "请先下载听力");
                }
            }
        });
        curViewHolder.section_c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (prepareDownload(test) == 1) {
                    Intent intent = new Intent(mContext, SectionA.class);
                    intent.putExtra("section", "C");
                    intent.putExtra("isNewType", true);
                    intent.putExtra("title", sb.toString());
                    getData(2, test, data[3]);
                    mContext.startActivity(intent);
                } else {
                    T.showShort(mContext, "请先下载听力");
                }
            }
        });
        curViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("A".equals(section.toUpperCase())) {
                    curViewHolder.section_a.performClick();
                } else if ("B".equals(section.toUpperCase())) {
                    curViewHolder.section_b.performClick();
                } else if ("C".equals(section.toUpperCase())) {
                    curViewHolder.section_c.performClick();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private ImageView functionImage, iv_year;
        private TextView title;
        private View section_a, section_b, section_c, touch, ll_touch;
        private View functionView;
        private RoundProgressBar progressBar;
        private ImageView image;
        private TextView tv_question_number;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.artical_title);
            tv_question_number = itemView.findViewById(R.id.tv_question_number);
            functionView = itemView.findViewById(R.id.testlist_sub);
            functionImage = itemView.findViewById(R.id.go);
            ll_touch = itemView.findViewById(R.id.ll_touch);
            iv_year = itemView.findViewById(R.id.iv_year);
            touch = itemView.findViewById(R.id.touch);
            section_a = itemView.findViewById(R.id.section_a);
            section_b = itemView.findViewById(R.id.section_b);
            section_c = itemView.findViewById(R.id.section_c);
            progressBar = itemView.findViewById(R.id.roundProgressBar);
            image = itemView.findViewById(R.id.image_downed);
        }
    }


    private void getData(int type, String year, String sound) {
        ListenDataManager.Instance().year = year;
        switch (type) {
            case 0:
                ArrayList<CetAnswer> answerAList = new NewTypeSectionAAnswerOp(mContext).selectData(year);
                ArrayList<CetAnswer> notFavoriteListA = getList(answerAList, sound);
                answerAList.removeAll(notFavoriteListA);
                ListenDataManager.Instance().answerList = answerAList;

                ArrayList<CetExplain> explainAList = new NewTypeExplainOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = getList(explainAList, notFavoriteListA);
                break;
            case 1:
                ArrayList<CetAnswer> answerBList = new NewTypeSectionBAnswerOp(mContext).selectData(year);
                ArrayList<CetAnswer> notFavoriteListB = getList(answerBList, sound);
                answerBList.removeAll(notFavoriteListB);
                ListenDataManager.Instance().answerList = answerBList;

                ArrayList<CetExplain> explainBList = new NewTypeExplainOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = getList(explainBList, notFavoriteListB);
                break;
            case 2:
                ArrayList<CetAnswer> answerCList = new NewTypeSectionCAnswerOp(mContext).selectData(year);
                ArrayList<CetAnswer> notFavoriteListC = getList(answerCList, sound);
                answerCList.removeAll(notFavoriteListC);
                ListenDataManager.Instance().answerList = answerCList;

                ArrayList<CetExplain> explainCList = new NewTypeExplainOp(mContext).selectData(year);
                ListenDataManager.Instance().explainList = getList(explainCList, notFavoriteListC);
                break;
            default:
                break;
        }
    }

    /**
     * 根据听力文件名，去掉非收藏的题目
     */
    private ArrayList<CetAnswer> getList(ArrayList<CetAnswer> answerAList, String sound) {
        ArrayList<CetAnswer> notFavorite = new ArrayList<>();
        for (int i = 0; i < answerAList.size(); i++) {
            if (!answerAList.get(i).sound.equals(sound)) {
                notFavorite.add(answerAList.get(i));
            }
        }
        return notFavorite;
    }

    /**
     * 根据id题号，去掉非收藏的题目
     */
    private ArrayList<CetExplain> getList(ArrayList<CetExplain> explainList, ArrayList<CetAnswer> notFavoriteList) {
        ArrayList<CetExplain> notFavorite = new ArrayList<>();
        for (int i = 0; i < explainList.size(); i++) {
            boolean x = false;
            for (int k = 0; k < notFavorite.size(); k++) {
                if (notFavoriteList.get(k).id.equals(explainList.get(i).id)) {
                    x = true;
                    break;
                }
            }
            if (x) {
                notFavorite.add(explainList.get(i));
            }
        }
        explainList.removeAll(notFavorite);
        return explainList;
    }

    private int prepareDownload(String year) {
        String fileNoAppend = Constant.videoAddr + year + ".cet4";
        String folder = Constant.videoAddr + year;
        File file1 = new File(folder);
        File file2 = new File(fileNoAppend);
        if (file1.exists()) {
            if (file1.list().length <= 0) {
                file1.delete();
                return 0;
            }
            return 1;
            // complete
        } else if (file2.exists()) {
            return 2;
            // downloading
        } else {
            return 0;
            // no down
        }
    }
}
