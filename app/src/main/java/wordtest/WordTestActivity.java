package wordtest;

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

import com.iyuba.CET4bible.R;
import com.iyuba.base.BaseActivity;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.news.WordUpdateRequest;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newDB.CetDataBase;
import newDB.CetRootWord;

public class WordTestActivity extends BaseActivity implements WordTestMvpView{

    @BindView(R.id.jiexi)
    TextView jiexi;
    @BindView(R.id.next_button)
    TextView nextButton;
    @BindView(R.id.jiexi_word)
    TextView jiexiWord;
    @BindView(R.id.jiexi_def)
    TextView jiexiDef;
    @BindView(R.id.jiexi_pron)
    TextView jiexiPron;
    @BindView(R.id.cb)
    CheckBox cb;
    @BindView(R.id.jiexi_root)
    RelativeLayout jiexiRoot;
    private String playurl = "http://dict.youdao.com/dictvoice?audio=";

    MediaPlayer player;

    public static void start(Context context, int level) {
        Intent starter = new Intent(context, WordTestActivity.class);
        starter.putExtra("level", level);
        context.startActivity(starter);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.word)
    TextView word;
    @BindView(R.id.answera)
    TextView answera;
    @BindView(R.id.answerb)
    TextView answerb;
    @BindView(R.id.answerc)
    TextView answerc;
    @BindView(R.id.answerd)
    TextView answerd;
    @BindView(R.id.ll)
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
        setContentView(R.layout.activity_word_test_main);
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
            textView.setBackground(getResources().getDrawable(R.drawable.rect_default1));
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
    @OnClick({R.id.jiexi, R.id.next_button, R.id.answera, R.id.answerb, R.id.answerc, R.id.answerd, R.id.ll})
    public void onViewClicked(View view) {
        if (jiexiRoot.getVisibility() == View.VISIBLE) {
            jiexiRoot.setVisibility(View.GONE);
            if (view.getId() == R.id.next_button)
                initData(++position, false);
            return;
        }
        switch (view.getId()) {
            case R.id.jiexi:
                showJiexiView();
                break;
            case R.id.next_button:
                initData(++position, false);
                break;
            case R.id.answera:
            case R.id.answerb:
            case R.id.answerc:
            case R.id.answerd:
                if (!isCheckable) return;
                isCheckable = false;
                jiexi.setVisibility(View.VISIBLE);
                if (position == words.size() - 1) nextButton.setText("完成");
                nextButton.setVisibility(View.VISIBLE);
                if (view == right) {
                    //正确的时候
                    view.setBackground(getResources().getDrawable(R.drawable.wordtest_answer_wrong));
                    ((TextView)view).setTextColor(getResources().getColor(R.color.white));
                    cetRootWord.wrong = 1;
                    if (position == words.size() - 1) {
                        showSuccessDialog();
                        ConfigManager.Instance().putInt("stage", level + 1);
                    }
                } else {
                    //错误的时候
                    view.setBackground(getResources().getDrawable(R.drawable.wordtest_answer_right));
                    ((TextView)view).setTextColor(getResources().getColor(R.color.white));
                    view.startAnimation(shakeAnimation(1));
                    cetRootWord.wrong = 2;
                    wrong++;
                    if (wrong * 100 / words.size() > 10) {
                        showFailDialog();
                    } else {
                        if (position == words.size() - 1) {
                            showSuccessDialog();
                            ConfigManager.Instance().putInt("stage", level + 1);
                        }
                    }
                }
                db.getCetRootWordDao().updateSingleWord(cetRootWord);
                break;
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
        new AlertDialog.Builder(this).setMessage("恭喜您闯关成功!")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
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
        ClientSession.Instance().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_DELETE,
                        word), new IResponseReceiver() {

                    @Override
                    public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                    }

                });
    }

    private void addNetwordWord(String wordTemp) {
        ExeProtocol.exe(new WordUpdateRequest(
                        AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new ProtocolResponse() {
                    @Override
                    public void finish(BaseHttpResponse bhr) {

                    }

                    @Override
                    public void error() {
                        // TODO Auto-generated method stub
                        Log.d("测试", "finish: 我是br上传失败");
                    }
                });
    }


}
