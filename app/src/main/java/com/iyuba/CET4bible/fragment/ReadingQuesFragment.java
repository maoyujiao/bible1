package com.iyuba.CET4bible.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.ReadingAnswer;
import com.iyuba.CET4bible.sqlite.mode.ReadingExplain;
import com.iyuba.CET4bible.util.FavoriteUtil;

import java.util.ArrayList;
import java.util.List;

public class ReadingQuesFragment extends Fragment implements OnClickListener {
    public Context context;
    public int curPosition;
    public List<ReadingAnswer> readingAnswers;
    public List<ReadingExplain> readingExplains;
    public boolean isSubmit = false;
    public String[] answers;
    private View root;
    private TextView tv_question;
    private TextView tv_answera, tv_answerb, tv_answerc, tv_answerd;
    private ImageView answeraImage, answerbImage, answercImage, answerdImage;
    private RelativeLayout rl_answera;
    private RelativeLayout rl_answerb;
    private RelativeLayout rl_answerc;
    private RelativeLayout rl_answerd;
    private boolean isShowExplain;
    private RelativeLayout rl_submitAnswer;
    private ArrayList<View> answerImages = new ArrayList<View>();
    private TextView tv_right_answer;
    private TextView tv_explain;
    private RelativeLayout rl_explain;
    private TextView tv_submitAnswer;


    // 收藏
    private CheckBox cbFavorite;
    private FavoriteUtil favoriteUtil;
    private String favoritePrefix = "reading";

    public static ReadingQuesFragment newInstance(Context context, int position,
                                                  List<ReadingAnswer> readingAnswers, List<ReadingExplain> readingExplains) {
        ReadingQuesFragment readingFragment = new ReadingQuesFragment();
        readingFragment.context = context;
        readingFragment.curPosition = position;
        readingFragment.readingAnswers = readingAnswers;
        readingFragment.readingExplains = readingExplains;
        return readingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ReadingQuesFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("ReadingQuesFragment", "oncreateView");
        root = inflater.inflate(R.layout.ques_fragment, null);
        initView();
        initData();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ReadingQuesFragment", "onDestory");
    }

    /**
     * 初始化控件
     */
    private void initView() {
        cbFavorite = root.findViewById(R.id.cb_favorite);

        tv_question = root.findViewById(R.id.tv_question);
        //问题
        tv_answera = root.findViewById(R.id.tv_answera);
        tv_answerb = root.findViewById(R.id.tv_answerb);
        tv_answerc = root.findViewById(R.id.tv_answerc);
        tv_answerd = root.findViewById(R.id.tv_answerd);
        rl_answera = root.findViewById(R.id.rl_answera);
        rl_answerb = root.findViewById(R.id.rl_answerb);
        rl_answerc = root.findViewById(R.id.rl_answerc);
        rl_answerd = root.findViewById(R.id.rl_answerd);
        answeraImage = root.findViewById(R.id.answera_default);
        answerbImage = root.findViewById(R.id.answerb_default);
        answercImage = root.findViewById(R.id.answerc_default);
        answerdImage = root.findViewById(R.id.answerd_default);
        rl_submitAnswer = root.findViewById(R.id.rl_submitAnswer);
        tv_right_answer = root.findViewById(R.id.tv_right_answer);
        tv_explain = root.findViewById(R.id.tv_explain);
        rl_explain = root.findViewById(R.id.rl_explain);
        tv_submitAnswer = root.findViewById(R.id.tv_submitAnswer);

        rl_answera.setOnClickListener(this);
        rl_answerb.setOnClickListener(this);
        rl_answerc.setOnClickListener(this);
        rl_answerd.setOnClickListener(this);
        /*answeraImage.setOnClickListener(this);
        answerbImage.setOnClickListener(this);
		answercImage.setOnClickListener(this);
		answerdImage.setOnClickListener(this);*/

        rl_submitAnswer.setOnClickListener(this);

        answerImages.clear();
        answerImages.add(answeraImage);
        answerImages.add(answerbImage);
        answerImages.add(answercImage);
        answerImages.add(answerdImage);
        showUserAnswer();
    }

