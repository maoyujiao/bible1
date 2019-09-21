package com.iyuba.CET4bible.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.protocol.StudyRecordInfo;
import com.iyuba.CET4bible.protocol.UpdateStudyRecordRequestNew;
import com.iyuba.CET4bible.sqlite.mode.FillInBlankBean;
import com.iyuba.CET4bible.util.FavoriteUtil;
import com.iyuba.CET4bible.widget.ConfirmDialog;
import com.iyuba.base.BaseActivity;
import com.iyuba.base.util.ClickableMovementMethod;
import com.iyuba.configation.Constant;
import com.iyuba.core.discover.protocol.WordUpdateRequest;
import com.iyuba.core.discover.protocol.WordUpdateResponse;
import com.iyuba.core.http.Http;
import com.iyuba.core.http.HttpCallback;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.me.activity.VipCenter;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.util.GetDeviceInfo;
import com.iyuba.core.util.TouristUtil;
import com.iyuba.core.widget.subtitle.TextPage;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * 选择填空题
 *
 * @author wayne
 * @date 2017/12/19
 */
public class FillInBlankActivity extends BaseActivity {
    private final int QUESTION_NUMBER = 10;
    String[] answerStringsxxxxxxx;

    String content;
    /**
     * 解析数据
     */
    String[] explainArray;

    /**
     * 字母排序后的正确答案
     */
    List<String> sortedAnswerWordList = new ArrayList<>();
    /**
     * 未排序
     */
    List<String> realAnswerWordList = new ArrayList<>();


    SpanClickListener spanClickListener;
    ExplainClickListener explainClickListener;

    /**
     * 选中的单词，key是选择题的位置
     */
    SparseArray<String> selectWordArray = new SparseArray<>();
    /**
     * 答题整错还是错误
     */
    SparseArray<Boolean> answerArray = new SparseArray<>();
    TextPage textView;
    /**
     * 但是是否未显示答案状态
     */
    boolean isShowAnswer = false;


    private String selectText;
    private Word selectCurrWordTemp;
    private ConfirmDialog confirmDialog;

    private FavoriteUtil favoriteUtil;
    private FillInBlankBean bean;

