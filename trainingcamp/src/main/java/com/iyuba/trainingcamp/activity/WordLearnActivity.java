package com.iyuba.trainingcamp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.http.HttpUrls;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

/**
 * @author yq QQ:1032006226
 */
public class WordLearnActivity extends BaseActivity {

    private List<LearningContent> mWords;
    private Context mContext;
    private MediaPlayer player;

    private TextView mWord;
    private ImageView mProImg;
    private TextView mPro;
    private TextView mCn;
    private TextView mPhrase;
    private TextView mNotYet;
    private TextView mRemembered;
    private TextView nextQue;
    private Handler handler;
    private List<AbilityQuestion.TestListBean> questionList;
    private ExplosionField mExplosionField;
    private LinearLayout ll_top;
    private LinearLayout ll_bottom;
    private int curPosition;

    private void bindViews() {
        mWord = findViewById(R.id.word);
        mProImg = findViewById(R.id.pro_img);
        mPro = findViewById(R.id.pro);
        mCn = findViewById(R.id.cn);
        mPhrase = findViewById(R.id.phrase);
        mNotYet = findViewById(R.id.not_yet);
        mRemembered = findViewById(R.id.remembered);
        nextQue = findViewById(R.id.next_que);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_top = findViewById(R.id.ll_top);
        handler = new Handler();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(1);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.trainingcamp_word_learn);
        mExplosionField = ExplosionField.attach2Window(this);
        bindViews();
        mWords = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
        questionList = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
        player = new MediaPlayer();
        refreshUI();
        showTranslation();

        mRemembered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                showTranslation();
                mWords.get(curPosition).remembered = true;
                iKnow();
            }
        });
        mNotYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate();
                showTranslation();
                showNext();
                if (curPosition == questionList.size() - 1) {
                    nextQue.setText("FINISHED");
                } else {
                    nextQue.setText("NEXT");
                }
                mWords.get(curPosition).remembered = false;
            }
        });
        nextQue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                if (curPosition == mWords.size() - 1) {
                    Intent intent = new Intent(mContext, LearnResultActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mWords);
                    intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) questionList);
                    startActivity(intent);
                    finish();
                    return;
                }
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                curPosition++;
                                measureItems();
                                mExplosionField.explode(ll_bottom);
                                mExplosionField.explode(ll_top);
                                mPro.postDelayed(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshUI();
                                            }
                                        }
                                        , 500);
                            }
                        }, 0);
                disableNext();
            }
        });
        mProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPronounce();
            }
        });
    }

    private void measureItems() {
        ll_bottom.measure(0, 0);
        ll_top.measure(0, 0);
    }

    private void showTranslation() {
        mCn.setText(mWords.get(curPosition).cn_detail);
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < mWords.get(curPosition).phrases.size(); i++) {
            if (i == 1) {
                buffer.append("\n");
            }
            buffer.append(mWords.get(curPosition).phrases.get(i));
        }
        mPhrase.setText(buffer);
    }

    private void disableNext() {
        mRemembered.setVisibility(View.VISIBLE);
        mNotYet.setVisibility(View.VISIBLE);
        nextQue.setVisibility(View.GONE);
    }


    private void showNext() {
        mRemembered.setVisibility(View.GONE);
        mNotYet.setVisibility(View.GONE);
        nextQue.setVisibility(View.VISIBLE);
    }

    public void iKnow() {
        if (curPosition < mWords.size() - 1) {
            showNext();
            nextQue.setText("NEXT");
            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            curPosition++;
                            mExplosionField.explode(ll_top);
                            mExplosionField.explode(ll_bottom);
                            mPro.postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            refreshUI();
                                        }
                                    }, 500);
                        }
                    }, 2000);
        } else {
            showNext();
            nextQue.setText("FINISHED");
        }
    }

    private void refreshUI() {
        ll_top.setAlpha(1f);
        ll_bottom.setAlpha(1f);
        ll_top.setScaleX(1f);
        ll_bottom.setScaleX(1f);

        ll_top.setScaleY(1f);
        ll_bottom.setScaleY(1f);

        setWordsNullView();
        showTranslation();
        disableNext();
        getPronounce();

    }

    private void setWordsNullView() {

        LearningContent temp = mWords.get(curPosition);
        mPro.setText(temp.pro);
        mWord.setText(temp.en);
        mCn.setText("");
        if (temp.phrases != null && temp.phrases.size() > 1) {
            mPhrase.setText("提示:\n" + temp.phrases.get(1));
        }
    }

    public void getPronounce() {
        if (player.isPlaying()) {
            return;
        } else {
            player.reset();
        }
        String url = HttpUrls.GET_WORD_PRO + mWords.get(curPosition).getQuestion().getSounds();
        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void showConfirmDialog(int i) {
        if (i == 1) {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                    .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        } else {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_is_study_sentence_continue))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.trainingcamp_exit_study, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        showConfirmDialog(1);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    public static void start(Context mContext, List<LearningContent> mLearningContents,
                             List<AbilityQuestion.TestListBean> list) {
        Intent intent = new Intent(mContext, WordLearnActivity.class);
        intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
        intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);
        mContext.startActivity(intent);
    }
}
