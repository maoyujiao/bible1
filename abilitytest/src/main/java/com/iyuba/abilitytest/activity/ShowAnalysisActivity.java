package com.iyuba.abilitytest.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.adapter.ShowAnalysisAdapter;
import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.widget.AbilityTestViewPager;
import com.iyuba.abilitytest.widget.WaittingDialog;
import com.iyuba.configation.Constant;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.RoundProgressBar;
import com.iyuba.core.widget.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShowAnalysisActivity extends AppBaseActivity {
    private Context mContext;
    private String mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + DataManager.getInstance().lessonId;
    private ArrayList<AbilityQuestion.TestListBean> mQuesList;//所有的题目
    private ArrayList<AbilityQuestion.TestListBean> mQuesListLoaded;//已经加载的题目
    private AbilityTestViewPager vp_analysis_test;
    private ImageButton btn_nav_sub;
    private ImageButton ibtn_title_pre;//pre title
    private ImageButton ibtn_title_next;//next title
    private RoundProgressBar rpb_sound_play;//sound Play
    private TextView tv_current_percent;//cur title num
    private TextView tv_titlebar_sub;
    private Player player;
    private int curPosition = 0;
    private Timer mTimer;
    private CustomDialog mWaittingDialog;
    private int fisrtCount;//首次加载的条数
    private int count = 5;//每次加载的条数
    private int times = 0;//加载的次数
    private boolean lastData = false;//加载完成之后是否还要加载
    private ShowAnalysisAdapter adapter;
    private ArrayList<View> mViewItems;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_showanalysis;
    }

    @Override
    protected void initVariables() {
        this.mContext = this;
        mWaittingDialog = new WaittingDialog().wettingDialog(mContext);//waiting dialog
        player = new Player(mContext, new OnPlayStateChangedListener() {
            @Override
            public void playCompletion() {
                rpb_sound_play.setBackgroundResource(R.mipmap.audio_play);
            }

            @Override
            public void playFaild() {
                rpb_sound_play.setBackgroundResource(R.mipmap.audio_play);
            }
        });
        mQuesList = DataManager.getInstance().practiceList;
        mQuesListLoaded = new ArrayList<>();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        vp_analysis_test = findView(R.id.vp_analysis_test);
        btn_nav_sub = findView(R.id.btn_nav_sub);
        tv_titlebar_sub = findView(R.id.tv_titlebar_sub);
        ibtn_title_pre = findView(R.id.ibtn_title_pre);
        tv_current_percent = findView(R.id.tv_current_percent);
        ibtn_title_next = findView(R.id.ibtn_title_next);
        rpb_sound_play = findView(R.id.rpb_sound_play);
        tv_titlebar_sub.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void loadData() {
        btn_nav_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAnalysisActivity.this.finish();
            }
        });

        mWaittingDialog.show();
        long time1 = System.currentTimeMillis();

        mViewItems = new ArrayList<>();
        if (mQuesList.size() >= count * 2) {
            fisrtCount = count;
        } else {
            fisrtCount = mQuesList.size();
        }
        for (int i = 0; i < fisrtCount; i++) {
            LogUtils.e(TAG, "fist fist:   ");
            mViewItems.add(getLayoutInflater().inflate(R.layout.item_analysis, null));
            mQuesListLoaded.add(mQuesList.get(i));
        }
        long time2 = System.currentTimeMillis();
        LogUtils.e(TAG, "time2: " + (time2 - time1));
        String category = "";
        try {
            category = getIntent().getStringExtra("testCategory");
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new ShowAnalysisAdapter(mContext, category);
        adapter.setData(mQuesListLoaded, mViewItems);
        long time3 = System.currentTimeMillis();
        LogUtils.e(TAG, "time3: " + (time3 - time2));
        adapter.setPlayer(player);
        long time4 = System.currentTimeMillis();
        LogUtils.e(TAG, "time4: " + (time4 - time3));
        vp_analysis_test.setAdapter(adapter);
        long time5 = System.currentTimeMillis();
        LogUtils.e(TAG, "time5: " + (time5 - time4));
        mWaittingDialog.dismiss();
        vp_analysis_test.setOnPageChangeListener(pageChangeListener());
        ibtn_title_pre.setOnClickListener(preTitleClickListener());
        ibtn_title_next.setOnClickListener(nextTitleClickListener());
        rpb_sound_play.setOnClickListener(playAudioClickListener());
        if (mQuesList.get(0).getSounds() == null || mQuesList.get(0).getSounds().trim().equals("")) {
            rpb_sound_play.setVisibility(View.GONE);
        }
        tv_current_percent.setText(curPosition + 1 + "/" + mQuesList.size());
        tv_titlebar_sub.setText(mQuesList.get(0).getCategory());
    }

    private View.OnClickListener playAudioClickListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (player.isPlaying()) {
                    player.pause();
                    rpb_sound_play.setBackgroundResource(R.mipmap.audio_play);
                } else {
                    String au = mLocalAudioPrefix + mQuesList.get(curPosition).getSounds();
                    player.playUrl(au);
                    LogUtils.e("音频" + au);
                    rpb_sound_play.setBackgroundResource(R.mipmap.audio_pause);
                    mTimer = new Timer();
                    mTimer.schedule(new RequestTimerTask(), 0, 100);
                }
            }
        };
    }

    private boolean flag = false;

    private static final String TAG = "ShowAnalysisActivity";

    class RequestTimerTask extends TimerTask {
        @Override
        public void run() {
            int duration, position;
            if (player != null && player.isPlaying() && !flag) {
                flag = true;
            }
            if (player != null && player.isPlaying() && flag) {
                duration = player.getDuration();
                position = player.mediaPlayer.getCurrentPosition();
                rpb_sound_play.setCricleProgressColor(0xfffea523);
                if (duration < 0) {
                    duration = 0;
                }
                rpb_sound_play.setMax(duration);
                rpb_sound_play.setProgress(position);
            }
            if (player != null && !player.isPlaying() && flag) {
                handler.removeMessages(1);
                handler.sendEmptyMessage(1);
                flag = false;
            }
        }
    }

    @NonNull
    private View.OnClickListener nextTitleClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPosition < mQuesList.size() - 1) {
                    curPosition += 1;
                    vp_analysis_test.setCurrentItem(curPosition);
                } else {
                    ToastUtil.showToast(mContext, "没有更多了~");
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener preTitleClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curPosition > 0) {
                    curPosition -= 1;
                    vp_analysis_test.setCurrentItem(curPosition);
                } else {
                    ToastUtil.showToast(mContext, "已经是第一个了");
                }
            }
        };
    }

    @NonNull
    private ViewPager.OnPageChangeListener pageChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                tv_titlebar_sub.setText(mQuesList.get(position).getCategory());
                tv_titlebar_sub.setText(mQuesList.get(curPosition).getCategory());
                tv_current_percent.setText(curPosition + 1 + "/" + mQuesList.size());
                if (mQuesList.get(position).getSounds() == null || mQuesList.get(position).getSounds().trim().equals("")) {
                    rpb_sound_play.setVisibility(View.INVISIBLE);
                }
                if (player != null && player.isPlaying() && mTimer != null) {
                    mTimer.cancel();
                    player.pause();
                }
                rpb_sound_play.setBackgroundResource(R.mipmap.audio_play);
                LogUtils.e(TAG, "loadsssss" + (mQuesListLoaded.size()));
                if (position == mQuesListLoaded.size() - 1 && times < (mQuesList.size() / count - 1)) {
                    times++;
                    if (times == mQuesList.size() / count - 1) {
                        lastData = true;
                    }
                    for (int i = 0; i < count; i++) {
                        LogUtils.e(TAG, "adddd:   " + (count * times + i));
                        mQuesListLoaded.add(mQuesList.get(count * times + i));
                        mViewItems.add(getLayoutInflater().inflate(R.layout.item_analysis, null));
                    }
                    adapter.setData(mQuesListLoaded, mViewItems);
                    adapter.notifyDataSetChanged();
                } else if (lastData) {
                    times++;
                    lastData = false;
                    for (int i = 0; i < mQuesList.size() % count; i++) {
                        LogUtils.e(TAG, "adlast:   " + (count * times + i));
                        mQuesListLoaded.add(mQuesList.get(count * times + i));
                        mViewItems.add(getLayoutInflater().inflate(R.layout.item_analysis, null));
                    }
                    adapter.setData(mQuesListLoaded, mViewItems);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    rpb_sound_play.setBackgroundResource(R.mipmap.audio_play);
                    rpb_sound_play.setCricleProgressColor(0xfff5f5f5);
                    rpb_sound_play.setMax(100);
                    rpb_sound_play.setProgress(0);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }
}
