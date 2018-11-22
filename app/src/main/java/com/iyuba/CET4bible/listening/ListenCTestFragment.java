package com.iyuba.CET4bible.listening;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.CET4bible.widget.subtitle.Subtitle;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSum;
import com.iyuba.CET4bible.widget.subtitle.SubtitleSynView;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.sqlite.mode.test.CetFillInBlank;
import com.iyuba.core.sqlite.mode.test.CetText;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListenCTestFragment extends Fragment {
    private Context mContext;
    private View root;
    private SubtitleSynView original;
    private SubtitleSum subtitleSum;
    private ArrayList<CetFillInBlank> blankList;
    private Button sure;
    private TextView submit, question;
    private View youranswer;
    private EditText answer;
    private int questionNumber;
    private OnBackPressListener mCallback;
    private int type;
    private TextPageSelectTextCallBack tp = new TextPageSelectTextCallBack() {
        @Override
        public void selectTextEvent(String selectText) {

            if (selectText.matches("^\\(\\d{2}\\)_*.*")) {
                questionNumber = getNumber(selectText);
                if (blankList.get(questionNumber).yourAnswer.equals("")) {
                    answer.getText().clear();
                } else {
                    answer.setText(blankList.get(questionNumber).yourAnswer);
                }
                question.setText(blankList.get(questionNumber).id + ".");
                youranswer.setVisibility(View.VISIBLE);
                answer.requestFocus();
            }
        }

        @Override
        public void selectParagraph(int paragraph) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.listen_c_test, container, false);
        mContext = RuntimeManager.getContext();
        init();
        return root;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.getBoolean("submit")) {
            type = 1;
        } else {
            type = 0;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnBackPressListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
        ListenDataManager.Instance().blankList = blankList;
    }

    public void init() {
        blankList = ListenDataManager.Instance().blankList;
        setSubtitleSum(type);
        original = root.findViewById(R.id.original);
        original.setSubtitleSum(subtitleSum);
        original.setTpstcb(tp);
        original.setSyncho(false);
        submit = root.findViewById(R.id.submit);
        sure = root.findViewById(R.id.sure);
        answer = root.findViewById(R.id.answer);
        youranswer = root.findViewById(R.id.youranswer);
        youranswer.setVisibility(View.GONE);
        question = root.findViewById(R.id.question_number);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                type = 1;
                setSubtitleSum(type);
                mCallback.onSubmit(true);
                original.setSubtitleSum(subtitleSum);
            }
        });
        sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                type = 0;
                blankList.get(questionNumber).yourAnswer = answer.getText()
                        .toString();
                setSubtitleSum(type);
                original.setSubtitleSum(subtitleSum);
                youranswer.setVisibility(View.GONE);
            }
        });
        answer.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final boolean isFocus = hasFocus;
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) answer
                                .getContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        if (isFocus) {
                            imm.toggleSoftInput(0,
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        } else {
                            imm.hideSoftInputFromWindow(
                                    answer.getWindowToken(), 0);
                        }
                    }
                }, 300);
            }
        });
        original.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keycode, KeyEvent arg2) {

                if (keycode == KeyEvent.KEYCODE_BACK) {
                    if (youranswer.isShown()) {
                        youranswer.setVisibility(View.GONE);
                        mCallback.onPresseded(true);
                    } else {
                        mCallback.onPresseded(false);
                    }
                }
                return false;
            }
        });
    }

    private void setSubtitleSum(int type) {
        subtitleSum = new SubtitleSum();
        if (subtitleSum.subtitles == null) {
            subtitleSum.subtitles = new ArrayList<Subtitle>();
            subtitleSum.subtitles.clear();
        }
        setDetail(type);
    }

    private void setDetail(int type) {
        ArrayList<CetText> textList = ListenDataManager.Instance().textList;
        int size = textList.size();
        Subtitle st;
        StringBuffer sb;
        int j = 0;
        for (int i = 0; i < size; i++) {
            st = new Subtitle();
            sb = new StringBuffer();
            sb.append("\t");
            if (textList.get(i).qwords.equals("0")) {
                sb.append(textList.get(i).sentence);
                st.content = sb.toString();
                subtitleSum.subtitles.add(st);
            } else {
                if (type == 0) {
                    sb.append(fillInAnswer(j));
                } else {
                    sb.append(fillInRightAnswer(j));
                }
                st.content = sb.toString();
                st.isHtml = true;
                subtitleSum.subtitles.add(st);
                j++;
            }
        }
    }

    private int getNumber(String question) {

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(question);
        String trim = m.replaceAll("").trim();
        for (int i = 0; i < blankList.size(); i++) {
            if (blankList.get(i).id.equals(trim)) {
                return i;
            }
        }
        return 0;
    }

    private String getColorString(int curPos) {
        String youranswer = blankList.get(curPos).yourAnswer;
        StringBuffer sb = new StringBuffer();
        sb.append('(').append(blankList.get(curPos).id).append(')')
                .append("<u>").append("<font color='")
                .append(getResources().getColor(R.color.text_highlight))
                .append("'>").append("&nbsp;&nbsp;").append(youranswer)
                .append("&nbsp;&nbsp;").append("</font>").append("</u>");
        return sb.toString();
    }

    private String getColorfulString(int curPos) {
        String youranswer = blankList.get(curPos).yourAnswer;
        String rightanswer = blankList.get(curPos).answer;
        StringBuffer sb = new StringBuffer();
        if (rightanswer.equals(youranswer)) {
            sb.append('(').append(blankList.get(curPos).id).append(')')
                    .append("<u>").append("<font color='#08d816'>")
                    .append("&nbsp;&nbsp;").append(youranswer)
                    .append("&nbsp;&nbsp;").append("</font>").append("</u>");
        } else {
            sb.append('(').append(blankList.get(curPos).id).append(')')
                    .append("<u>").append("<font color='#fa4b4b'>");
            if (youranswer.equals("")) {
                sb.append("&nbsp;&nbsp;").append("NA").append("&nbsp;&nbsp;");
            } else {
                sb.append("&nbsp;&nbsp;").append(youranswer)
                        .append("&nbsp;&nbsp;");
            }
            sb.append("</font>").append('/').append("<font color='#08d816'>")
                    .append("&nbsp;&nbsp;").append(rightanswer)
                    .append("&nbsp;&nbsp;").append("</font>").append("</u>");
        }
        return sb.toString();
    }

    private String fillInAnswer(int curPos) {
        String question = blankList.get(curPos).question;
        String yourAnswer = blankList.get(curPos).yourAnswer;
        if (yourAnswer.equals("")) {
            return question;
        } else {
            String[] group = question.split(" ");
            StringBuffer fillInQuestion = new StringBuffer();
            for (String string : group) {
                if (string.matches("^\\(\\d{2}\\)_*.*")) {
                    String regEx = "^\\(\\d{2}\\)_*";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(string);
                    string = getColorString(curPos);
                    fillInQuestion.append(string)
                            .append(m.replaceAll("").trim()).append(' ');
                } else {
                    fillInQuestion.append(string).append(' ');
                }
            }
            return fillInQuestion.toString();
        }
    }

    private String fillInRightAnswer(int curPos) {
        String question = blankList.get(curPos).question;
        String[] group = question.split(" ");
        StringBuffer fillInQuestion = new StringBuffer();
        for (String string : group) {
            if (string.matches("\\(\\d{2}\\)_*.*")) {
                String regEx = "^\\(\\d{2}\\)_*";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(string);
                string = getColorfulString(curPos);
                fillInQuestion.append(string).append(m.replaceAll("").trim())
                        .append(' ');
            } else {
                fillInQuestion.append(string).append(' ');
            }
        }
        return fillInQuestion.toString();
    }

    public interface OnBackPressListener {
        void onPresseded(boolean press);

        void onSubmit(boolean submit);
    }
}
