package com.iyuba.CET4bible.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.widget.Player;
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.Animator.AnimatorListener;
//import com.nineoldandroids.animation.AnimatorSet;

/**
 * 取消和确认的对话框
 *
 * @author Administrator
 */
public class ConfirmDialog extends BaseDialog {
    boolean isCancel = false;
    private Button dialog_btn_cancel, dialog_btn_addword;
    private String keyWord, explanation, pron, audioUrl;
    private Context mContext;
    private Typeface mFace;
    private OnConfirmDialogClickListener listener;
    private TextView tv_key;
    private TextView tv_pron;
    private TextView tv_def;
    //private SimplePlayer mPlayer;
    private ImageView iv_audio;
    private View view;
    private LinearLayout ll_content;
    private View dialog_confirm;
    private AnimatorSet set;
    private Player player;

    public ConfirmDialog(Context context, String keyWord, String explanation,
                         String pron, String audioUrl, Player mediaPlayer, OnConfirmDialogClickListener listener) {
        super(context);
        this.listener = listener;
        this.keyWord = keyWord;
        this.explanation = explanation;
        this.mContext = context;
        this.pron = pron;
        this.audioUrl = audioUrl;
        set = new AnimatorSet();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationRepeat(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("MingYu AnimationEnd ", "AnimationEnd ~~~");

            }

            @Override
            public void onAnimationCancel(Animator animation) {


            }
        });
    }

    public void setValue(String keyWord, String explanation, String pron, String audioUrl) {
        this.audioUrl = audioUrl;
        tv_key.setText(keyWord);
        tv_def.setText(explanation);
        tv_pron.setText(Html.fromHtml("[ " + pron + " ]"));
        tv_pron.setTypeface(mFace);
    }

    @Override
    protected void initView() {
        dialog_confirm = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
        ll_content = dialog_confirm.findViewById(R.id.ll_content);
        tv_key = dialog_confirm.findViewById(R.id.word_key);
        tv_pron = dialog_confirm.findViewById(R.id.word_pron);
        tv_def = dialog_confirm.findViewById(R.id.word_def);
        iv_audio = dialog_confirm.findViewById(R.id.iv_audio);
        iv_audio.setOnClickListener(this);
        mFace = Typeface.createFromAsset(mContext.getAssets(),
                "font/SEGOEUI.TTF");
        dialog_btn_cancel = dialog_confirm.findViewById(R.id.dialog_btn_cancel);
        dialog_btn_addword = dialog_confirm.findViewById(R.id.dialog_btn_addword);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                         /*set.playTogether(ObjectAnimator.ofFloat(ll_content, "rotationY", 90, 0).setDuration(300),
                                 ObjectAnimator.ofFloat(ll_content, "translationX", -300, 0).setDuration(300));
				 		set.setInterpolator(new AccelerateInterpolator());
				 		set.start();	*/
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (player != null) {
                    player.stop();
                    player = null;
                }
            }
        });
        setContentView(dialog_confirm);
    }

    @Override
    protected void initListener() {
        dialog_btn_cancel.setOnClickListener(this);
        dialog_btn_addword.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_key.setText(keyWord);
        tv_def.setText(explanation);
        tv_pron.setText(Html.fromHtml("[ " + pron + " ]"));
        tv_pron.setTypeface(mFace);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_cancel:
                if (listener != null) {
                    listener.onCancel();
                    dismiss();
                }
                break;
            case R.id.dialog_btn_addword:
                if (listener != null) {
                    listener.onSave();
                    dismiss();
                }
                break;
            case R.id.iv_audio:
                playerAudio();
                break;
        }
    }

    /**
     *
     */
    private void playerAudio() {
        if (player != null) {
            //什么也不做
        } else {
            player = new Player(mContext, new Opscl());
        }
        if (audioUrl != null && !audioUrl.equals("")) {
            player.playUrl(audioUrl);
        }
    }

    public interface OnConfirmDialogClickListener {
        void onCancel();

        void onSave();
    }

    private class Opscl implements OnPlayStateChangedListener {

        @Override
        public void playFaild() {

        }

        @Override
        public void playCompletion() {

        }

    }
}
