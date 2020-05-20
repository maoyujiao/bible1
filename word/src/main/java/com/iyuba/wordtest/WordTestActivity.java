package com.iyuba.wordtest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.wordtest.R;

import com.iyuba.wordtest.R2;
import com.iyuba.wordtest.db.CetDataBase;
import com.iyuba.wordtest.entity.CetRootWord;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.network.HttpManager;
import com.iyuba.wordtest.sign.WordSignDialog;
import com.iyuba.wordtest.utils.TextAttr;
import com.iyuba.wordtest.viewmodel.UserSignViewModel;
import com.jaeger.library.StatusBarUtil;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WordTestActivity extends AppCompatActivity {

    @BindView(R2.id.jiexi)
    TextView jiexi;
    @BindView(R2.id.next_button)
    TextView nextButton;
    @BindView(R2.id.jiexi_word)
    TextView jiexiWord;
    @BindView(R2.id.jiexi_def)
    TextView jiexiDef;
    @BindView(R2.id.jiexi_pron)
    TextView jiexiPron;
    @BindView(R2.id.cb)
    CheckBox cb;
    @BindView(R2.id.jiexi_root)
    RelativeLayout jiexiRoot;
    private String playurl = "http://dict.youdao.com/dictvoice?audio=";

    MediaPlayer player;

    public static void start(Context context, int level) {
        Intent starter = new Intent(context, WordTestActivity.class);
        starter.putExtra("level", level);
        context.startActivity(starter);
    }

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.word)
    TextView word;
    @BindView(R2.id.answera)
    TextView answera;
    @BindView(R2.id.answerb)
    TextView answerb;
    @BindView(R2.id.answerc)
    TextView answerc;
    @BindView(R2.id.answerd)
    TextView answerd;
    @BindView(R2.id.ll)
    LinearLayout ll;

    TextView right;

    private List<CetRootWord> words = new ArrayList<>();
    private List<String> wronganswers = new ArrayList<>();
    CetRootWord cetRootWord;
    int level;
    CetDataBase db;
    TextView[] tvs;
    int wrong = 0;

    ObjectAnimator animator;
    ObjectAnimator animator1;
    AnimatorSet animatorSet = new AnimatorSet();
    int position;
    boolean isCheckable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_test);
        ButterKnife.bind(this);

        getDatas();
        initActionBar();
        initPlayer();
        initAnswerTv();
        initAnimator();

        initData(0, true);

        cb.setOnCheckedChangeListener(listener);
    }

    private void getDatas() {
        db = CetDataBase.getInstance(this);
        level = getIntent().getIntExtra("level", 1);
        words = db.getCetRootWordDao().getWordsByStage(level);
        Collections.shuffle(words);
        wronganswers = db.getCetRootWordDao().getAllAnswers();
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                cetRootWord.flag = 1;
                addNetwordWord(cetRootWord.word);
            } else {
                cetRootWord.flag = 0;
                deleteNetWord(cetRootWord.word);
            }
            db.getCetRootWordDao().updateSingleWord(cetRootWord);
        }
    };

    private void initActionBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initAnswerTv() {
        tvs = new TextView[]{answera, answerb, answerc, answerd};
    }

    private void initAnimator() {
        animator = ObjectAnimator.ofFloat(ll, "translationY", -200, 0);
        animator1 = ObjectAnimator.ofFloat(ll, "alpha", 0.2f, 1f);
        animatorSet.playTogether(animator, animator1);
        animatorSet.setDuration(600);
    }

    private void initPlayer() {
        player = new MediaPlayer();
        player.setOnPreparedListener(onPreparedListener);
    }

    MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            player.start();
        }
    };

    private void initData(int position, boolean reset) {
        try {
            cetRootWord = words.get(position);
            setData(cetRootWord);
            if (reset) {
                wrong = 0;
            }
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }

    }

    private void setData(CetRootWord words) {
        isCheckable = true;
        animatorSet.start();
        word.setText(words.word);
        for (TextView textView : tvs) {
            textView.setBackground(getResources().getDrawable(R.drawable.wordtest_rect_default1));
            textView.setTextColor(Color.parseColor("#333333"));
        }
        int random = new Random().nextInt(100) % 4;
        Log.d("bible", random + "");
        switch (random) {
            case 0:
                fillinAnswers(answera, words.def);
                break;
            case 1:
                fillinAnswers(answerb, words.def);
                break;
            case 2:
                fillinAnswers(answerc, words.def);
                break;
            case 3:
                fillinAnswers(answerd, words.def);
                break;
        }
        jiexi.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        playWord(words.word);
    }

    private void playWord(String word) {
        try {
            player.reset();
            player.setDataSource(playurl + word);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fillinAnswers(TextView textView, String def) {
        textView.setText(def);
        right = textView;
        for (int i = 0; i < tvs.length; i++) {
            if (tvs[i] != textView) {
                tvs[i].setText(wronganswers.get(new Random().nextInt(1000)));
            }
        }
    }

    @SuppressLint("CheckResult")
    @OnClick({R2.id.jiexi, R2.id.next_button, R2.id.answera, R2.id.answerb, R2.id.answerc, R2.id.answerd, R2.id.ll})
    public void onViewClicked(View view) {
        if (jiexiRoot.getVisibility() == View.VISIBLE) {
            jiexiRoot.setVisibility(View.GONE);
            if (view.getId() == R.id.next_button)
                initData(++position, false);
            return;
        }
        int id = view.getId();
        if (id == R.id.jiexi) {
            showJiexiView();
        } else if (id == R.id.next_button) {
            initData(++position, false);
        } else if (id == R.id.answera || id == R.id.answerb || id == R.id.answerc || id == R.id.answerd) {
            if (!isCheckable) return;
            isCheckable = false;
            jiexi.setVisibility(View.VISIBLE);
            if (position == words.size() - 1) nextButton.setText("完成");
            nextButton.setVisibility(View.VISIBLE);
            if (view == right) {
                view.setBackground(getResources().getDrawable(R.drawable.answer_right));
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                cetRootWord.wrong = 1;
                    if (position == (words.size() - 1)) {
//                if (position == 0) {
                    showSuccessDialog();
                    WordConfigManager.Instance(this).putInt("stage", level + 1);
                }
            } else {
                view.setBackground(getResources().getDrawable(R.drawable.answer_wrong));
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                view.startAnimation(shakeAnimation(1));
                cetRootWord.wrong = 2;
                wrong++;
                if (wrong * 100 / words.size() > 10) {
                    showFailDialog();
                } else {
                    if (position == words.size() - 1) {
                        showSuccessDialog();
                        WordConfigManager.Instance(this).putInt("stage", level + 1);
                    }
                }
            }
            db.getCetRootWordDao().updateSingleWord(cetRootWord);
        }

    }

    private void showJiexiView() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jiexiRoot.setVisibility(View.VISIBLE);
            jiexiDef.setText(cetRootWord.def);
            if (!cetRootWord.pron.startsWith("[")){
                jiexiPron.setText(String.format("[%s]", TextAttr.decode(cetRootWord.pron)));
            }else {
                jiexiPron.setText(String.format("%s", TextAttr.decode(cetRootWord.pron)));
            }
            jiexiWord.setText(cetRootWord.word);
            cb.setChecked(cetRootWord.flag == 1);
            Animator animator = ObjectAnimator.ofFloat(jiexiRoot, "scaleX", 0.1f, 1f);
            animator.setDuration(600);
            animator.start();
        } else {
            jiexiRoot.setVisibility(View.VISIBLE);
            jiexiDef.setText(cetRootWord.def);
            if (!cetRootWord.pron.startsWith("[")){
                jiexiPron.setText(String.format("[%s]", TextAttr.decode(cetRootWord.pron)));
            }else {
                jiexiPron.setText(String.format("%s", TextAttr.decode(cetRootWord.pron)));
            }
//            jiexiPron.setText(String.format("[%s]", TextAttr.decode(cetRootWord.pron)));
            jiexiWord.setText(cetRootWord.word);
        }
    }

    private void showFailDialog() {
        String c = String.format("闯关失败,回答%s题,答错%s题,错误率%s,是否重新闯关?", position + 1, wrong, wrong * 100 / (position + 1) + "%");
        new AlertDialog.Builder(this).setMessage(c)
                .setPositiveButton("重新闯关", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initData(0, true);
                    }
                })
                .setNegativeButton("再学一会儿", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showSuccessDialog() {

        String rate = 100 - wrong * 100 / (words.size())+"%";
        String username = WordManager.get().username;
        WordSignDialog wordSignDialog  =  new WordSignDialog(this,new UserSignViewModel(
                level+"",words.size()+"",rate,username,""
        ));
        wordSignDialog.show();
    }

    public Animation shakeAnimation(int counts) {
        Animation translate = new TranslateAnimation(0, 18, 0, 0);
        translate.setInterpolator(new CycleInterpolator(counts));
        translate.setRepeatCount(1);
        translate.setDuration(100);
        return translate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (jiexiRoot.getVisibility() == View.VISIBLE) {
            jiexiRoot.setVisibility(View.GONE);
        } else {
            new AlertDialog.Builder(this).setMessage("您正在进行闯关,确定要退出吗?").setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            })
                    .create()
                    .show();
        }
    }

    private void deleteNetWord(String word) {
        HttpManager.getWordApi().operateWord(word,"delete",
                "Iyuba", WordManager.get().userid);
    }

    private void addNetwordWord(String wordTemp) {
        HttpManager.getWordApi().operateWord(wordTemp,"insert",
                "Iyuba", WordManager.get().userid);
    }


}
