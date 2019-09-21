package com.iyuba.CET4bible.listening;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.configation.Constant;
import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.listener.IMEListener;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.manager.BackgroundManager;
import com.iyuba.core.sqlite.mode.test.CetFillInBlank;
import com.iyuba.core.widget.BackPlayer;
import com.iyuba.core.widget.Player;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;

public class ListenCBlankFragment extends Fragment implements
        OnPlayStateChangedListener {
    private Context mContext;
    private View root;
    private int curPos;
    private boolean isSubmit;
    private ArrayList<CetFillInBlank> blanks;
    private Button previous, next, submit, questionSound;
    private BackPlayer mPlayer;
    private Player qsound;
    private TextView question, number;
    private EditText youranswer;
    private TextView rightanswer;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    setContent();
                    break;
                case 1:
                    previous.setBackgroundResource(R.drawable.previous_question);
                    break;
                case 2:
                    previous.setBackgroundResource(R.drawable.un_previous_question);
                    break;
                case 3:
                    next.setBackgroundResource(R.drawable.next_question);
                    break;
                case 4:
                    next.setBackgroundResource(R.drawable.next_question);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.listen_single_blank, container, false);
        mContext = RuntimeManager.getContext();
        init();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        isSubmit = args.getBoolean("submit");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
        mPlayer = BackgroundManager.Instace().bindService.getPlayer();
        qsound = new Player(mContext, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
        qsound.stop();
    }

    public void init() {
        blanks = ListenDataManager.Instance().blankList;
        previous = root.findViewById(R.id.preview);
        next = root.findViewById(R.id.next);
        submit = root.findViewById(R.id.submit);
        questionSound = root.findViewById(R.id.qsound);
        youranswer = root.findViewById(R.id.youranswer);
        rightanswer = root.findViewById(R.id.rightanswer);
        question = root.findViewById(R.id.blank);
        number = root.findViewById(R.id.number);
        rightanswer.setMovementMethod(ScrollingMovementMethod.getInstance());
        InputMethodManager input = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View outerLayout = root.findViewById(R.id.backlayout);
        outerLayout.setOnTouchListener(new IMEListener(input, false));
        initListener();
        if (isSubmit) {
            rightanswer.setVisibility(View.VISIBLE);
        }
        handler.sendEmptyMessage(0);
    }

    private void initListener() {

        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                preview();
            }
        });
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                next();
            }
        });
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                rightanswer.setVisibility(View.VISIBLE);
            }
        });
        questionSound.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                }
                qsound.playUrl(Constant.videoAddr
                        + ListenDataManager.Instance().year + File.separator
                        + blanks.get(curPos).sound);
            }
        });

    }

    private void preview() {
        if (isSubmit) {
            rightanswer.setVisibility(View.VISIBLE);
        } else {
            rightanswer.setVisibility(View.GONE);
        }
        if (curPos > 0) {
            curPos--;
            handler.sendEmptyMessage(0);
            handler.sendEmptyMessage(1);
        }
        if (curPos == blanks.size() - 2) {
            handler.sendEmptyMessage(4);
        }
        if (curPos == 0) {
            handler.sendEmptyMessage(2);
        }
    }

    private void next() {
        if (isSubmit) {
            rightanswer.setVisibility(View.VISIBLE);
        } else {
            rightanswer.setVisibility(View.GONE);
        }
        if (curPos < blanks.size() - 1) {
            handler.sendEmptyMessage(4);
            curPos++;
            handler.sendEmptyMessage(0);
        }
        if (curPos == blanks.size() - 1) {
            handler.sendEmptyMessage(3);
        }
        if (curPos == 1) {
            handler.sendEmptyMessage(1);
        }
    }

    private void setContent() {
        CetFillInBlank blank = blanks.get(curPos);
        youranswer.getText().clear();
        if (!blank.yourAnswer.equals("")) {
            youranswer.setText(blank.yourAnswer);
        }
        number.setText("第" + blank.id + "题");
        question.setText(blank.question);
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append("<font color='")
                .append(getResources().getColor(R.color.app_color))
                .append("'>");
        sbBuffer.append(mContext.getString(R.string.answer)).append(":")
                .append("</font>&emsp;");
        sbBuffer.append(blank.answer).append("<br/>");
        if (blank.keyword1 != null && !blank.keyword1.equals("null")) {
            sbBuffer.append("<font color='")
                    .append(getResources().getColor(R.color.app_color))
                    .append("'>");
            sbBuffer.append(mContext.getString(R.string.keys)).append(
                    "</font>&emsp;");
            sbBuffer.append(blank.keyword1).append(';').append("&emsp;")
                    .append(blank.keyword2).append(';').append("&emsp;")
                    .append(blank.keyword3).append(';').append("&emsp;");
        }
        rightanswer.setText(Html.fromHtml(sbBuffer.toString()));
    }

    @Override
    public void playCompletion() {

        if (mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
        qsound.pause();
        qsound.reset();
    }

    @Override
    public void playFaild() {


    }
}
