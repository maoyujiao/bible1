package com.iyuba.trainingcamp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.bean.TestResultBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecord;
import com.iyuba.trainingcamp.db.dbclass.GoldDateRecordHelper;
import com.iyuba.trainingcamp.http.DownloadUtil;
import com.iyuba.trainingcamp.http.HttpUrls;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.DateUtils;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.ParaConstants;
import com.iyuba.trainingcamp.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yq QQ:1032006226
 */
public class ExamActivity extends BaseActivity implements View.OnClickListener {
    private String answer = "";
    private TextView testIndex;
    private ImageView play;
    private TextView mAnswerA_root;
    private TextView mAnswerB_root;
    private TextView mAnswerC_root;
    private TextView mAnswerD_root;
    private Button mUserEditNxt;
    Context mContext;
    String lessonid;
    DailyWordDBHelper mHelper;
    List<AbilityQuestion.TestListBean> list;
    List<AbilityQuestion.TestListBean> wrongList = new ArrayList<>();
    EditText userEdit ;

    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;
    int playState = STOP;

    AbilityQuestion.TestListBean bean;
    private MediaPlayer player;
    int position;
    private TextView curPosition, tvDuration;
    private SeekBar mSeekBar;
    private int duration;
    private int right;
    private TextView questionText;

    private void bindViews() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        testIndex = findViewById(R.id.position);
        play = findViewById(R.id.play);
        mAnswerA_root = findViewById(R.id.answerA_root);
        mAnswerB_root = findViewById(R.id.answerB_root);
        mAnswerC_root = findViewById(R.id.answerC_root);
        mAnswerD_root = findViewById(R.id.answerD_root);
        questionText = findViewById(R.id.question);
        curPosition = findViewById(R.id.curren_pos);
        tvDuration = findViewById(R.id.duration);
        userEdit = findViewById(R.id.user_edit);
        mUserEditNxt = findViewById(R.id.user_edit_next);
        curPosition.setText("00:00");
        tvDuration.setText("00:00");
        mSeekBar = findViewById(R.id.sb_player_seek_bar);
        mAnswerA_root.setOnClickListener(this);
        mAnswerB_root.setOnClickListener(this);
        mAnswerC_root.setOnClickListener(this);
        mAnswerD_root.setOnClickListener(this);
        mUserEditNxt.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        play.setOnClickListener(this);
        mContext = this;
        player = new MediaPlayer();
        mSeekBar.setMax(1000);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress * duration / 1000);
                    player.start();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sendEmptyMessageDelayed(10, 1000);
            curPosition.setText(TimeUtils.formatTime(player.getCurrentPosition()));
            if (player!= null){
                mSeekBar.setProgress(player.getCurrentPosition() * 1000 / duration);
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingcamp_exam_activity);
        bindViews();
        mHelper = new DailyWordDBHelper(this);
        lessonid  =  getIntent().getStringExtra("lessonid");

        list = (List<AbilityQuestion.TestListBean>) getIntent().getSerializableExtra(ParaConstants.QUESTION_LIST_LABEL);
        for (int i = 0; i < list.size(); i++) {
            File file = new File(FilePath.getTxtPath() + list.get(i).Explains);
            if (!file.exists()) {
                downloadAttach();
                break;
            }
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.pause();
                player.seekTo(0);
                mSeekBar.setProgress(0);
                play.setImageResource(R.drawable.trainingcamp_play_white);
            }
        });
        refreshUI(position);
        startAudio();
    }

    private void refreshUI(int position) {

        bean = list.get(position);
        Log.d("diao", "refreshUI: " + bean.toString());
        testIndex.setText((position + 1) + "/" + list.size());
        setMultipuleVisible(View.VISIBLE);
        TypedArray a = obtainStyledAttributes(R.styleable.TrainingTheme);
        @SuppressLint("ResourceAsColor")
        int col = a.getColor(0, R.color.trainingcamp_theme_color); //icoid 指的是所需要的drwable在Mytheme的实际index        // 回收
        a.recycle();
        mAnswerD_root.setTextColor(col);
        mAnswerC_root.setTextColor(col);
        mAnswerB_root.setTextColor(col);
        mAnswerA_root.setTextColor(col);
        userEdit.setVisibility(View.GONE);
        mAnswerA_root.setText("A. " + bean.getAnswer1());
        mAnswerB_root.setText("B. " + bean.getAnswer2());
        mAnswerC_root.setText("C. " + bean.getAnswer3());
        mAnswerD_root.setText("D. " + bean.getAnswer4());
        mAnswerA_root.setBackgroundResource(R.drawable.trainingcamp_rect_exam);
        mAnswerB_root.setBackgroundResource(R.drawable.trainingcamp_rect_exam);
        mAnswerC_root.setBackgroundResource(R.drawable.trainingcamp_rect_exam);
        mAnswerD_root.setBackgroundResource(R.drawable.trainingcamp_rect_exam);
        setClickable(true);
        questionText.setVisibility(View.VISIBLE);
        mUserEditNxt.setVisibility(View.GONE);
        userEdit.setVisibility(View.GONE);

        questionText.setText(bean.getQuestion());
        if (bean.getTestType() == 8) { //单词拼写
            questionText.setVisibility(View.VISIBLE);
            questionText.setText(bean.getQuestion());
            questionText.append("请在下方填写正确的单词");
            setMultipuleVisible(View.GONE);
            userEdit.setVisibility(View.VISIBLE);
            mUserEditNxt.setVisibility(View.VISIBLE);
        }
        if (bean.getTestType() == 7 || bean.getAnswer3() == null) {
            mAnswerC_root.setVisibility(View.INVISIBLE);
            mAnswerD_root.setVisibility(View.INVISIBLE);
        }
    }

    private void setMultipuleVisible(int i) {
        mAnswerA_root.setVisibility(i);
        mAnswerB_root.setVisibility(i);
        mAnswerC_root.setVisibility(i);
        mAnswerD_root.setVisibility(i);
    }

    private void setClickable(Boolean clickable) {
        mAnswerD_root.setClickable(clickable);
        mAnswerC_root.setClickable(clickable);
        mAnswerB_root.setClickable(clickable);
        mAnswerA_root.setClickable(clickable);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.answerA_root) {
            answer = "A";
            compareRight((TextView) v);
        } else if (i == R.id.answerB_root) {
            answer = "B";
            compareRight((TextView) v);

        } else if (i == R.id.answerC_root) {
            answer = "C";
            compareRight((TextView) v);

        } else if (i == R.id.answerD_root) {
            answer = "D";
            compareRight((TextView) v);

        }
        if (i == R.id.back) {
            showConfirmDialog(1);
        } else if (i == R.id.play) {
            if (player == null){
                player = new MediaPlayer();
            }
            if (player.isPlaying()) {
                player.pause();
                play.setImageResource(R.drawable.trainingcamp_play_white);
                playState = PAUSE;
                mHandler.removeCallbacksAndMessages(null);
            } else if (playState == PAUSE) {
//                player.seekTo(duration * mSeekBar.getProgress() / 100);
                play.setImageResource(R.drawable.trainingcamp_pause_white);
                player.start();
                playState = PLAYING;
                mHandler.sendEmptyMessage(100);
            } else if (playState == STOP) {
                startAudio();
                mSeekBar.setProgress(0);
            }
        }else if (i == R.id.user_edit_next){
            position++ ;
            refreshUI(position);
        }
    }

    // 与正确答案比较并进入下一题
    private void compareRight(TextView view) {
        setClickable(false);
        list.get(position).setUserAnswer(answer);
        if (answer.equals(getRight())) {
            list.get(position).setResult("1");
            right++;

        } else {
            list.get(position).setResult("0");
            wrongList.add(list.get(position));
        }
        refreshColorUI(view);
        position++;
        if (gotoShareActivity()) return;

        if (position!=list.size()){
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshUI(position);
                    startAudio();
                }
            }, 600);
        }

    }



    private void refreshColorUI(TextView view) {
        view.setBackgroundResource(R.drawable.trainingcamp_rect_exam_press);
        view.setTextColor(Color.WHITE);
    }

    private boolean gotoShareActivity() {
        if (position == list.size()) {
            if (right*100/list.size()<=60){
                showAlert();
                return false;
            }else {
                startScoreActivity();
            }
            return true;
        }
        return false;
    }
    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您目前的得分为"+right*100/list.size()+",需要达到60分才能进入下一关~~").setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setPositiveButton("重新闯关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                right = 0 ;
                position = 0;
                refreshUI(position);
                startAudio();
            }
        }).setCancelable(false);
        builder.show();

    }
    // 下载解析的附件
    private void downloadAttach() {
         for (int i = 0; i < list.size(); i++) {
            final int temp = i;
            final int[] length = {0};
            DownloadUtil.get(mContext).download(HttpUrls.getAttach(this) + list.get(temp).Explains,
                    FilePath.getTxtPathSuffix(), new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess() {
                            length[0]++;
                            if (length[0] == list.size()) {

                            }
                        }

                        @Override
                        public void onDownloading(int progress) {

                        }

                        @Override
                        public void onDownloadFailed() {

                        }
                    });
        }
    }

    private void setExamScore(String s) {
        GoldDateRecord goldDateRecord = mHelper.selectDataById(GoldApp.getApp(mContext).userId,ACache.get(this).getAsString("id"));
        if (goldDateRecord == null ){
            Log.d("diao", "setSentenceScore: null");
        }else {
            mHelper.updateUpdateExamScore(GoldApp.getApp(mContext).userId,s, ACache.get(this).getAsString("id"));
        }
    }




    private void startScoreActivity() {
        TestResultBean.getBean().examScore = right * 100 / list.size();
        setExamScore("" + TestResultBean.getBean().examScore);
        ScoreActivity.start(mContext, lessonid, null,list,2,"" + TestResultBean.getBean().examScore);

    }

    private String getRight() {
        return list.get(position).getAnswer();
    }

    private void showConfirmDialog(int i) {
        if (i == 1) {
            new AlertDialog.Builder(this).setMessage(getResources().getString(R.string.trainingcamp_sure_to_quit_study))
                    .setNegativeButton(getResources().getString(R.string.trainingcamp_continue_study), null)
                    .setPositiveButton(getResources().getString(R.string.trainingcamp_exit_study), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        }
    }

    private void startAudio() {
        if (player == null ){
            player = new MediaPlayer();
        }
        if (player != null && player.isPlaying()) {
            player.stop();
            player.reset();
        } else if (player != null && !player.isPlaying()){
            player.reset();
        } else {
            player = new MediaPlayer();
            player.reset();
        }
        String url = "http://static2.iyuba.com/" + GoldApp.getApp(mContext).getLessonType() + "/sounds/" + list.get(position).getSounds();

        try {
            player.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mHandler == null) {
                    return;
                }
                if (player == null) {
                    return;
                }
                player.start();
                play.setImageResource(R.drawable.trainingcamp_pause_white);
                mHandler.sendEmptyMessage(10);

                duration = player.getDuration();
                tvDuration.setText(TimeUtils.formatTime(duration));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacksAndMessages(null);
        if (player != null) {
            player.pause();
            playState = PAUSE;
            play.setImageResource(R.drawable.trainingcamp_play_white);
//            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }
        mHandler = null;
    }

//    public static void keepScreenLongLight(Activity activity) {
//        boolean isOpenLight = CommSharedUtil.getInstance(activity).getBoolean(CommSharedUtil.FLAG_IS_OPEN_LONG_LIGHT, true);
//        Window window = activity.getWindow();
//        if (isOpenLight) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
//
//    }

}
