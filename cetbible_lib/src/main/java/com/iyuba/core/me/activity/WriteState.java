package com.iyuba.core.me.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.BasisActivity;
import com.iyuba.core.activity.CrashApplication;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.sqlite.mode.Emotion;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestUpdateState;
import com.iyuba.core.protocol.message.ResponseUpdateState;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.dialog.CustomToast;

/**
 * 更改心情
 *
 * @author 陈彤
 * @version 1.0
 */
public class WriteState extends BasisActivity {
    private Context mContext;
    private Button backBtn;
    private EditText content;
    private Button send;
    private TextView count;
    private ImageView face;
    private int maxLength = 60;// 最多输入60个字
    private RelativeLayout rlShow;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CustomToast.showToast(mContext, R.string.me_over_fix);
                    break;
                case 1:
                    CustomToast.showToast(mContext, R.string.me_state_success);
                    onBackPressed();
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.me_state_fail);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
            }
        }
    };
    private int[] imageIds = new int[30];
    private GridView emotion_GridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_state);
        CrashApplication.getInstance().addActivity(this);
        mContext = this;
        backBtn = findViewById(R.id.button_back);
        content = findViewById(R.id.content);
        send = findViewById(R.id.submit);
        count = findViewById(R.id.fix);
        count.setText(String.valueOf(maxLength));
        rlShow = findViewById(R.id.rl_show);
        emotion_GridView = rlShow.findViewById(R.id.grid_emotion);
        face = findViewById(R.id.face);
        face.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (rlShow.getVisibility() == View.GONE) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(WriteState.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    rlShow.setVisibility(View.VISIBLE);
                    initEmotion();
                    emotion_GridView.setVisibility(View.VISIBLE);
                    emotion_GridView
                            .setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int arg2, long arg3) {
                                    Bitmap bitmap = null;
                                    bitmap = BitmapFactory.decodeResource(
                                            getResources(), imageIds[arg2
                                                    % imageIds.length]);
                                    ImageSpan imageSpan = new ImageSpan(
                                            mContext, bitmap);
                                    String str = "image" + arg2;
                                    SpannableString spannableString = new SpannableString(
                                            str);
                                    String str1 = Emotion.express[arg2];
                                    SpannableString spannableString1 = new SpannableString(
                                            str1);
                                    if (str.length() == 6) {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        6,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else if (str.length() == 7) {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        7,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } else {
                                        spannableString
                                                .setSpan(
                                                        imageSpan,
                                                        0,
                                                        5,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    content.append(spannableString1);
                                }
                            });
                } else {
                    rlShow.setVisibility(View.GONE);
                }
            }
        });
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                onBackPressed();
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            private CharSequence mTemp;
            private int mSelectionStart;
            private int mSelectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2,
                                      int i3) {
                mTemp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int number = maxLength - editable.length();
                count.setText(String.valueOf(number));
                mSelectionStart = content.getSelectionStart();
                mSelectionEnd = content.getSelectionEnd();
                if (mTemp.length() > maxLength) {
                    handler.sendEmptyMessage(0);
                    editable.delete(mSelectionStart - 1, mSelectionEnd);// 删掉多输入的文字
                    int tempSelection = mSelectionEnd;
                    content.setText(editable);
                    content.setSelection(tempSelection);
                }
            }
        });
        content.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                rlShow.setVisibility(View.GONE);
            }
        });
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TouristUtil.isTourist()) {
                    TouristUtil.showTouristHint(mContext);
                    return;
                }

                if (!send.isClickable()) {
                    CustomToast.showToast(mContext, "请不要重复发送");
                    return;
                }
                send.setClickable(false);

                rlShow.setVisibility(View.GONE);
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(WriteState.this
                                        .getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                ExeProtocol.exe(
                        new RequestUpdateState(
                                AccountManager.Instace(mContext).getId(),
                                AccountManager.Instace(mContext).getUserName(),
                                content.getText().toString()),
                        new ProtocolResponse() {

                            @Override
                            public void finish(BaseHttpResponse bhr) {
                                send.setClickable(true);

                                ResponseUpdateState responseUpdateState = (ResponseUpdateState) bhr;
                                int code = Integer
                                        .parseInt(responseUpdateState.result);
                                if (code == 351) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(2);
                                }
                            }

                            @Override
                            public void error() {
                                send.setClickable(true);

                                handler.sendEmptyMessage(3);
                            }
                        });
            }
        });
    }

    private void initEmotion() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                Emotion.initEmotion(),
                R.layout.team_layout_single_expression_cell,
                new String[]{"image"}, new int[]{R.id.image});
        emotion_GridView.setAdapter(simpleAdapter);
        emotion_GridView.setNumColumns(7);
    }

    @Override
    public void onBackPressed() {
        if (rlShow.isShown()) {
            rlShow.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

}
