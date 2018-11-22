package com.iyuba.CET4bible.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.adapter.ReadingQuesFragmentAdapter;
import com.iyuba.CET4bible.manager.ReadingDataManager;
import com.iyuba.CET4bible.sqlite.mode.PackInfo;
import com.iyuba.CET4bible.sqlite.mode.ReadingAnswer;
import com.iyuba.CET4bible.sqlite.mode.ReadingExplain;
import com.iyuba.CET4bible.sqlite.mode.ReadingText;
import com.iyuba.CET4bible.sqlite.op.ReadingAnswerOp;
import com.iyuba.CET4bible.sqlite.op.ReadingExplainOp;
import com.iyuba.CET4bible.sqlite.op.ReadingInfoOp;
import com.iyuba.CET4bible.sqlite.op.ReadingTextOp;
import com.iyuba.CET4bible.widget.ConfirmDialog;
import com.iyuba.CET4bible.widget.ConfirmDialog.OnConfirmDialogClickListener;
import com.iyuba.core.activity.Login;
import com.iyuba.core.discover.protocol.WordUpdateRequest;
import com.iyuba.core.discover.protocol.WordUpdateResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.base.DictRequest;
import com.iyuba.core.protocol.base.DictResponse;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.sqlite.op.WordOp;
import com.iyuba.core.widget.subtitle.TextPage;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;

import java.util.ArrayList;
import java.util.List;

