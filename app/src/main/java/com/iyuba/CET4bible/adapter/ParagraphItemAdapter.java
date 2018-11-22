package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.widget.ConfirmDialog;
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

import java.util.List;

/**
 * ParagraphItemAdapter
 *
 * @author wayne
 * @date 2017/12/22
 */
public class ParagraphItemAdapter extends RecyclerView.Adapter<ParagraphItemAdapter.Holder> {
    private Context mContext;
    private List<String> mList;
    private List<String> mTranslationList;
    private boolean isShowAnswer = false;

    public ParagraphItemAdapter(Context context, List<String> mList, List<String> mTranslationList) {
        this.mContext = context;
        this.mList = mList;
        this.mTranslationList = mTranslationList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new BottomHolder(LayoutInflater.from(mContext).inflate(R.layout.item_empty_layout, parent, false));
        } else {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_paragraph, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        if (position == mList.size()) {
            return;
        }
        if (position == 0) {
            holder.text.getPaint().setFakeBoldText(true);
            holder.text.setGravity(Gravity.CENTER);
        } else {
            holder.text.setGravity(Gravity.START);
            holder.text.getPaint().setFakeBoldText(false);
        }
        holder.text.setText(mList.get(position));
        holder.translate.setSelected(true);
        holder.translate.setVisibility(isShowAnswer ? View.VISIBLE : View.GONE);
        holder.translate.setTag(true);
        holder.translate.setText("原");

        holder.translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.text.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(150);
                holder.text.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.text.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100);
                    }
                }, 150);

                holder.translate.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(150);
                holder.translate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.translate.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100);
                    }
                }, 150);

                Boolean isOriginal = (Boolean) holder.translate.getTag();
                if (isOriginal) {
                    holder.translate.setText("译");
                    holder.text.setText(mTranslationList.get(holder.getAdapterPosition()));
                } else {
                    holder.translate.setText("原");
                    holder.text.setText(mList.get(holder.getAdapterPosition()));
                }
                holder.translate.setTag(!isOriginal);
            }
        });


        holder.text.setTextpageSelectTextCallBack(new TextPageSelectTextCallBack() {
            @Override
            public void selectTextEvent(String word) {
                selectText = word;
                getNetworkInterpretation();
            }

            @Override
            public void selectParagraph(int paragraph) {
            }
        });
    }

    private String selectText;
    private Word selectCurrWordTemp;
    private ConfirmDialog confirmDialog;


    /**
     * 获取单词释义
     */
    public void getNetworkInterpretation() {
        if (selectText != null && selectText.length() != 0) {
            selectCurrWordTemp = null;
            confirmDialog = new ConfirmDialog(mContext,
                    selectText, "加载中...", "", "",
                    null, new ConfirmDialog.OnConfirmDialogClickListener() {
                @Override
                public void onSave() {
                    if (!AccountManager.Instace(mContext).checkUserLogin()) {
                        Toast.makeText(mContext, R.string.play_no_login, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        mContext.startActivity(intent);
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
        ClientSession.Instace().asynGetResponse(
                new WordUpdateRequest(AccountManager.Instace(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                        WordUpdateResponse wur = (WordUpdateResponse) response;
                        if (wur.result == 1) {
                            // Log.e("添加网络生词本", wur.word+","+true);
                        }
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

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextPage text;
        TextView translate;

        public Holder(View itemView) {
            super(itemView);
            try {
                text = itemView.findViewById(R.id.text);
                translate = itemView.findViewById(R.id.translate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setShowAnswer(boolean showAnswer) {
        isShowAnswer = showAnswer;
        notifyDataSetChanged();
    }

    private class BottomHolder extends Holder {
        public BottomHolder(View inflate) {
            super(inflate);
        }
    }
}
