package com.iyuba.trainingcamp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.R2;
import com.iyuba.trainingcamp.adapter.WordListAdapter;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.http.DownloadUtil;
import com.iyuba.trainingcamp.http.HttpUrls;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.FileUtils;
import com.iyuba.trainingcamp.utils.LogUtils;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.iyuba.trainingcamp.widget.WheelView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yq QQ:1032006226
 */
public class WordListActvity extends BaseActivity {


    WordListAdapter adapter;
    List<AbilityQuestion.TestListBean> list;
    @BindView(R2.id.ll)
    LinearLayout linearLayout;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.title_txt)
    TextView mTitleTxt;
    @BindView(R2.id.share)
    ImageView mShare;
    @BindView(R2.id.textbtn)
    TextView history;
    @BindView(R2.id.wheelView)
    WheelView wheelView;
    @BindView(R2.id.start)
    TextView start;
    @BindView(R2.id.go_test)
    TextView mGoTest;

    @BindView(R2.id.en)
    TextView mEn;
    @BindView(R2.id.fayin)
    ImageView fayin;
    @BindView(R2.id.pro)
    TextView mPro;
    @BindView(R2.id.cn)
    TextView mCn;
    @BindView(R2.id.phrase)
    TextView mPhrase;
    @BindView(R2.id.llcontent)
    LinearLayout ll;
    @BindView(R2.id.avi)
    AVLoadingIndicatorView aviView;
    @BindView(R2.id.rr_avi)
    RelativeLayout mRrAvi;
    @BindView(R2.id.rr_root)
    RelativeLayout mRrRoot;

    private int finished;

    MediaPlayer player;
    String lessonid;
    boolean finishDownload = false;

    List<LearningContent> mLearningContents = new ArrayList<>();
    Context context;
    String url;

    int llheight;
    public static final int TXT_WORD_EN = 0;

    public static final int TXT_WORD_CN = 2;

    public static final int TXT_WORD_CN_DETAIL = 4;
    private static final int TXT_WORD_PRO = 1;
    private static final int TXT_WORD_PHRASE = 5;

    private TextView go_test;

    private int position = 0;
    private boolean isFinishDownload;

    /**
     * 解析文件示例
     * 0:prominent
     * 1:['prɒmɪnənt]
     * 2:adj.突出的，显著的
     * 3:中文释义：
     * 4:adj.突出的，显著的；杰出的；卓越的
     * 5:引申：
     * 6:prominent figures
     * 7:杰出人物
     * 8:a prominent position
     * 9:一个显眼位置
     * 10:play a prominent part
     * 11:起着非常重要的作用
     */