    //显示当前用户答案
    private void showUserAnswer() {
        //是否已经提交过答案
        if (!isSubmit) {
            switch (readingAnswers.get(curPosition).curAnswer) {
                case 1:
                    rl_answera.performClick();
                    break;
                case 2:
                    rl_answerb.performClick();
                    break;
                case 3:
                    rl_answerc.performClick();
                    break;
                case 4:
                    rl_answerd.performClick();
                    break;
                default:
                    break;
            }
        } else {
            showAnswerAndExplain();
        }
    }

    /**
     * 填充视图
     */
    private void initData() {
        SpannableString ss = getSpannableString(readingAnswers.get(curPosition).QuesIndex, readingAnswers.get(curPosition).QuesText);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "font/times.ttf");
        tv_question.setTextSize(18);
        tv_question.setTypeface(type);
        tv_question.setText(ss);
        //解析出试题  text++text++text++text
        answers = getAnswers();
        if (answers != null && answers.length > 0) {
            tv_answera.setTextSize(18);
            tv_answera.setTypeface(type);
            tv_answera.setText(answers[0]);
            tv_answerb.setTextSize(18);
            tv_answerb.setTypeface(type);
            tv_answerb.setText(answers[1]);
            tv_answerc.setTextSize(18);
            tv_answerc.setTypeface(type);
            tv_answerc.setText(answers[2]);
            tv_answerd.setTextSize(18);
            tv_answerd.setTypeface(type);
            tv_answerd.setText(answers[3]);
        }


        favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.reading);
        final ReadingAnswer answer = readingAnswers.get(curPosition);
        cbFavorite.setChecked(favoriteUtil.isFavorite(getKey(answer)));
        cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favoriteUtil.setFavorite(isChecked, getKey(answer));
            }
        });
    }

    @NonNull
    private String getKey(ReadingAnswer answer) {
        return favoritePrefix + "_" + answer.TitleNum + "_" + answer.QuesIndex;
    }

    private SpannableString getSpannableString(int quesIndex, String quesText) {
        SpannableString ss = new SpannableString(String.valueOf(quesIndex) + ". " + quesText);
        ss.setSpan(new ForegroundColorSpan(Color.rgb(245, 97, 45)), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private String[] getAnswers() {
        String[] strs = readingAnswers.get(curPosition).AnswerText.split("\\++");
        return strs;
    }

    @Override
    public void onClick(View v) {
        setAnswerBg(v);
    }

    /**
     * 给题目设置背景
     */
    private void setAnswerBg(View v) {
        if (v.getId() == R.id.rl_answera || v.getId() == R.id.answera_default) {
            readingAnswers.get(curPosition).curAnswer = 1;
            rl_answera.setBackgroundResource(R.drawable.answer_bg);
            rl_answerb.setBackgroundResource(0);
            rl_answerc.setBackgroundResource(0);
            rl_answerd.setBackgroundResource(0);
            answeraImage.setBackgroundResource(R.drawable.shape_reading_ques_answer_checked);
            answerbImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answercImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerdImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
        }
        if (v.getId() == R.id.rl_answerb || v.getId() == R.id.answerb_default) {
            readingAnswers.get(curPosition).curAnswer = 2;
            rl_answera.setBackgroundResource(0);
            rl_answerb.setBackgroundResource(R.drawable.answer_bg);
            rl_answerc.setBackgroundResource(0);
            rl_answerd.setBackgroundResource(0);
            answeraImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerbImage.setBackgroundResource(R.drawable.shape_reading_ques_answer_checked);
            answercImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerdImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
        }
        if (v.getId() == R.id.rl_answerc || v.getId() == R.id.answerc_default) {
            readingAnswers.get(curPosition).curAnswer = 3;
            rl_answera.setBackgroundResource(0);
            rl_answerb.setBackgroundResource(0);
            rl_answerc.setBackgroundResource(R.drawable.answer_bg);
            rl_answerd.setBackgroundResource(0);
            answeraImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerbImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answercImage.setBackgroundResource(R.drawable.shape_reading_ques_answer_checked);
            answerdImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
        }
        if (v.getId() == R.id.rl_answerd || v.getId() == R.id.answerd_default) {
            readingAnswers.get(curPosition).curAnswer = 4;
            rl_answera.setBackgroundResource(0);
            rl_answerb.setBackgroundResource(0);
            rl_answerc.setBackgroundResource(0);
            rl_answerd.setBackgroundResource(R.drawable.answer_bg);
            answeraImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerbImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answercImage.setBackgroundResource(R.drawable.shape_reading_ques_answer);
            answerdImage.setBackgroundResource(R.drawable.shape_reading_ques_answer_checked);
        }
        if (v.getId() == R.id.rl_submitAnswer) {
            //已经提交
            isSubmit = true;
            //判断答案是否正确
            showAnswerAndExplain();
        }
    }

    private void showAnswerAndExplain() {
        /**
         * 显示答案
         */
        ReadingAnswer curReadAnswer = readingAnswers.get(curPosition);
        ReadingExplain curReadExplain = readingExplains.get(curPosition);

        switch (curReadAnswer.Answer) {
            case 1:
                answeraImage.setBackgroundResource(R.drawable.ans_right);
                rl_answera.setBackgroundResource(R.drawable.answer_bg);
                rl_answerb.setBackgroundResource(0);
                rl_answerc.setBackgroundResource(0);
                rl_answerd.setBackgroundResource(0);
                break;
            case 2:
                answerbImage.setBackgroundResource(R.drawable.ans_right);
                rl_answera.setBackgroundResource(0);
                rl_answerb.setBackgroundResource(R.drawable.answer_bg);
                rl_answerc.setBackgroundResource(0);
                rl_answerd.setBackgroundResource(0);
                break;
            case 3:
                answercImage.setBackgroundResource(R.drawable.ans_right);
                rl_answera.setBackgroundResource(0);
                rl_answerb.setBackgroundResource(0);
                rl_answerc.setBackgroundResource(R.drawable.answer_bg);
                rl_answerd.setBackgroundResource(0);
                break;
            case 4:
                answerdImage.setBackgroundResource(R.drawable.ans_right);
                rl_answera.setBackgroundResource(0);
                rl_answerb.setBackgroundResource(0);
                rl_answerc.setBackgroundResource(0);
                rl_answerd.setBackgroundResource(R.drawable.answer_bg);
                break;
            default:
                break;
        }
        if (curReadAnswer.curAnswer != curReadAnswer.Answer) {
            if (curReadAnswer.curAnswer != -1) {
                answerImages.get(curReadAnswer.curAnswer - 1).setBackgroundResource(R.drawable.answer_error);
            }
        }
        /**
         * 显示解析
         */
        rl_explain.setVisibility(View.VISIBLE);
        switch (curReadAnswer.Answer) {
            case 1:
                tv_right_answer.setText("A");
                tv_right_answer.setTextColor(Color.rgb(246, 93, 40));
                tv_explain.setText(curReadExplain.Explain);
                tv_explain.setTextColor(Color.rgb(246, 93, 40));
                break;
            case 2:
                tv_right_answer.setText("B");
                tv_right_answer.setTextColor(Color.rgb(246, 93, 40));
                tv_explain.setText(curReadExplain.Explain);
                tv_explain.setTextColor(Color.rgb(246, 93, 40));
                break;
            case 3:
                tv_right_answer.setText("C");
                tv_right_answer.setTextColor(Color.rgb(246, 93, 40));
                tv_explain.setText(curReadExplain.Explain);
                tv_explain.setTextColor(Color.rgb(246, 93, 40));
                break;
            case 4:
                tv_right_answer.setText("D");
                tv_right_answer.setTextColor(Color.rgb(246, 93, 40));
                tv_explain.setText(curReadExplain.Explain);
                tv_explain.setTextColor(Color.rgb(246, 93, 40));
                break;
            default:
                break;
        }
        //将控件设置成不可点击
        disableView();


    }

    private void disableView() {
        rl_answera.setClickable(false);
        rl_answerb.setClickable(false);
        rl_answerc.setClickable(false);
        rl_answerd.setClickable(false);
        tv_submitAnswer.setText("已提交答案");
        rl_submitAnswer.setClickable(false);
        rl_submitAnswer.setBackgroundResource(R.drawable.bg_submit_press);


    }
}

