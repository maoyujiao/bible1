package com.iyuba.trainingcamp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.adapter.WordListAdapter;
import com.iyuba.trainingcamp.app.GoldApp;
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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author yq QQ:1032006226
 */
public class WordListActvity extends BaseActivity {

    WordListAdapter adapter;
    List<AbilityQuestion.TestListBean> list;

    private int finished;

    RecyclerView recyclerView;
    AVLoadingIndicatorView aviView;
    WheelView wheelView;
    ImageView back;
    TextView start;
    LinearLayout ll;
    LinearLayout linearLayout;
    ImageView fayin;
    MediaPlayer player;
    String lessonid ;
    boolean finishDownload = false ;

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
    private TextView history;

    private int position = 0 ;
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
        lessonid = getIntent().getStringExtra("lessonid");
        player = new MediaPlayer();
        list = (List<AbilityQuestion.TestListBean>) getIntent().getExtras().getSerializable(ParaConstants.QUESTION_LIST_LABEL);
        for (AbilityQuestion.TestListBean bean :list){
            Log.d("diao", "onCreate: " +bean.toString());
        }
        recyclerView = findViewById(R.id.recyclerView);
        start = findViewById(R.id.start);
        back = findViewById(R.id.back);
        ll = findViewById(R.id.llcontent);
        go_test = findViewById(R.id.go_test);
        ll.setVisibility(View.INVISIBLE);
        linearLayout = findViewById(R.id.ll);
        history = findViewById(R.id.textbtn);
        history.setVisibility(View.VISIBLE);
//        linearLayout.getBackground().setColorFilter(R.attr.baseTheme, PorterDuff.Mode.SRC_ATOP);
        linearLayout.setVisibility(View.INVISIBLE);
        fayin = findViewById(R.id.fayin);
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

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
        go_test.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WordTestActivity.start(context,mLearningContents,list,lessonid);
                        finish();
                    }
                }
        );
        context = this;
        wheelView = findViewById(R.id.wheelView);
        aviView = findViewById(R.id.avi);
        aviView.show();
        findViewById(R.id.rr_avi).setVisibility(View.VISIBLE);

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
        if (finishDownload){
            return super.dispatchTouchEvent(ev);
        }else {
            ToastUtil.showToast(context,"请在加载完成后操作~~");
            return  false;
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
            Log.d("diao", "startPronounce: "+position);
            Log.d("diao", "startPronounce: "+url);
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
        String filePath ="";
        for (int i = 0; i < list.size(); i++) {
            for (int in = 0; in < 1; in++) {
                final int localInt = i;
                if (in == 0) {
                    url = HttpUrls.getAttach(context) + list.get(i).getQuestion()+".txt";
                } else {
                    url = HttpUrls.GET_WORD_PRO + list.get(i).getSounds();
                }
                final int finalIn = in;
                if (in == 0){
                    filePath = FilePath.getTxtPathSuffix();
                }else {
                    filePath = FilePath.getRecordPathSuffix();
                }
                DownloadUtil.get(context).download(url, filePath, new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        if (finalIn == 0){
                            setWordContents(localInt);
                            finished++;
                        }
                        Log.d("diao", "onDownloadSuccess: " + url+"tt"+Thread.currentThread());
                        if (finished == list.size()&& finalIn == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (finishDownload){
                                        return;
                                    }else {
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
                    if (o1.index > o2.index){
                        return 1 ;
                    }else {
                        return -1;
                    }
                }
            });
        finishDownload = true;
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WordHistoryActivity.class);
                intent.putExtra("lessonid",lessonid);
                startActivity(intent);
            }
        });
        findViewById(R.id.rr_avi).setVisibility(View.VISIBLE);
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
        ((TextView) ll.findViewById(R.id.phrase)).setText("");
        ((TextView) ll.findViewById(R.id.en)).setText(item.en);
        ((TextView) ll.findViewById(R.id.cn)).setText(item.cn_detail);
        ((TextView) ll.findViewById(R.id.pro)).setText(item.pro);
        for (int ii = 1; ii < item.phrases.size(); ii++) {
            ((TextView) ll.findViewById(R.id.phrase)).append(item.phrases.get(ii));
        }
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, LearningContent item) {
                position = selectedIndex - 1;
                ((TextView) ll.findViewById(R.id.phrase)).setText("");
                ((TextView) ll.findViewById(R.id.en)).setText(item.en);
                ((TextView) ll.findViewById(R.id.cn)).setText(item.cn_detail);
                ((TextView) ll.findViewById(R.id.pro)).setText(item.pro);
                fayin.setVisibility(View.VISIBLE);
                if (null == item.phrases) {
                    return;
                }
                for (int ii = 1; ii < item.phrases.size(); ii++) {
                    ((TextView) ll.findViewById(R.id.phrase)).append(item.phrases.get(ii) + "");
                }
                super.onSelected(selectedIndex, item);
            }
        });

        wheelView.setOnScrollLisener(new WheelView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                fayin.setVisibility(View.INVISIBLE);
                ((TextView) ll.findViewById(R.id.phrase)).setText("");
                ((TextView) ll.findViewById(R.id.en)).setText("");
                ((TextView) ll.findViewById(R.id.cn)).setText("");
                ((TextView) ll.findViewById(R.id.pro)).setText("");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordLearnActivity.start(context,mLearningContents,list);
                finish();
            }
        });
        fayin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPronounce();
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
}
