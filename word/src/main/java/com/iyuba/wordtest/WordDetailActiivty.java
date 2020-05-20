package com.iyuba.wordtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;

import com.iyuba.wordtest.R;

import com.iyuba.wordtest.R2;
import com.iyuba.wordtest.bean.SearchVoaResult;
import com.iyuba.wordtest.bean.SendEvaluateResponse;
import com.iyuba.wordtest.db.CetDataBase;
import com.iyuba.wordtest.entity.CetRootWord;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.network.EvaluateApi;
import com.iyuba.wordtest.network.HttpManager;
import com.iyuba.wordtest.utils.RecordManager;
import com.iyuba.wordtest.utils.SentenceSpanUtils;
import com.iyuba.wordtest.utils.TextAttr;
import com.jaeger.library.StatusBarUtil;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RuntimePermissions
public class WordDetailActiivty extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private int count;
    private ProgressDialog dialog ;

    public static void start(Context context, CetRootWord word) {
        Intent intent = new Intent(context, WordDetailActiivty.class);
        intent.putExtra("word", word);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.txt_word)
    TextView txtWord;
    @BindView(R2.id.cb_collect)
    CheckBox cbCollect;
    @BindView(R2.id.img_speaker)
    ImageView imgSpeaker;
    @BindView(R2.id.txt_pron)
    TextView txtPron;
    @BindView(R2.id.txt_explain)
    TextView txtExplain;
    @BindView(R2.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R2.id.txt_sentence)
    TextView txtSentence;
    @BindView(R2.id.img_swift)
    ImageView imgSwift;
    @BindView(R2.id.txt_sentence_pron)
    TextView txtSentencePron;
    @BindView(R2.id.txt_sentence_ch)
    TextView txtSentenceCh;
    @BindView(R2.id.img_low_score)
    ImageView imgLowScore;
    @BindView(R2.id.txt_score)
    TextView txtScore;
    @BindView(R2.id.ll_score)
    LinearLayout llScore;
    @BindView(R2.id.txt_encourage)
    TextView txtEncourage;
    @BindView(R2.id.img_original)
    ImageView imgOriginal;
    @BindView(R2.id.img_record)
    ImageView imgRecord;
    @BindView(R2.id.ll_record_bg)
    LinearLayout llRecordBg;
    @BindView(R2.id.img_own)
    ImageView imgOwn;
    @BindView(R2.id.ll_own)
    LinearLayout llOwn;
    @BindView(R2.id.txt_pos_hint)
    TextView txtPosHint;
    @BindView(R2.id.click_record_hint)
    TextView txtClickHint;
    @BindView(R2.id.iv_last)
    TextView last;
    @BindView(R2.id.iv_next)
    TextView next;


    CetDataBase db;

    public static final String Header = "http://voa.iyuba.cn/voa/";
    List<CetRootWord> cetRootWordList = new ArrayList<>();
    File file;
    int position;

    private String sentenceUrl;
    private String wordUrl;

    CetRootWord cetRootWord;

    AnimationDrawable animation;
    AnimationDrawable animation_record;
    AnimationDrawable animation_own;
    RecordManager recordManager;
    private boolean isRecording;

    Context context;
    MediaPlayer player;

    private String playurl = "http://dict.youdao.com/dictvoice?audio=";
    private boolean isSentence = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;
        db = CetDataBase.getInstance(this);
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        int stage = getIntent().getExtras().getInt("stage");
        if (null != getIntent().getExtras().getSerializable("word")) {
            CetRootWord word = (CetRootWord) getIntent().getExtras().getSerializable("word");
            cetRootWordList.add(word);
//            cetRootWordList.addAll(db.getCetRootWordDao().getWordsByStage(word.stage));
            cetRootWordList.addAll(db.getCetRootWordDao().getAllRootWord());

        } else {
            cetRootWordList = db.getCetRootWordDao().getWordsByStage(stage);
        }
        animation = (AnimationDrawable) imgOriginal.getDrawable();
        animation_own = (AnimationDrawable) imgOwn.getDrawable();
        animation_record = (AnimationDrawable) imgRecord.getDrawable();
        refreshUI();
    }


    @OnClick({R2.id.iv_last, R2.id.iv_next, R2.id.cb_collect, R2.id.img_original, R2.id.img_record
            , R2.id.img_swift, R2.id.ll_own, R2.id.img_speaker})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.iv_last) {
            if (position != 0) {
                position--;
                refreshUI();
            } else {
                ToastUtils.showShort("不能向前了");
            }
            stopPlayer();
        } else if (id == R.id.iv_next) {
            if (position != cetRootWordList.size() - 1) {
                position++;
                refreshUI();
            } else {
                ToastUtils.showShort("不能向后了");
            }
            stopPlayer();
        } else if (id == R.id.cb_collect) {
        } else if (id == R.id.img_original) {
            if (isRecording) {
                ToastUtils.showShort("评测中...");
                return;
            }
            if (!isSentence) {
                playAudio(cetRootWord.word, false, false);
            } else {
                playAudio(cetRootWord.sentencePron, true, false);
            }
        } else if (id == R.id.img_swift) {
            llScore.setVisibility(View.INVISIBLE);
            imgLowScore.setVisibility(View.INVISIBLE);
            isSentence = !isSentence;
            setSentenceTxt();
        } else if (id == R.id.img_speaker) {
            playAudio(cetRootWord.word, false, false);
        } else if (id == R.id.ll_own) {
            if (isRecording) {
                ToastUtils.showShort("评测中...");
                return;
            }
            if (!isSentence) {
                playAudio(cetRootWord.word, false, true);
            } else {
                playAudio(cetRootWord.word, true, true);
            }
        } else if (id == R.id.img_record) {
            if (player.isPlaying()) {
                player.stop();
                animation.stop();
            }
            if (!isRecording) {
                WordDetailActiivtyPermissionsDispatcher.startRecordWithPermissionCheck(this, cetRootWord.word);
            } else {
                stopRecord();
                dialog.setMessage("正在测评");
                dialog.show();
                Map<String, RequestBody> map = buildMap(txtSentence.getText().toString());
                HttpManager.getEvaluateApi().sendVoice(map, HttpManager.fromFile(file))
                        .enqueue(new Callback<SendEvaluateResponse>() {
                            @Override
                            public void onResponse(Call<SendEvaluateResponse> call, Response<SendEvaluateResponse> response) {
                                dialog.dismiss();
                                if (response.body().getData() != null) {
                                    if (response.body().getData().getWords() != null) {
                                        if (response.body().getData().getWords().size() > 1) {
                                            sentenceUrl = response.body().getData().getURL();
                                            llOwn.setVisibility(View.VISIBLE);
                                        } else {
                                            wordUrl = response.body().getData().getURL();
                                            llOwn.setVisibility(View.VISIBLE);
                                        }
                                        llOwn.setVisibility(View.VISIBLE);
                                        int scoreF = (int) (Float.parseFloat(response.body().getData().getTotal_score()) * 20);
                                        if (scoreF < 50) {
                                            imgLowScore.setVisibility(View.VISIBLE);
                                            llScore.setVisibility(View.INVISIBLE);
                                            Animation animation = new TranslateAnimation(0, 0, 300, 0);
                                            animation.setDuration(1000);
                                            imgLowScore.startAnimation(animation);
                                        } else {
                                            txtScore.setText(scoreF + "");
                                            imgLowScore.setVisibility(View.INVISIBLE);
                                            llScore.setVisibility(View.VISIBLE);
                                            Animation animation = new TranslateAnimation(0, 0, 300, 0);
                                            animation.setDuration(1000);
                                            llScore.startAnimation(animation);
                                        }
                                        try {
                                            SpannableStringBuilder builder = SentenceSpanUtils.getSpanned(context, response.body().getData().getSentence(),
                                                    response.body().getData().getWords(), cetRootWord.word);
                                            txtSentence.setText(builder);
                                        } catch (IndexOutOfBoundsException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {
                                    ToastUtils.showShort("测评失败,请重试");
                                }
                            }

                            @Override
                            public void onFailure(Call<SendEvaluateResponse> call, Throwable t) {
                                ToastUtils.showShort("测评失败,请重试");
                                dialog.dismiss();
                            }
                        });
            }
        }
    }

    private void setSentenceTxt() {
        if (isSentence) {
            getSentence();
        } else {
            txtSentence.setText(cetRootWord.word);
            txtSentenceCh.setText(cetRootWord.def);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WordDetailActiivtyPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void getSentence(final CetRootWord rootWord) {
        HttpManager.getSearchApi().getSearchResult("json",
                ""+rootWord.word+"", 1, 1,
                0, "voa", "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchVoaResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchVoaResult searchVoaResult) {
                        if (searchVoaResult!=null &&searchVoaResult.getTextData()!=null&& searchVoaResult.getTextData().size()>0){
                            rootWord.sentence = searchVoaResult.getTextData().get(0).getSentence();
                            rootWord.sentenceCN = searchVoaResult.getTextData().get(0).getSentence_cn();
                            rootWord.sentencePron = searchVoaResult.getTextData().get(0).getSoundText();
                            db.getCetRootWordDao().updateSingleWord(rootWord);
                            setSentenceTxt();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        rootWord.sentence = "No Examples" ;
                        rootWord.sentenceCN = "暂无" ;
                        rootWord.sentencePron = "";
//                        db.getCetRootWordDao().updateSingleWord(rootWord);
                        setSentenceTxt();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void playAudio(String word, boolean isSentence, boolean isOwn) {
        if (player.isPlaying()) {
            player.pause();
            animation.stop();
            animation_own.stop();
            return;
        }
        try {
            player.reset();
            if (isSentence) {
                if (isOwn) {
                    player.setDataSource(Header + sentenceUrl);
                    animation_own.start();
                } else {
                    if (TextUtils.isEmpty(cetRootWord.sentencePron)){
                        return;
                    }
                    player.setDataSource(cetRootWord.sentencePron);
                    animation.start();
                }
            } else {
                if (isOwn) {
                    player.setDataSource(Header + wordUrl);
                    animation_own.start();
                } else {
                    player.setDataSource(playurl + word);
                    animation.start();
                }
            }
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void stopRecord() {
        animation_record.stop();
        recordManager.stopRecord();
        isRecording = false;
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void startRecord(String word) {
        txtClickHint.setText("点击停止");
        file = new File(Environment.getExternalStorageDirectory() + "/iyuba/" + WordManager.get().type+ "/audio/"
                + word + ".amr");
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/iyuba/" + WordManager.get().type + "/audio/");
        if (!fileFolder.exists()) fileFolder.mkdirs() ;
        Log.d("http", file.getAbsolutePath());
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        recordManager = new RecordManager(file);
        animation_record.start();
        recordManager.startRecord();
        isRecording = true;
    }

    private Map<String, RequestBody> buildMap(String s) {
        Map<String, RequestBody> map = new HashMap<>(6);
        map.put(EvaluateApi.GetVoa.Param.Key.SENTENCE, HttpManager.fromString(s));
        map.put(EvaluateApi.GetVoa.Param.Key.IDINDEX, HttpManager.fromString(position + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.NEWSID, HttpManager.fromString(position + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.PARAID, HttpManager.fromString(position + ""));
        map.put(EvaluateApi.GetVoa.Param.Key.TYPE, HttpManager.fromString(WordManager.get().type));
        map.put(EvaluateApi.GetVoa.Param.Key.USERID, HttpManager.fromString(WordManager.get().userid));
        return map;
    }

    private void refreshUI() {

        cetRootWord = cetRootWordList.get(position);
        setSentenceTxt();
        llOwn.setVisibility(View.INVISIBLE);
        cbCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });
        cbCollect.setChecked(cetRootWord.flag == 1 ? true : false);
        txtClickHint.setText("点击录音");
        txtWord.setText(cetRootWord.word);
        txtExplain.setText(cetRootWord.def);
        if (cetRootWord.pron.startsWith("[")){
            txtPron.setText(String.format("%s", TextAttr.decode(cetRootWord.pron)));
        }else {
            txtPron.setText(String.format("[%s]", TextAttr.decode(cetRootWord.pron)));
        }
//        txtPron.setText(String.format("[%s]", TextAttr.decode(cetRootWord.pron)));
        txtPosHint.setText(String.format("%d/%d", position + 1, cetRootWordList.size()));

        llScore.setVisibility(View.INVISIBLE);
        imgLowScore.setVisibility(View.INVISIBLE);
        if (WordConfigManager.Instance(context).loadBoolean("autoplay", false)) {
            playAudio(cetRootWord.word, false, false);
            animation.start();
        }
        animation_own.stop();
    }

    private void deleteNetWord(String word) {
        HttpManager.getWordApi().operateWord(word,"delete",
                "Iyuba",WordManager.get().userid);
    }

    private void addNetwordWord(String wordTemp) {
        HttpManager.getWordApi().operateWord(wordTemp,"insert",
                "Iyuba", WordManager.get().userid);
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        animation.stop();
        imgOriginal.setImageDrawable(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void getSentence() {
        if(TextUtils.isEmpty(cetRootWord.sentence)){
            getSentence(cetRootWord);
        }else {
            txtSentence.setText(cetRootWord.sentence);
            txtSentenceCh.setText(cetRootWord.sentenceCN);
            try {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(cetRootWord.sentence);
                spannableStringBuilder.setSpan(new RelativeSizeSpan(1.35f), cetRootWord.sentence.toLowerCase().indexOf(cetRootWord.word),
                        cetRootWord.sentence.toLowerCase().indexOf(cetRootWord.word) + cetRootWord.word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtSentence.setText(spannableStringBuilder);
            } catch (IndexOutOfBoundsException e) {
                txtSentence.setText(cetRootWord.sentence);
            }
        }
    }

    private void stopPlayer() {
        animation.stop();
        if (player.isPlaying()) {
            player.stop();
        }
    }

}
