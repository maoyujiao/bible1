package com.iyuba.trainingcamp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.manager.IseManager;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.widget.WheelView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yq QQ:1032006226
 */

/**
 * onCreate: TestListBean{TestId=2, Sounds='1-S-2.mp3', Answer='今年失业人数在2.1亿到近2.4亿之间。',
 * Category='口语', Question='The number of unemployed people this year range from 210 million to nearly 240 million people.',
 * Attach='null', Pic='null', id=33116, TestType=5, Tags='语音语调',
 * Answer5='null', Answer4='null', Answer2='null', Answer3='null',
 * Answer1='null', result='null', userAnswer='null', Lessonid=1, Explains='null', flag_ever_do=false}
 */
public class SentencListActivity extends BaseActivity implements View.OnClickListener {

    List<AbilityQuestion.TestListBean> list;

    RecyclerView recyclerView;

    TextView history ;
    TextView tv_cn , tv_en ;
    WheelView wheelView;
    ImageView back;
    TextView start;
    LinearLayout ll;
    LinearLayout linearLayout;
    MediaPlayer player;
    IseManager manager;
    List<LearningContent> mLearningContents = new ArrayList<>();
    Context context;
    int temp = 0;
    LearningContent mLearningContent;
    AnimationDrawable animationDrawable ;
    private int index;
    int playIndex;
    int llheight;
    private ImageView play;
    private ImageView follow;
    private final int PLAYER_STATE_PLAY = 1;
    private final int PLAYER_STATE_PAUSE = 2;
    private final int PLAYER_STATE_STOP = 0;
    private int mPlayState;
    private  TextView img_score;
    private int [] mDrawables = {R.drawable.trainingcamp_icon_0_0,R.drawable.trainingcamp_icon_1_59,R.drawable.trainingcamp_icon_60_79,R.drawable.trainingcamp_icon_80_100};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_sentence_list_gold);
        context = this;

        player = new MediaPlayer();
        list = (List<AbilityQuestion.TestListBean>) getIntent().getExtras().getSerializable(ParaConstants.QUESTION_LIST_LABEL);
        for (int i = 0; i < list.size(); i++) {
            LearningContent learningContent = new LearningContent();
            learningContent.en = list.get(i).getQuestion();
            learningContent.cn = list.get(i).getAnswer();
            learningContent.question = list.get(i);
            learningContent.pro = list.get(i).getSounds();
            learningContent.id = list.get(i).getId();
            mLearningContents.add(learningContent);
        }
        ACache.get(context).put("sentence", (Serializable) mLearningContents);
        manager = IseManager.getInstance(this, handler);
        mLearningContent = mLearningContents.get(0);
        bindViews();
        ll.measure(0, 0);
        llheight = ll.getMeasuredHeight();
        initItems();
    }

    private void bindViews() {
        play = findViewById(R.id.play);
        recyclerView = findViewById(R.id.recyclerView);
        tv_en = findViewById(R.id.en);
        tv_cn = findViewById(R.id.cn);

        history= findViewById(R.id.textbtn);
        history.setVisibility(View.VISIBLE);
        history.setOnClickListener(this);
        start = findViewById(R.id.start);
        back = findViewById(R.id.back);
        ll = findViewById(R.id.llcontent);
        ll.setVisibility(View.VISIBLE);
        linearLayout = findViewById(R.id.ll);
        linearLayout.setVisibility(View.INVISIBLE);
        follow = findViewById(R.id.follow);
        wheelView = findViewById(R.id.wheelView);
        img_score = findViewById(R.id.img_score);
        play.setOnClickListener(this);
        follow.setOnClickListener(this);
        back.setOnClickListener(this);
        start.setOnClickListener(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/DINMedium_0.ttf");
        img_score.setTypeface(tf);
    }


    private void startPronounce() {
        String url = "http://static2.iyuba.com/" + GoldApp.getApp(context).LessonType + "/sounds/" + mLearningContent.pro ;
        if (mPlayState == PLAYER_STATE_PLAY) {
            player.pause();
            mPlayState = PLAYER_STATE_PAUSE;
            play.setImageResource(R.drawable.trainingcamp_icon_play);
            return;
        } else if (mPlayState == PLAYER_STATE_PAUSE) {
            if (playIndex == index){
                mPlayState = PLAYER_STATE_PLAY;
                play.setImageResource(R.drawable.trainingcamp_icon_pause);
                player.start();
            }else {
                playIndex = index;
                play.setImageResource(R.drawable.trainingcamp_icon_play);
                player.reset();
                try {
                    player.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.prepareAsync();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        play.setImageResource(R.drawable.trainingcamp_icon_pause);
                        mPlayState = PLAYER_STATE_PLAY;
                    }
                });
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        play.setImageResource(R.drawable.trainingcamp_icon_play);
                        mPlayState = PLAYER_STATE_STOP;
                    }
                });
            }

        } else if (mPlayState == PLAYER_STATE_STOP) {
            playIndex = index;
            play.setImageResource(R.drawable.trainingcamp_icon_play);
            player.reset();
            try {
                player.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    play.setImageResource(R.drawable.trainingcamp_icon_pause);
                    mPlayState = PLAYER_STATE_PLAY;
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    play.setImageResource(R.drawable.trainingcamp_icon_play);
                    mPlayState = PLAYER_STATE_STOP;
                }
            });
        }

    }

    @Override
    protected void onPause() {
        follow.setImageResource(R.drawable.trainingcamp_icon_follow);
        if (manager!=null){
            manager.cancelEvaluate(true);
            handler.removeCallbacksAndMessages(null);
            manager.destroy();
        }

        if (player != null) {
            player.stop();
//            player.stop();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager = IseManager.getInstance(this, handler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    LogUtils.d("diao", "handleMessage: " + msg.arg1 + ":::" + msg.arg2);
                    mLearningContent.score = msg.arg1+"";
                    if (wheelView.getSeletedItem().equals(mLearningContent)){
                        img_score.setVisibility(View.VISIBLE);
                        setImageScore(msg.arg1);
                    }

                    follow.setImageResource(R.drawable.trainingcamp_icon_follow);
                    if (animationDrawable != null && animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                    break;
                case 1000:
                    animationDrawable.stop();
                    ToastUtil.showToast(context,"网络环境异常,请稍后重试");
                    follow.setImageResource(R.drawable.trainingcamp_icon_follow);
            }
            super.handleMessage(msg);
        }
    };

    private void setImageScore(int score) {
        int index = 0;
        if (score<5){
            index = 0;
        }else if (score<60){
            index = 1;
        }else if (score<80){
            index = 2;
        }else {
            index = 3;
        }
        img_score.setBackgroundResource(mDrawables[index]);
        img_score.setText(score+"");
    }

    private void follow() {
        if (manager == null){
            manager = IseManager.getInstance(context,handler);
            manager.startEvaluate(mLearningContent.en, mLearningContent.getId());
            animationDrawable = (AnimationDrawable) getResources().getDrawable(
                    R.drawable.trainingcamp_speaking_anim);
            follow.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }else if (manager.isEvaluating()){
            manager.stopEvaluating();
        }else {
            manager.startEvaluate(mLearningContent.en, mLearningContent.getId());
            animationDrawable = (AnimationDrawable) getResources().getDrawable(
                    R.drawable.trainingcamp_speaking_anim);
            follow.setImageDrawable(animationDrawable);
            animationDrawable.start();
        }
    }



    private void initItems() {
        ll.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        wheelView.init(WheelView.TYPE_SENTENCE, llheight);

        wheelView.setItems(mLearningContents);

        wheelView.setSeletion(0);
        LearningContent item = mLearningContents.get(0);
        tv_en.setText(item.en);
        tv_cn.setText(item.question.getAnswer());

        wheelView.measure(0,0);
        LogUtils.d("diao",wheelView.getMeasuredHeight()+"");
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, LearningContent item) {
                if (selectedIndex == mLearningContents.size()+1){
                    tv_en.setText(mLearningContents.get(mLearningContents.size()-1).en);
                    tv_cn.setText(mLearningContents.get(mLearningContents.size()-1).cn);
                    mLearningContent = mLearningContents.get(mLearningContents.size()-1);
                }else {
                    tv_en.setText(item.en);
                    tv_cn.setText(item.cn);
                    mLearningContent = item;

                }

                findViewById(R.id.rr).setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(item.score)){
                    img_score.setBackgroundResource(R.drawable.trainingcamp_score_default);
                    img_score.setText("");
                }else {
                    img_score.setVisibility(View.VISIBLE);
                    setImageScore(Integer.parseInt(item.score));
                }
                index = selectedIndex;
                super.onSelected(selectedIndex, item);
            }
        });

//        wheelView.setOffset(0);
        wheelView.setOnScrollLisener(new WheelView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                // TODO
                findViewById(R.id.rr).setVisibility(View.INVISIBLE);
            }
        });

        return;
    }

    private void showConfirmDialog(int i) {
        if (i == 1) {
            AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                    .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(player == null){
                            }else if (player.isPlaying()){
                                player.stop();
                            }
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.follow) {
            follow();
        } else if (v.getId() == R.id.play) {
            startPronounce();
        } else if (v.getId() == R.id.start) {
            Intent intent = new Intent(context,SentenceTestActivity.class);
            intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable) mLearningContents);
            intent.putExtra(ParaConstants.QUESTION_LIST_LABEL, (Serializable) list);

            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.back) {
            showConfirmDialog(1);
        } else if (v.getId() == R.id.textbtn){
            showHistory();
        }
    }

    private void showHistory() {
        Intent intent = new Intent(this, SentenceResultActivity.class);
        intent.putExtra(ParaConstants.LEARNINGS_LABEL, (Serializable)ACache.get(context).getAsObject("sentence"));
        startActivity(intent);
    }

}
