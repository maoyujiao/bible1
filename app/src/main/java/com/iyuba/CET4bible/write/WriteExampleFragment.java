package com.iyuba.CET4bible.write;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WriteDataManager;
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
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.subtitle.TextPage;
import com.iyuba.core.widget.subtitle.TextPageSelectTextCallBack;
import com.umeng.analytics.MobclickAgent;

public class WriteExampleFragment extends Fragment implements TextPageSelectTextCallBack {
    private Context mContext;
    private View root;
    private TextPage text;
    private String selectText;
    private Word selectCurrWordTemp;
    private ConfirmDialog confirmDialog;
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




    public WriteExampleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = container.getContext() ;
        root = inflater.inflate(R.layout.write_example, container, false);
        init();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
    }

    private void init() {
        text = root.findViewById(R.id.example);
        text.setTextpageSelectTextCallBack(this);
        String content = WriteDataManager.Instance().write.text;
        text.setText(TextAttr.ToDBC(content));
    }

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
            confirmDialog = new ConfirmDialog(mContext,
                    selectCurrWordTemp.key, selectCurrWordTemp.def,
                    selectCurrWordTemp.pron, selectCurrWordTemp.audioUrl,
                    null, new OnConfirmDialogClickListener() {
                @Override
                public void onSave() {
                    if (!AccountManager.Instace(mContext)
                            .checkUserLogin()) {// 未登录
                        Toast.makeText(mContext,
                                R.string.play_no_login,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
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
            confirmDialog = new ConfirmDialog(mContext,
                    selectText, "加载中...",
                    "", "",
                    null, new ConfirmDialog.OnConfirmDialogClickListener() {
                @Override
                public void onSave() {
                    if (!AccountManager.Instace(mContext)
                            .checkUserLogin()) {// 未登录
                        Toast.makeText(mContext,
                                R.string.play_no_login,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        startActivity(intent);
                    } else {
                        if (selectCurrWordTemp != null) {
                            selectCurrWordTemp.key = selectText;
                            selectCurrWordTemp.userid = AccountManager.Instace(mContext).userId;
                            saveNewWords(selectCurrWordTemp);
                        }
                    }
                }

                @Override
                public void onCancel() {
                }
            });
            confirmDialog.show();

            ClientSession.Instance().asynGetResponse(
                    new DictRequest(selectText), new IResponseReceiver() {
                        @Override
                        public void onResponse(BaseHttpResponse response,
                                               BaseHttpRequest request, int rspCookie) {
                            DictResponse dictResponse = (DictResponse) response;
                            selectCurrWordTemp = dictResponse.word;
                            if (selectCurrWordTemp != null) {
                                if (selectCurrWordTemp.def != null
                                        && selectCurrWordTemp.def.length() != 0) {
                                    handler.sendEmptyMessage(0);
                                } else {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }


                    }, null, null);
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
            Toast.makeText(mContext, R.string.play_ins_new_word_success,
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
        ClientSession.Instance().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(mContext).userId,
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