/*    TestListBean{TestId=2, Sounds='unemployed.mp3', Answer='B', Category='单词',
            Question='unemployed', Attach='null', Pic='null', id=33106, TestType=1,
            Tags='形容词辨析', Answer5='null', Answer4='美学的', Answer2='失业的',
            Answer3='脆弱的', Answer1='被雇用的', result='null', userAnswer='null',
            Lessonid=1, Explains='unemployed.txt', flag_ever_do=false}*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_word_list_gold);
        context = this ;
        ButterKnife.bind(this);
        lessonid = getIntent().getStringExtra("lessonid");
        player = new MediaPlayer();
        list = (List<AbilityQuestion.TestListBean>) getIntent().getExtras().getSerializable(ParaConstants.QUESTION_LIST_LABEL);
        for (AbilityQuestion.TestListBean bean : list) {
            Log.d("diao", "onCreate: " + bean.toString());
        }

        history.setVisibility(View.VISIBLE);
//        linearLayout.getBackground().setColorFilter(R2.attr.baseTheme, PorterDuff.Mode.SRC_ATOP);
        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout.post(new Runnable() {
            @Override
            public void run() {
                llheight = linearLayout.getMeasuredHeight();
//                if (checkAllFiles()) {
//                    for (int i = 0; i < list.size(); i++) {
//                        setWordContents(i);
//                    }
//                    finishDownload();
//                } else {
                startDownload();
//                }
            }
        });

        aviView.show();
        mRrAvi.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (finishDownload) {
            return super.dispatchTouchEvent(ev);
        } else {
            ToastUtil.showToast(context, "请在加载完成后操作~~");
            return false;
        }

    }

    private boolean checkAllFiles() {
        for (int i = 0; i < list.size(); i++) {
            File file = new File(FilePath.getTxtPath() +
                    list.get(i).getQuestion() + ".txt");
            if (!file.exists()) {
                return false;
            }
            if (i == list.size() - 1) {
                return true;
            }
        }
        return false;
    }

    private void startPronounce() {
        if (player.isPlaying()) {
            return;
        } else {
            player.reset();
        }

        String url = HttpUrls.GET_WORD_PRO + mLearningContents.get(position).getQuestion().getSounds();
//        String url = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constant.mListen + "/" + wheelView.getSeletedItem().en + ".mp3";
        try {
            player.setDataSource(url);
            Log.d("diao", "startPronounce: " + position);
            Log.d("diao", "startPronounce: " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();

            }
        });
    }

    private void startDownload() {
        String filePath = "";
        for (int i = 0; i < list.size(); i++) {
            for (int in = 0; in < 1; in++) {
                final int localInt = i;
                if (in == 0) {
                    url = HttpUrls.getAttach(context) + list.get(i).getQuestion() + ".txt";
                } else {
                    url = HttpUrls.GET_WORD_PRO + list.get(i).getSounds();
                }
                final int finalIn = in;
                if (in == 0) {
                    filePath = FilePath.getTxtPathSuffix();
                } else {
                    filePath = FilePath.getRecordPathSuffix();
                }
                DownloadUtil.get(context).download(url, filePath, new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        if (finalIn == 0) {
                            setWordContents(localInt);
                            finished++;
                        }
                        Log.d("diao", "onDownloadSuccess: " + url + "tt" + Thread.currentThread());
                        if (finished == list.size() && finalIn == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (finishDownload) {
                                        return;
                                    } else {
                                        finishDownload();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast(context, "下载失败");
                                Log.d("diao", "onDownloadFailed: " + url);
                                ((Activity) context).finish();
                            }
                        });
                    }
                });
            }
        }

    }

    private void setWordContents(int currentPosition) {
        List<String> s = FileUtils.ReadTxtFile(FilePath.getTxtPath() + list.get(currentPosition).getQuestion() + ".txt");
        LearningContent content = new LearningContent();
        content.question = list.get(currentPosition);
        content.phrases = new ArrayList<>();

        for (int i = 0; i < s.size(); i++) {
            content.en = s.get(TXT_WORD_EN).trim();
            if (i == TXT_WORD_CN) {
                content.cn = s.get(TXT_WORD_CN);
            }
            if (i == TXT_WORD_PRO) {
                content.pro = s.get(TXT_WORD_PRO);
            }
            if (i == TXT_WORD_CN_DETAIL) {
                content.cn_detail = s.get(TXT_WORD_CN_DETAIL);
            }
            if (i >= TXT_WORD_PHRASE) {
                content.phrases.add(s.get(i));
            }
            content.index = currentPosition;
            LogUtils.d(i + ":" + s.get(i));
        }
        mLearningContents.add(content);


    }


    private void finishDownload() {
        Log.d("diao", "finishDownload: ");
        Collections.sort(mLearningContents, new Comparator<LearningContent>() {
            @Override
            public int compare(LearningContent o1, LearningContent o2) {
                if (o1.index > o2.index) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        finishDownload = true;
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRrAvi.setVisibility(View.VISIBLE);
        aviView.hide();
        ll.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        adapter = new WordListAdapter(context, mLearningContents);
        //在这里初始化wheelview的content的高度
        wheelView.init(WheelView.TYPE_WORD, llheight);
        wheelView.setItems(mLearningContents);
        wheelView.getItemHeight();
        wheelView.setSeletion(0);
        LearningContent item = mLearningContents.get(0);
        mPhrase.setText("");
        mEn.setText(item.en);
        mCn.setText(item.cn_detail);
        mPro.setText(item.pro);
        for (int ii = 1; ii < item.phrases.size(); ii++) {
            mPhrase.append(item.phrases.get(ii));
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, LearningContent item) {
                position = selectedIndex - 1;
                mPhrase.setText("");
                mEn.setText(item.en);
                mCn.setText(item.cn_detail);
                mPro.setText(item.pro);
                fayin.setVisibility(View.VISIBLE);
                if (null == item.phrases) {
                    return;
                }
                for (int ii = 1; ii < item.phrases.size(); ii++) {
                    mPhrase.append(item.phrases.get(ii) + "");
                }
                super.onSelected(selectedIndex, item);
            }
        });

        wheelView.setOnScrollLisener(new WheelView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                fayin.setVisibility(View.INVISIBLE);
                mPhrase.setText("");
                mEn.setText("");
                mCn.setText("");
                mPro.setText("");
            }
        });
    }

    private void showConfirmDialog() {
        AlertDialog builder = new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        showConfirmDialog();
    }

    @OnClick(R2.id.back)
    public void onMBackClicked() {
        showConfirmDialog();
    }

    @OnClick(R2.id.textbtn)
    public void onMTextbtnClicked() {
        Intent intent = new Intent(context, WordHistoryActivity.class);
        intent.putExtra("lessonid", lessonid);
        startActivity(intent);
    }

    @OnClick(R2.id.start)
    public void onMStartClicked() {
        WordLearnActivity.start(context, mLearningContents, list);
        finish();
    }

    @OnClick(R2.id.go_test)
    public void onMGoTestClicked() {
        WordTestActivity.start(context, mLearningContents, list, lessonid);
        finish();
    }

    @OnClick(R2.id.fayin)
    public void onMFayinClicked() {
        startPronounce();
    }
}
