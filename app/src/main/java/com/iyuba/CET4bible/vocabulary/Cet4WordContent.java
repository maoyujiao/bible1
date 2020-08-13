/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.vocabulary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WordDataManager;
import com.iyuba.CET4bible.sqlite.mode.Cet4Word;
import com.iyuba.CET4bible.sqlite.op.Cet4WordOp;
import com.iyuba.CET4bible.widget.SegmentedRadioGroup;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.activity.Login;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.news.WordUpdateRequest;
import com.iyuba.core.service.Background;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.EGDBOp;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.BackPlayer;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Cet4WordContent extends BasisActivity implements
        OnCheckedChangeListener, OnClickListener {
    private Context mContext;
    private TextView position;
    private View left, right, study, review;
    private ImageView leftImage, rightImage;
    private SegmentedRadioGroup mode;
    private String vocabularyMode;
    private TextView word;
    private Button easy, difficult, know;
    private TextView word2, pron, def, example;
    private ImageView addWord, addWord2;
    private ImageView sound;
    private int pos, all;
    private Cet4Word cet4Word;
    private BackPlayer vv;
    private boolean fromTestDifficult;
    private WordOp wordOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_content);
        mContext = this;
        CrashApplication.getInstance().addActivity(this);
        vocabularyMode = ConfigManager.Instance().loadString("vocabulary");
        pos = WordDataManager.Instance().pos;
        all = WordDataManager.Instance().words.size();
        fromTestDifficult = false;
        wordOp = new WordOp(mContext);
        init();
    }

    private void init() {

        findViewById(R.id.button_back).setOnClickListener(this);
        left = findViewById(R.id.left);
        left.setOnClickListener(this);
        right = findViewById(R.id.right);
        right.setOnClickListener(this);
        leftImage = findViewById(R.id.left_img);
        rightImage = findViewById(R.id.right_img);
        mode = findViewById(R.id.mode);
        mode.setOnCheckedChangeListener(this);
        position = findViewById(R.id.position);
        study = findViewById(R.id.study_content);
        review = findViewById(R.id.review_content);
        if (null == BackgroundManager.Instace().bindService){
            bindService(new Intent(mContext,Background.class), BackgroundManager.Instace().conn, Service.BIND_AUTO_CREATE);
        }
        if (BackgroundManager.Instace().bindService != null){
            vv = BackgroundManager.Instace().bindService.getPlayer();
        }else {
            vv = new BackPlayer(mContext);
        }
        initStudy();
        initReview();
        setPosText();
        controlVideo();
        if (vocabularyMode.equals("study")) {
            checkStudy();
        } else {
            checkReview();
        }
    }

    private void initReview() {

        word = findViewById(R.id.word);
        easy = findViewById(R.id.so_easy);
        difficult = findViewById(R.id.difficult);
        addWord = findViewById(R.id.word_add2);
        easy.setOnClickListener(this);
        difficult.setOnClickListener(this);
        addWord.setOnClickListener(this);
    }

    /**
     *
     */
    private void initStudy() {

        word2 = findViewById(R.id.word_key);
        def = findViewById(R.id.word_def);
        pron = findViewById(R.id.word_pron);
        sound = findViewById(R.id.word_speaker);
        example = findViewById(R.id.example);
        example.setMovementMethod(ScrollingMovementMethod.getInstance());
        addWord2 = findViewById(R.id.word_add);
        know = findViewById(R.id.know);
        addWord2.setOnClickListener(this);
        sound.setOnClickListener(this);
        know.setOnClickListener(this);
    }

    private void checkStudy() {
        study.setVisibility(View.VISIBLE);
        review.setVisibility(View.GONE);
        mode.check(R.id.study);
    }

    private void checkReview() {
        study.setVisibility(View.GONE);
        review.setVisibility(View.VISIBLE);
        mode.check(R.id.review);
    }

    private void setPosText() {
        StringBuffer sb = new StringBuffer();
        sb.append(pos + 1).append('/').append(all);
        position.setText(sb.toString());
        if (pos == 0) {
            leftImage.setBackgroundResource(R.drawable.no_left);
            rightImage.setBackgroundResource(R.drawable.no_right);
        } else if (pos == all - 1) {
            leftImage.setBackgroundResource(R.drawable.no_left);
            rightImage.setBackgroundResource(R.drawable.no_right);
        } else {
            leftImage.setBackgroundResource(R.drawable.no_left);
            rightImage.setBackgroundResource(R.drawable.no_right);
        }
        fromTestDifficult = false;
        setContent();
    }

    private void setContent() {
        cet4Word = WordDataManager.Instance().words.get(pos);
        if (BuildConfig.isEnglish) {
            cet4Word.example = new EGDBOp(mContext).findData(cet4Word.word);
        } else {
            cet4Word.example = new EGDBOp(mContext).findJPData(cet4Word.word);
        }
        if (mode.getCheckedRadioButtonId() == R.id.study) {
            word2.setText(cet4Word.word);
            if (TextUtils.isEmpty(cet4Word.example)) {
                example.setText(com.iyuba.biblelib.R.string.no_word_example);
            } else {
                if (BuildConfig.isEnglish) {
                    example.setText(Html.fromHtml(cet4Word.example));
                } else {
                    example.setText(cet4Word.example);
                }
                example.scrollTo(0, 0);
            }
            StringBuffer sb = new StringBuffer();
            if(cet4Word.pron.startsWith("[")){
                sb.append(cet4Word.pron);
            }else {
                sb.append('[').append(cet4Word.pron).append(']');
            }
            pron.setText(TextAttr.decode(sb.toString()));
            def.setText(cet4Word.def);
            if (fromTestDifficult) {
                know.setVisibility(View.VISIBLE);
            } else {
                know.setVisibility(View.GONE);
            }
            if (wordOp.findDataByName(cet4Word.word) != null) {
                addWord2.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord2.setBackgroundResource(R.drawable.word_add);
            }
        } else {
            word.setText(cet4Word.word);
            if (wordOp.findDataByName(cet4Word.word) != null) {
                addWord.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord.setBackgroundResource(R.drawable.word_add);
            }
        }
    }

    private void saveNewWords() {
        if (!AccountManager.Instace(mContext).checkUserLogin()) {
            Intent intent = new Intent();
            intent.setClass(mContext, Login.class);
            mContext.startActivity(intent);
        } else {
            Word word = new Word();
            word.key = cet4Word.word;
            word.audioUrl = cet4Word.sound;
            word.pron = cet4Word.pron;
            word.def = cet4Word.def;
            word.examples = cet4Word.example;
            word.userid = AccountManager.Instace(mContext).userId;
            new WordOp(mContext).saveData(word);
            if (mode.getCheckedRadioButtonId() == R.id.study) {
                addWord2.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord.setBackgroundResource(R.drawable.word_add_ok);
            }
            CustomToast.showToast(mContext, R.string.play_ins_new_word_success);
            if (BuildConfig.isEnglish) {
                addNetwordWord(cet4Word.word);
            }
        }
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


                    }
                });
    }

    private void playAudio() {
        if (BuildConfig.isEnglish) {
            String url = "http://staticvip2.iyuba.cn/aiciaudio/" + cet4Word.word
                    + ".mp3";
            vv.setVideoPath(url);
        } else {
            if (!TextUtils.isEmpty(cet4Word.sound)) {
                vv.setVideoPath(cet4Word.sound);
            }
        }
    }

    private void controlVideo() {
        vv.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                vv.start();
            }
        });
    }

    private void toNextOne() {
        if (pos < all - 1) {
            pos++;
        } else {
            CustomToast.showToast(mContext, R.string.sort_last);
        }
        setPosText();
        playAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mode.getCheckedRadioButtonId() == R.id.study) {
            ConfigManager.Instance().putString("vocabulary", "study");
        } else {
            ConfigManager.Instance().putString("vocabulary", "review");
        }
        WordDataManager.Instance().number = pos;
    }

    @Override
    public void onClick(View arg0) {

        int grade;
        Cet4WordOp op;
        switch (arg0.getId()) {
            case R.id.button_back:
                onBackPressed();
                break;
            case R.id.left:
                if (pos > 0) {
                    pos--;
                } else {
                    CustomToast.showToast(mContext, R.string.sort_first);
                }
                setPosText();
                playAudio();
                break;
            case R.id.right:
                toNextOne();
                break;
            case R.id.word_add:
            case R.id.word_add2:
                saveNewWords();
                break;
            case R.id.so_easy:
                vv.pause();
                try {
                    grade = Integer.parseInt(cet4Word.star);
                    grade += 3;
                    cet4Word.star = String.valueOf(grade);
                    op = new Cet4WordOp(mContext);
                    op.updateStar(cet4Word);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                toNextOne();
                break;
            case R.id.difficult:
                vv.pause();
                try {
                    grade = Integer.parseInt(cet4Word.star);
                    grade -= 3;
                    cet4Word.star = String.valueOf(grade);
                    op = new Cet4WordOp(mContext);
                    op.updateStar(cet4Word);
                    fromTestDifficult = true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                checkStudy();
                setContent();
                break;
            case R.id.word_speaker:
                playAudio();
                break;
            case R.id.know:
                fromTestDifficult = false;
                toNextOne();
                checkReview();
                setContent();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int checkedId) {

        if (checkedId == R.id.study) {
            checkStudy();
        } else {
            checkReview();
        }
        setContent();
    }
}