public class ReadingFragment extends Fragment implements TextPageSelectTextCallBack {
    public Context context;
    private List<ReadingText> readingTexts;
    private TextPage tv_content;
    private List<ReadingAnswer> readingAnswers;
    private List<ReadingExplain> readingExplains;
    private String selectText;
    private Word selectCurrWordTemp;
    private ViewPager mViewPager;
    private View root;
    private String packName;
    private RelativeLayout rl_content;
    private int[] titleNums = new int[]{};
    private int curPosition;
    private List<PackInfo> packInfos = new ArrayList<PackInfo>();
    private ConfirmDialog confirmDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    initData();
                    break;
                case 1:
                    Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "font/times.ttf");
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < readingTexts.size(); i++) {
                        sb.append(readingTexts.get(i).Sentence + "\n\n");
                    }
                    tv_content.setTypeface(face);
                    tv_content.setTextSize(18);
                    tv_content.setText(sb.toString());
                    break;
                case 2:
                    if (selectCurrWordTemp != null && confirmDialog != null) {
                        confirmDialog.setValue(selectCurrWordTemp.key, selectCurrWordTemp.def,
                                selectCurrWordTemp.pron, selectCurrWordTemp.audioUrl);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static ReadingFragment newInstance(Context context, int position) {
        ReadingFragment readingFragment = new ReadingFragment();
        readingFragment.context = context;
        readingFragment.curPosition = position;
        return readingFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        packName = bundle.getString("PackName");
        Log.e("ReadingFragment", "onCreate");
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.reading_fragment, null);
        initView();
        Log.e("ReadingFragment", "onCreateView");
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isVisibleToUser) {
            handler.sendEmptyMessage(0);
        }
    }

    private void initData() {
        ReadingInfoOp readingInfoOp = new ReadingInfoOp(context);
        if (packInfos.size() == 0) {
            packInfos = readingInfoOp.findDataByPackName(packName);
            ReadingDataManager.getInstance().packInfos = packInfos;
        }
        ReadingTextOp readingTextOp = new ReadingTextOp(context);
        readingTexts = readingTextOp.findDataByTitleNum(packInfos.get(curPosition).TitleNum);
        ReadingAnswerOp readingAnswerOp = new ReadingAnswerOp(context);
        readingAnswers = readingAnswerOp.findDataByTitleNum(packInfos.get(curPosition).TitleNum);
        ReadingExplainOp readingExplainOp = new ReadingExplainOp(context);
        readingExplains = readingExplainOp.findDataByTitleNum(packInfos.get(curPosition).TitleNum);
        handler.sendEmptyMessage(1);
    }

    private void initView() {
        rl_content = root.findViewById(R.id.rl_content);
        tv_content = root.findViewById(R.id.tv_content);
        tv_content.setTextpageSelectTextCallBack(this);
        mViewPager = root.findViewById(R.id.viewpager);
        ReadingQuesFragmentAdapter readingQuesFragmentAdapter = new ReadingQuesFragmentAdapter(context,
                getChildFragmentManager(), readingAnswers, readingExplains);
        mViewPager.setAdapter(readingQuesFragmentAdapter);
    }


    //取词功能
    @Override
    public void selectTextEvent(String selectText) {
        this.selectText = selectText;
        getNetworkInterpretation();
    }

    @Override
    public void selectParagraph(int paragraph) {
    }

    /**
     * 显示翻译面板
     */
    protected void showTranslationDialog() {
        if (selectCurrWordTemp != null) {
            confirmDialog = new ConfirmDialog(context,
                    selectCurrWordTemp.key, selectCurrWordTemp.def,
                    selectCurrWordTemp.pron, selectCurrWordTemp.audioUrl,
                    null, new OnConfirmDialogClickListener() {
                @Override
                public void onSave() {
                    if (!AccountManager.Instace(context)
                            .checkUserLogin()) {// 未登录
                        Toast.makeText(context,
                                R.string.play_no_login,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(context, Login.class);
                        startActivity(intent);
                    } else {
                        saveNewWords(selectCurrWordTemp);
                    }
                }

                @Override
                public void onCancel() {

                }
            });
            confirmDialog.show();
        }
    }

    /**
     * 获取单词释义
     */
    public void getNetworkInterpretation() {
        if (selectText != null && selectText.length() != 0) {
            selectCurrWordTemp = null;
            confirmDialog = new ConfirmDialog(context,
                    selectText, "加载中...",
                    "", "",
                    null, new ConfirmDialog.OnConfirmDialogClickListener() {
                @Override
                public void onSave() {
                    if (!AccountManager.Instace(context)
                            .checkUserLogin()) {// 未登录
                        Toast.makeText(context,
                                R.string.play_no_login,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(context, Login.class);
                        startActivity(intent);
                    } else {
                        if (selectCurrWordTemp != null) {
                            selectCurrWordTemp.key = selectText;
                            selectCurrWordTemp.userid = AccountManager.Instace(context).userId;
                            saveNewWords(selectCurrWordTemp);
                        }
                    }
                }

                @Override
                public void onCancel() {
                }
            });
            confirmDialog.show();


            ClientSession.Instace().asynGetResponse(
                    new DictRequest(selectText), new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {

                            DictResponse dictResponse = (DictResponse) response;
                            selectCurrWordTemp = dictResponse.word;
                            if (selectCurrWordTemp != null) {
                                if (selectCurrWordTemp.def != null
                                        && selectCurrWordTemp.def.length() != 0) {
                                    handler.sendEmptyMessage(2);
                                } else {
                                    handler.sendEmptyMessage(5);
                                }
                            } else {

                            }
                        }


                    }, null, null);
        } else {
            Toast.makeText(context, "请选择英文单词", Toast.LENGTH_SHORT).show();
        }
    }

    //添加单词到生词本
    public boolean saveNewWords(Word wordTemp) {
        try {
            WordOp wo = new WordOp(context);
            Log.e("Mingyu ReadingFragment", wordTemp.key);
            wo.saveData(wordTemp);
            // Log.e("插入生词数据库", "完成");
            Toast.makeText(context, R.string.play_ins_new_word_success,
                    Toast.LENGTH_SHORT).show();
            // 保存到网络
            addNetwordWord(wordTemp.key);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public void addNetwordWord(String wordTemp) {
        ClientSession.Instace().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(context).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {
                        WordUpdateResponse wur = (WordUpdateResponse) response;
                        if (wur.result == 1) {
                            // Log.e("添加网络生词本", wur.word+","+true);
                        }
                    }
                }, null, null);
    }
}
