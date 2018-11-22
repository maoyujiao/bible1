package com.iyuba.CET4bible.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ParagraphItemAdapter;
import com.iyuba.CET4bible.event.ParagraphEvent;
import com.iyuba.CET4bible.fragment.ParagraphMatchingQuesCountFragment;
import com.iyuba.CET4bible.fragment.ParagraphMatchingQuesFragment;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.sqlite.mode.ParagraphMatchingBean;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.base.BaseActivity;
import com.iyuba.base.util.SimpleLineDividerDecoration;
import com.iyuba.configation.Constant;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.TouristUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * ParagraphMatchingActivity
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphMatchingActivity extends BaseActivity {
    RecyclerView recyclerView;
    ParagraphItemAdapter adapter;
    List<String> mOriginalList;
    List<String> mTranslationList;
    private boolean isShowAnswer = false;
    ViewPager viewPager;
    SparseIntArray selectedQuestionArray = new SparseIntArray();

    ParagraphMatchingBean data;
    String[] questions;
    // 学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pargraph_matching);

        data = (ParagraphMatchingBean) getIntent().getSerializableExtra("data");
        questions = data.question.split("[+][+]");

        findView(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvTitle = findView(R.id.title_info);
        tvTitle.setText("段落匹配");

        recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new SimpleLineDividerDecoration(mContext));
        mOriginalList = new ArrayList<>();
        mTranslationList = new ArrayList<>();
        adapter = new ParagraphItemAdapter(mContext, mOriginalList, mTranslationList);
        recyclerView.setAdapter(adapter);

        EventBus.getDefault().register(this);
        for (int i = 0; i < 15; i++) {
            selectedQuestionArray.put(i, -1);
        }

        String[] originals = data.original.split("[+][+]");
        String[] translations = data.translation.split("[+][+]");
        mOriginalList.addAll(Arrays.asList(originals));
        mTranslationList.addAll(Arrays.asList(translations));
        adapter.notifyDataSetChanged();

        viewPager = findView(R.id.viewpager);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return questions.length + 1;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == getCount() - 1) {
                    ParagraphMatchingQuesCountFragment fragment = new ParagraphMatchingQuesCountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);
                    bundle.putInt("count", getCount() - 1);
                    bundle.putBoolean("showAnswer", isShowAnswer);
                    fragment.setArguments(bundle);
                    return fragment;
                }
                ParagraphMatchingQuesFragment fragment = new ParagraphMatchingQuesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                bundle.putString("question", questions[position]);
                bundle.putInt("pos", position);
                bundle.putInt("count", getCount() - 1);
                bundle.putBoolean("showAnswer", isShowAnswer);
                fragment.setArguments(bundle);
                return fragment;
            }
        });


        studyRecord();
        favorite();
    }

    private void favorite() {
        CheckBox cbFavorite = findView(R.id.cb_favorite);
        final FavoriteUtil favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.paragraph);
        final String key = "paragraph_" + data.year + data.index;
        cbFavorite.setChecked(favoriteUtil.isFavorite(key));
        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favoriteUtil.setFavorite(isChecked, key);
            }
        });
    }

    private void studyRecord() {
        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = "1";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = data.year;
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        studyRecordInfo.TestNumber = "010" + data.index;
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        if (System.currentTimeMillis() - startTime < 1000 * 15) {
            e("--- 时间不够15秒 ---");
            super.onDestroy();
            return;
        }
        studyRecordInfo.EndTime = deviceInfo.getCurrentTime();
        try {
            if (AccountManager.Instace(mContext).checkUserLogin() && !TouristUtil.isTourist()) {
                if (!TextUtils.isEmpty(studyRecordInfo.uid) && !"0".equals(studyRecordInfo.uid)) {
                    Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, "3",
                            data.original.split(" ").length + ""), new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerSubmit(ParagraphEvent.ParagraphAnswerSubmitEvent event) {
        // 提交答案后显示翻译按钮，viewpager回到第一题
        isShowAnswer = true;
        viewPager.setCurrentItem(0);
        adapter.setShowAnswer(isShowAnswer);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphPageClick(ParagraphEvent.ParagraphPageClickEvent event) {
        // 上一页下一页
        viewPager.setCurrentItem(viewPager.getCurrentItem() + event.pos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphPageChangeed(ParagraphEvent.ParagraphQuesFragmentChangeEvent event) {
        // 跳转到制定页面
        viewPager.setCurrentItem(event.pos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParagraphAnswerClick(ParagraphEvent.ParagraphAnswerClickEvent event) {
        // 做题。点击的某个选项。记录数据，并发送最新数据到页面。
        if (event.selected) {
            selectedQuestionArray.put(event.fragmentPos, event.questionPos);
        } else {
            selectedQuestionArray.delete(event.fragmentPos);
        }

        onRequestArray(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRequestArray(ParagraphEvent.ParagraphRequestSelectedArray event) {
        // 发送最新答题数据到页面数据
        EventBus.getDefault().post(new ParagraphEvent.ParagraphAnswerRefreshEvent(selectedQuestionArray));
    }
}
