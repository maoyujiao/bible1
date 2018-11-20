package com.iyuba.trainingcamp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.widget.GoldMediaPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/10/11 10:00
 * @change
 * @chang time
 * @class describe
 */
public class SentenceDetailActivity extends BaseActivity {
    private static final int PLAYER_STATE_PAUSE = 2;
    private static final int PLAYER_STATE_PLAY = 1;
    private static final int PLAYER_STATE_STOP = 0;
    @BindView(R2.id.score)
    TextView mScore;
    @BindView(R2.id.content)
    TextView mContent;
    @BindView(R2.id.orignin_record)
    TextView mOrignin_record;
    @BindView(R2.id.my_record)
    TextView mMy_record;
    @BindView(R2.id.read_ll)
    LinearLayout mReadLl;
    @BindView(R2.id.previous)
    TextView mPrevious;
    @BindView(R2.id.next)
    TextView mNext;
    private int mPlayState;


    private int playIndex;
    private List<LearningContent> sentences;
    TypedArray ta;
    Context mContext;
    int color;
    private String currentUrl = "";
    GoldMediaPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.trainingcamp_sentence_detail);
        ButterKnife.bind(this);
        player = new GoldMediaPlayer();
        initTypedArray();
        bindViews();

        getIntents();
    }

    @SuppressLint("ResourceAsColor")
    private void initTypedArray() {
        ta = obtainStyledAttributes(R.styleable.TrainingTheme);
        color = ta.getColor(0, R.color.trainingcamp_cet_theme);
        ta.recycle();
    }

    private void getIntents() {
        playIndex = getIntent().getIntExtra("index", 0);
        sentences = (List<LearningContent>) getIntent().getExtras().getSerializable(ParaConstants.LEARNINGS_LABEL);
        refreshUI();
        player.getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mOrignin_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
                mMy_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);

            }
        });
    }


    private void refreshUI() {
        mContent.setText(sentences.get(playIndex).en);
        mScore.setText(sentences.get(playIndex).score);
        if (playIndex == 0) {
            mNext.setTextColor(color);
            mPrevious.setTextColor(getResources().getColor(R.color.trainingcamp_gray_999));
        } else if (playIndex == sentences.size() - 1) {
            mPrevious.setTextColor(color);
            mNext.setTextColor(getResources().getColor(R.color.trainingcamp_gray_999));
        } else {
            mNext.setTextColor(color);
            mPrevious.setTextColor(color);
        }
        if (player != null && player.getPlayer().isPlaying()) {
            player.getPlayer().stop();
        }
        mMy_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
        mOrignin_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
    }

    private void bindViews() {
        mMy_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
        mOrignin_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player.getPlayer() != null) {
            player.getPlayer().reset();
            player.getPlayer().release();
            player = null;
        }
    }

    private void startPronounce(TextView v, int position, int orignal) {
        String url = "";
        switch (orignal) {
            case 1:
                url = "http://static2.iyuba.com/" + GoldApp.getApp(mContext).LessonType + "/sounds/" + sentences.get(position).pro;
                mMy_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
                break;
            case 0:
                url = FilePath.getRecordPath() + sentences.get(position).getId() + ".amr";
                mOrignin_record.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
                break;
        }
        if (mPlayState == PLAYER_STATE_PLAY) {
            if (currentUrl.equals(url)) {
                currentUrl = url;
                player.pause();
                mPlayState = PLAYER_STATE_PAUSE;
                refreshPlayState();
                if (orignal == 0) {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
                } else {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_play_record), null, null);
                }
            } else {
                currentUrl = url;
                player.stopRestart(url);
                mPlayState = PLAYER_STATE_PLAY;
                if (orignal == 0) {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                } else {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                }
            }

        } else if (mPlayState == PLAYER_STATE_PAUSE) {
            if (currentUrl.equals(url)) {
                currentUrl = url;
                player.stopRestart(url);
                mPlayState = PLAYER_STATE_PLAY;
                if (orignal == 0) {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                } else {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                }
            } else {
                currentUrl = url;
                player.stopRestart(url);
                mPlayState = PLAYER_STATE_PLAY;

                if (orignal == 0) {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                } else {
                    v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
                }
            }

        } else if (mPlayState == PLAYER_STATE_STOP) {
            playIndex = position;
            if (orignal == 0) {
                v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
            } else {
                v.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.trainingcamp_icon_pause_record), null, null);
            }
            mPlayState = PLAYER_STATE_PLAY;
            player.stopRestart(url);
        }
    }

    private void refreshPlayState() {

    }

    @OnClick(R2.id.back)
    public void onMBackClicked() {
        finish();
    }

    @OnClick(R2.id.textbtn)
    public void onMTextbtnClicked() {
    }

    @OnClick(R2.id.orignin_record)
    public void onMOrigninRecordClicked() {
        startPronounce((AppCompatTextView) mOrignin_record, playIndex, 1);
    }

    @OnClick(R2.id.my_record)
    public void onMMyRecordClicked() {
        startPronounce((AppCompatTextView) mMy_record, playIndex, 0);
    }

    @OnClick(R2.id.previous)
    public void onMPreviousClicked() {
        if (playIndex == 0) {
            return;
        } else {
            playIndex--;
            refreshUI();
        }
    }

    @OnClick(R2.id.next)
    public void onMNextClicked() {
        if (playIndex == sentences.size() - 1) {
            return;
        } else {
            playIndex++;
            refreshUI();
        }
    }
}
