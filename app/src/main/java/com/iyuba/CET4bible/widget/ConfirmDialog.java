package com.iyuba.CET4bible.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iyuba.CET4bible.R;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.WordCard;

/**
 * 取消和确认的对话框
 *
 * @author Administrator
 */
public class ConfirmDialog extends Dialog {
    private String keyWord, explanation, pron, audioUrl;
    private Context mContext;
    private View dialog_confirm;
    private WordCard card;

    public ConfirmDialog(Context context, String keyWord, String explanation,
                         String pron, String audioUrl, Player mediaPlayer, OnConfirmDialogClickListener listener) {
        super(context,R.style.BaseDialog);
        this.keyWord = keyWord;
        this.explanation = explanation;
        this.mContext = context;
        this.pron = pron;
        this.audioUrl = audioUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    public void setValue(String keyWord, String explanation, String pron, String audioUrl) {
        this.audioUrl = audioUrl;
    }

    protected void initView() {
        dialog_confirm = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);

        setContentView(dialog_confirm);
        card = dialog_confirm.findViewById(R.id.wordcard);
        card.searchWord(keyWord);
        card.findViewById(R.id.word_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        card.findViewById(R.id.word_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.saveNewWords();
                dismiss();
            }
        });
    }



    public interface OnConfirmDialogClickListener {
        void onCancel();
        void onSave();
    }

}
