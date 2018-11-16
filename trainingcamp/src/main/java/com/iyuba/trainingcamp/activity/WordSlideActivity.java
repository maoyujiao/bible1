package com.iyuba.trainingcamp.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.Gravity;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.utils.ParaConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.training.activity
 * @class describe
 * @time 2018/6/26 15:08
 * @change
 * @chang time
 * @class describe
 */
/**该文件暂时没有用到 /*- . - */
public class WordSlideActivity extends BaseActivity {


    private List<LearningContent> mLearningContents;
    private List<LearningContent> reviewWords;
    private Context mContext;

    private int pos = 0;
    MediaPlayer player;
    int x, y;
    private LinearLayout card_item_content;
    private RelativeLayout title;
    private RelativeLayout card_top_layout;
    private TextView word;
    private LinearLayout word_pronounce;
    private TextView word_prounounce_Eng;
    private TextView phrase;
    private Button i_know;
    private Button not_know;
    private int currentword ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.trainingcamp_word_card);
        bindViews();
        mLearningContents = (List<LearningContent>) getIntent().getSerializableExtra(ParaConstants.LEARNINGS_LABEL);
        if (getIntent().getIntExtra("index",-1)!=-1){
            currentword = getIntent().getIntExtra("index",-1);
            setNextInvisible();
        }


//        int[] position = new int[2];
//        card_top_layout.getLocationInWindow(position);

//        x = position[0];
        reviewWords = new ArrayList<LearningContent>();
        for (LearningContent i : mLearningContents) {
            reviewWords.add(i);
        }
        refreshUI(pos);
    }

    //从试题界面跳转时候要去掉下一题
    private void setNextInvisible() {

    }

    private void bindViews() {

        card_item_content = (LinearLayout) findViewById(R.id.card_item_content);
        title = (RelativeLayout) findViewById(R.id.title);
        card_top_layout = (RelativeLayout) findViewById(R.id.card_top_layout);
        word = (TextView) findViewById(R.id.word);
        word_pronounce = (LinearLayout) findViewById(R.id.word_pronounce);
        word_prounounce_Eng = (TextView) findViewById(R.id.word_prounounce_Eng);
        phrase = (TextView) findViewById(R.id.phrase);
        i_know = (Button) findViewById(R.id.i_know);
        not_know = (Button) findViewById(R.id.not_know);


        i_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                I_know();
            }
        });

        not_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail();
            }
        });
    }

    private void showDetail() {

    }

    public void I_know() {

        ScaleAnimation myAnimation_Scale = new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        TranslateAnimation animation = new TranslateAnimation(0, 1000, 0, 0);
        RotateAnimation animation1 = new RotateAnimation(0.0f, +90.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
//        RotateAnimation anim1 = new RotateAnimation(-90.0f, 0.0f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF, 1.0f);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(animation1);
//        animationSet.addAnimation(anim1);

//        animationSet.addAnimation(myAnimation_Scale);
        animationSet.setDuration(300);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                i_know.setClickable(false);
                not_know.setLinksClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                i_know.setClickable(true);
                not_know.setLinksClickable(true);
                if (reviewWords.size() > 0) {
                    reviewWords.remove(reviewWords.get(pos));
                    if (reviewWords.size() > 0) {
                        refreshUI(pos);
                    }

                } else {
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        card_top_layout.setAnimation(animationSet);

        card_top_layout.setGravity(Gravity.CENTER_HORIZONTAL);


    }
//    private void startGame(){
//        Intent intent = new Intent(this, WordGameActivity.class);
//        intent.putExtra("words", (Serializable) mLearningContents);
//        startActivity(intent);
//    }

//    @OnClick(R2.id.not_know)
//    public void move() {
//        Intent intent = new Intent(mContext, WordDetailActivity.class);
//        intent.putExtra("words", (Serializable) mLearningContents);
//        startActivity(intent);
//    }

    private void refreshUI(int pos) {
        ScaleAnimation myAnimation_Scale2 = new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        TranslateAnimation anim = new TranslateAnimation(-1000, 0, 0, 0);
        RotateAnimation anim1 = new RotateAnimation(-90.0f, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        AnimationSet animSet = new AnimationSet(false);
        animSet.addAnimation(anim);
        animSet.addAnimation(anim1);
//        animationSet.addAnimation(myAnimation_Scale);
        animSet.setDuration(300);
        card_top_layout.setAnimation(animSet);


        LearningContent temp = reviewWords.get(pos);
        word_prounounce_Eng.setText(temp.pro);
        word.setText(temp.en);
    }

//    @OnClick(R2.id.word_pronounce)
//    public void getPronounce() {
//        if (player.isPlaying()){
//            return;
//        }else {
//            player.reset();
//        }
//        String url = "http://staticvip2.iyuba.com/aiciaudio/"+ reviewWords.get(pos).word
//                + ".mp3";
//        try {
//            player.setDataSource(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        player.prepareAsync();
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//
//                mp.start();
//
//            }
//        });
//        SimpleDateFormat format = new SimpleDateFormat("mm-dd");
//        Date s = getThisWeekMonday(new Date());
//        Log.d("123",format.format(s));

}


//    public static Date getThisWeekMonday(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        // 获得当前日期是一个星期的第几天
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
//        if (1 == dayWeek) {
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//        }
//        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
//        cal.setFirstDayOfWeek(Calendar.MONDAY);
//        // 获得当前日期是一个星期的第几天
//        int day = cal.get(Calendar.DAY_OF_WEEK);
//        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
//        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
//        return cal.getTime();
//    }