    //学习纪录
    private StudyRecordInfo studyRecordInfo;
    private GetDeviceInfo deviceInfo;
    private ScrollView scrollView;
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);

        findView(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        scrollView = findViewById(R.id.scroll);
        TextView tvTitle = findView(R.id.tv_question_title);
        textView = findView(R.id.text);
        textView.setDefaultSelection(false);
//        textView.setSelectEnable(true);

        FillInBlankBean data = (FillInBlankBean) getIntent().getSerializableExtra("data");
        bean = data;
        answerStringsxxxxxxx = data.word.split(",");
        content = data.original.replace("++", "\n");
        explainArray = data.explanation.split("[+][+]");

        tvTitle.setText(data.title);
        for (String s : answerStringsxxxxxxx) {
            sortedAnswerWordList.add(s.trim());
            realAnswerWordList.add(s.trim());
        }
        Collections.sort(sortedAnswerWordList);

        spanClickListener = new SpanClickListener();
        explainClickListener = new ExplainClickListener();

        textView.setText(StringUtil.getSpannableString(mContext, content, spanClickListener, explainClickListener));
        textView.setMovementMethod(ClickableMovementMethod.getInstance());
        textView.setTextpageSelectTextCallBack(new TextPageSelectTextCallBack() {
            @Override
            public void selectTextEvent(String word) {
                selectText = word;
                getNetworkInterpretation();
            }

            @Override
            public void selectParagraph(int paragraph) {
            }
        });

        final TextView tvSubmit = findView(R.id.submit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvSubmit.getText().toString();
                if (text.equals("提交")) {
                    if (selectWordArray.size() < QUESTION_NUMBER) {
                        showShort("请完成所有题目后再进行提交");
                        return;
                    }
                    isShowAnswer = true;

                    for (int i = 0; i < QUESTION_NUMBER; i++) {
                        answerArray.put(i, realAnswerWordList.get(i).equals(selectWordArray.get(i)));
                    }
                    refreshText();
                    tvSubmit.setText("查看译文");
                } else if (text.equals("查看译文")) {
                    showPopWindow();
                }
            }
        });
        findView(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowAnswer = false;
                selectWordArray.removeAtRange(0, selectWordArray.size());
                answerArray.removeAtRange(0, answerArray.size());
                refreshText();
            }
        });

        studyRecord();
        setFavorite();
    }

    private void studyRecord() {
        deviceInfo = new GetDeviceInfo(mContext);
        studyRecordInfo = new StudyRecordInfo();
        studyRecordInfo.uid = AccountManager.Instace(mContext).getId();
        studyRecordInfo.IP = deviceInfo.getLocalIPAddress();
        studyRecordInfo.DeviceId = deviceInfo.getLocalMACAddress();
        studyRecordInfo.Device = deviceInfo.getLocalDeviceType();
        studyRecordInfo.updateTime = "   ";
        studyRecordInfo.EndFlg = "1";
        studyRecordInfo.Lesson = Constant.APPName;
        studyRecordInfo.LessonId = bean.year;
        studyRecordInfo.BeginTime = deviceInfo.getCurrentTime();
        studyRecordInfo.TestNumber = "101" + bean.index;
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        if (System.currentTimeMillis() - startTime < 1000 * 15) {
            e("--- 时间不够15秒 ---");
            super.onDestroy();
            return;
        }
        studyRecordInfo.EndTime = deviceInfo.getCurrentTime();
        try {
            if (AccountManager.Instace(mContext).checkUserLogin() && !TouristUtil.isTourist()) {
                if (!TextUtils.isEmpty(studyRecordInfo.uid) && !"0".equals(studyRecordInfo.uid)) {
                    Http.get(UpdateStudyRecordRequestNew.getUrl(studyRecordInfo, "3",
                            bean.original.split(" ").length + ""), new HttpCallback() {
                        @Override
                        public void onSucceed(Call call, String response) {
                        }

                        @Override
                        public void onError(Call call, Exception e) {
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private PopupWindow popupWindow;

    private void showPopWindow() {
        if (popupWindow != null) {
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            return;
        }

        View view = getLayoutInflater().inflate(R.layout.pop_fill_in_blank_chinese, null);
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(view);
        TextView chinese = view.findViewById(R.id.text);
        chinese.setText(bean.chinese.replace("++", "\n"));
        chinese.setMovementMethod(LinkMovementMethod.getInstance());

        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        showPopWindow();
    }

    private void setFavorite() {
        favoriteUtil = new FavoriteUtil(FavoriteUtil.Type.fillInBlack);
        CheckBox checkBox = findView(R.id.cb_favorite);
        boolean isFavorite = favoriteUtil.isFavorite("fillInBlank_" + bean.year + bean.index);
        checkBox.setChecked(isFavorite);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                favoriteUtil.setFavorite(isChecked, "fillInBlank_" + bean.year + bean.index);
            }
        });
    }

    private void refreshText() {
        textView.setText(StringUtil.getSpannableString(mContext, content, selectWordArray, answerArray, spanClickListener, explainClickListener));
    }


    class SpanClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isShowAnswer) {
                showExplainDialog((int) v.getTag());
            } else {
                showChooseDialog((int) v.getTag());
            }
        }
    }

    class ExplainClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            showExplainDialog((int) v.getTag());
        }
    }

    private void showExplainDialog(int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_explain, null);
        builder.setView(contentView);
        final Dialog dialog = builder.create();
        dialog.show();


        TextView word = contentView.findViewById(R.id.word);
        TextView explain = contentView.findViewById(R.id.explain);
        explain.setMovementMethod(LinkMovementMethod.getInstance());

        word.setText(String.format("答案： %s", realAnswerWordList.get(pos)));
        if (!AccountManager.isVip()) {
            explain.setText("您还不是会员，会员用户才可以查看解析，是否开通会员？ 点击开通");
            explain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, VipCenter.class);
                    mContext.startActivity(intent);
                }
            });

        } else {
            explain.setText(explainArray[pos]);

        }

    }

    /**
     * 选择单词
     *
     * @param pos 填空题的位置
     */
    public void showChooseDialog(final int pos) {
        final Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = getLayoutInflater().inflate(R.layout.dialog_choose, null);
        builder.setView(view);
        dialog = builder.create();
        textView.setFocusable(false);

        final ListView listView = view.findViewById(R.id.list);
        // 单词是否被选中
        SparseBooleanArray selectArray = new SparseBooleanArray();
        // 当前选中的单词
        String word = selectWordArray.get(pos, null);
        for (int k = 0; k < sortedAnswerWordList.size(); k++) {
            String s = sortedAnswerWordList.get(k);
            int flag = -1;
            for (int i = 0; i < selectWordArray.size(); i++) {
                int key = selectWordArray.keyAt(i);
                if (selectWordArray.get(key).equals(s)) {
                    flag = key;
                    break;
                }
            }
            // 已经选择过的单词
            if (flag != -1) {
                selectArray.put(k, true);
            }
        }

        SelectItemAdapter adapter = new SelectItemAdapter(mContext, sortedAnswerWordList, word, selectArray);
        listView.setAdapter(adapter);
        adapter.setClearOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selectWordArray.removeAt(selectWordArray.indexOfValue((String) v.getTag()));
                textView.setText(StringUtil.getSpannableString(mContext, content, selectWordArray, spanClickListener, explainClickListener));
            }
        });
        adapter.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                selectWordArray.put(pos, (String) listView.getAdapter().getItem(position));
                textView.setText(StringUtil.getSpannableString(mContext, content, selectWordArray, spanClickListener, explainClickListener));
            }
        });
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                textView.setFocusable(true);
            }
        });

    }


    /**
     * 获取单词释义
     */
    public void getNetworkInterpretation() {
        if (selectText != null && selectText.length() != 0) {
            selectCurrWordTemp = null;
            confirmDialog = new ConfirmDialog(mContext,
                    selectText, "加载中...", "", "",
                    null, null);
            confirmDialog.show();
            textView.setFocusable(false);
            confirmDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    textView.setFocusable(true);
                }
            });
        } else {
            Toast.makeText(mContext, "请选择英文单词", Toast.LENGTH_SHORT).show();
        }
    }

    //添加单词到生词本
    public boolean saveNewWords(Word wordTemp) {
        try {
            WordOp wo = new WordOp(mContext);
            Log.e("Mingyu ReadingFragment", wordTemp.key);
            wo.saveData(wordTemp);
            // Log.e("插入生词数据库", "完成");
            Toast.makeText(mContext, R.string.play_ins_new_word_success, Toast.LENGTH_SHORT).show();
            // 保存到网络
            addNetwordWord(wordTemp.key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addNetwordWord(String wordTemp) {
        ClientSession.Instance().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                        WordUpdateResponse wur = (WordUpdateResponse) response;
                    }
                }, null, null);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("WriteExampe", "显示翻译面板");
                    if (selectCurrWordTemp != null && confirmDialog != null) {
                        confirmDialog.setValue(selectCurrWordTemp.key, selectCurrWordTemp.def,
                                selectCurrWordTemp.pron, selectCurrWordTemp.audioUrl);
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }

        }
    };

}
