package com.iyuba.abilitytest.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityQuestion;
import com.iyuba.abilitytest.manager.DataManager;
import com.iyuba.abilitytest.utils.CommonUtils;
import com.iyuba.abilitytest.utils.FileUtil;
import com.iyuba.abilitytest.widget.SuperGridView;
import com.iyuba.configation.Constant;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.util.LogUtils;
import com.iyuba.core.util.ToastUtil;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.RoundProgressBar;

import java.util.ArrayList;

/**
 * Created by liuzhenli on 2017/1/5.
 */

public class ShowAnalysisAdapter extends PagerAdapter {
    private static final String TAG = "ShowAnalysisAdapter";
    private Context mContext;
    private ArrayList<AbilityQuestion.TestListBean> itemData;
    private ArrayList<View> mViewItems;
    private Player player;
    private String mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + DataManager.getInstance().lessonId;

    public ShowAnalysisAdapter(Context context, String testCategory) {
        this.mContext = context;
        itemData = new ArrayList<>();
        if (Constant.APP_CONSTANT.TYPE().equals("4") || Constant.APP_CONSTANT.TYPE().equals("6")) {
            if (("G".equals(testCategory))) {
                mLocalAudioPrefix = Constant.APP_DATA_PATH + Constant.SDCARD_AUDIO_PATH + "/" + Constant.SDCARD_ATTACH_DIR + "0/";
            }
        }
    }


    public void setData(ArrayList<AbilityQuestion.TestListBean> menuData, ArrayList<View> mViewItems) {
        if (this.itemData.size() > 0) {
            //this.itemData.clear();
            // this.mViewItems.clear();
        }
        this.itemData = menuData;
        this.mViewItems = mViewItems;
        notifyDataSetChanged();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    @Override
    public int getCount() {
        return itemData.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewItems.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private View convertView;
    private ViewHolder holder;

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final AbilityQuestion.TestListBean curItemData = itemData.get(position);

        holder = new ViewHolder();
        convertView = mViewItems.get(position);
        holder.ll_time_line = convertView.findViewById(R.id.ll_time_line);//问题描述
        holder.ll_time_line.setVisibility(View.GONE);

        holder.ques_des = convertView.findViewById(R.id.ques_des);//问题描述
        holder.ques_des_title = convertView.findViewById(R.id.ques_des_title);//问题描述

        holder.word_img = convertView.findViewById(R.id.word_img);
        holder.word_play = convertView.findViewById(R.id.word_play);
        holder.tv_ques_word = convertView.findViewById(R.id.tv_ques_word);
        holder.tv_ques_attach = convertView.findViewById(R.id.tv_ques_attach);
        holder.chosen_char = convertView.findViewById(R.id.chosen_char);//单词拼写横线

        //单选题目
        holder.ll_ans_choices = convertView.findViewById(R.id.ll_ans_choices);
        holder.ansA = convertView.findViewById(R.id.ll_ansA);
        holder.ansB = convertView.findViewById(R.id.ll_ansB);
        holder.ansC = convertView.findViewById(R.id.ll_ansC);
        holder.ansD = convertView.findViewById(R.id.ll_ansD);
        holder.ansE = convertView.findViewById(R.id.ll_ansE);
        holder.A = convertView.findViewById(R.id.tv_ans_A);
        holder.B = convertView.findViewById(R.id.tv_ans_B);
        holder.C = convertView.findViewById(R.id.tv_ans_C);
        holder.D = convertView.findViewById(R.id.tv_ans_D);
        holder.E = convertView.findViewById(R.id.tv_ans_E);
        holder.tv_rightanswer = convertView.findViewById(R.id.tv_ana_rightanswer);
        holder.tv_question_type = convertView.findViewById(R.id.tv_line);


        holder.word_read = convertView.findViewById(R.id.word_read);
        holder.keyboardr = convertView.findViewById(R.id.keyboardr);
        holder.virtual_keyboard = convertView.findViewById(R.id.virtual_keyboard);

        if (curItemData != null && curItemData.getQuestion() != null && !curItemData.getQuestion().trim().equals("")) {
            holder.ques_des.setVisibility(View.VISIBLE);
            holder.ques_des.setText(curItemData.getQuestion().split("\\+\\+")[0]);//问题描述
        } else {
            holder.ques_des.setVisibility(View.GONE);
        }

        if (curItemData.getQuestion() != null && curItemData.getQuestion().split("\\+\\+").length >= 2) {
            holder.tv_ques_word.setVisibility(View.VISIBLE);
            holder.tv_ques_word.setText(curItemData.getQuestion().split("\\+\\+")[1]);//问题详情
            holder.tv_ques_word.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else {
            holder.tv_ques_word.setVisibility(View.GONE);
        }

        if (curItemData.getAnswer() == null || curItemData.getAnswer().equals("")) {
            curItemData.setAnswer("(略)");
        }
        if (curItemData.Explains == null) {
            holder.tv_rightanswer.setText("我的答案： " + curItemData.getUserAnswer() + "\n正确答案： " + curItemData.getAnswer());
        } else if (AccountManager.Instace(mContext).loginStatus != 0 && !AccountManager.Instace(mContext).userInfo.vipStatus.equals("0")) {
            holder.tv_rightanswer.setText("我的答案： " + curItemData.getUserAnswer() + "\n正确答案： " + curItemData.getAnswer() + "\n" + FileUtil.getTextFromFile(mLocalAudioPrefix + curItemData.Explains));
        } else {
            if (TextUtils.isEmpty(FileUtil.getTextFromFile(mLocalAudioPrefix + curItemData.Explains).trim())) {
                holder.tv_rightanswer.setText("我的答案： " + curItemData.getUserAnswer() + "\n正确答案： " + curItemData.getAnswer());
            } else {
                holder.tv_rightanswer.setText("我的答案： " + curItemData.getUserAnswer() + "\n正确答案： " + curItemData.getAnswer() + "\n" + "vip用户显示题目解析.");
            }
        }
        if (curItemData.getImage() != null && !curItemData.getImage().trim().equals("")) {
            holder.word_img.setVisibility(View.VISIBLE);
            holder.word_img.setImageBitmap(CommonUtils.getImageBitmap(mContext, mLocalAudioPrefix + curItemData.getImage()));//设置本地图片
        } else {
            holder.word_img.setVisibility(View.GONE);
        }

        if (curItemData.getAttach() != null) {//加载txt内容
            LogUtils.e(TAG, "attach : " + curItemData.getAttach());
            holder.tv_ques_attach.setVisibility(View.VISIBLE);
            holder.tv_ques_attach.setText(FileUtil.getTextFromFile(mLocalAudioPrefix + curItemData.getAttach()));
        } else {
            holder.tv_ques_attach.setVisibility(View.GONE);
        }

        //weather this title is listening
        if (curItemData.getSounds() != null && !curItemData.getSounds().trim().equals("")) {
            holder.word_play.setVisibility(View.VISIBLE);
            holder.word_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(TAG, curItemData.getSounds());
                    player.playUrl(mLocalAudioPrefix + curItemData.getSounds());
                    ToastUtil.showToast(mContext, "点击下面按钮试试");
                }
            });
        } else {
            holder.word_play.setVisibility(View.GONE);
        }

        showChoiceOption(holder.ansA, holder.A, curItemData.getAnswer1());
        showChoiceOption(holder.ansB, holder.B, curItemData.getAnswer2());
        showChoiceOption(holder.ansC, holder.C, curItemData.getAnswer3());
        showChoiceOption(holder.ansD, holder.D, curItemData.getAnswer4());
        showChoiceOption(holder.ansE, holder.E, curItemData.getAnswer5());

        LogUtils.e(TAG, "testtype: " + curItemData.getTestType());
        switch (curItemData.getTestType()) {// sorts of different test types
            case Constant.ABILITY_TESTTYPE_SINGLE://单选题目
                holder.tv_question_type.setText("Text  " + "单项选择  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_BLANK://填空题目
                holder.ll_ans_choices.setVisibility(View.GONE);
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.audio_play));
                holder.tv_question_type.setText("Text  " + "填空题  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_BLANK_CHOSE://单词填空
                holder.ll_ans_choices.setVisibility(View.GONE);
                holder.tv_question_type.setText("Text  " + "选择填空  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_CHOSE_PIC:
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.audio_play));
                holder.tv_question_type.setText("Text  " + "图文对照  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_VOICE://语音评测
                holder.ll_ans_choices.setVisibility(View.GONE);
                holder.tv_ques_word.setVisibility(View.VISIBLE);
                holder.tv_ques_word.setText(curItemData.getAnswer());
                holder.word_play.setVisibility(View.VISIBLE);
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.speak_ques_read));
                holder.tv_question_type.setText("Text  " + "语音评测  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_MULTY://多选题目
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.audio_play));
                CommonUtils.showTextWithUnderLine(holder.D, curItemData.getAnswer4());
                holder.tv_question_type.setText("Text  " + "多项选择  " + curItemData.getTags());
                break;
            case Constant.ABILITY_TESTTYPE_JUDGE://判断题目
                holder.ll_ans_choices.setVisibility(View.GONE);
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.audio_play));
                holder.tv_question_type.setText("Text  " + "判断题  " + curItemData.getTags());
                if (curItemData != null && !curItemData.getAnswer1().equals("")) {
                    holder.ques_des_title.setVisibility(View.VISIBLE);
                    holder.ques_des_title.setText(curItemData.getAnswer1());
                } else {
                    holder.ques_des_title.setVisibility(View.GONE);
                }
                String userAns = "未答";
                if (curItemData.getUserAnswer().equals("0")) {
                    userAns = "错误";
                } else if (curItemData.getUserAnswer().equals("1")) {
                    userAns = "正确";
                } else if (curItemData.getUserAnswer().equals("2")) {
                    userAns = "未知";
                }
                String rigAns = "错误";
                if (curItemData.getAnswer().equals("0")) {
                    rigAns = "错误";
                } else if (curItemData.getAnswer().equals("1")) {
                    rigAns = "正确";
                } else if (curItemData.getAnswer().equals("2")) {
                    rigAns = "未知";
                }

                if (curItemData.Explains == null) {
                    holder.tv_rightanswer.setText("我的答案： " + userAns + "\n正确答案： " + rigAns);
                } else if (AccountManager.Instace(mContext).loginStatus != 0 && !AccountManager.Instace(mContext).userInfo.vipStatus.equals("0")) {
                    holder.tv_rightanswer.setText("我的答案： " + userAns + "\n正确答案： " + rigAns + "\n" + FileUtil.getTextFromFile(mLocalAudioPrefix + curItemData.Explains));
                } else {
                    holder.tv_rightanswer.setText("我的答案： " + userAns + "\n正确答案： " + rigAns + "\nvip用户显示题目解析");
                }

                break;
            case Constant.ABILITY_TESTTYPE_BLANK_WORD://填空题目
                holder.ll_ans_choices.setVisibility(View.GONE);
                holder.tv_question_type.setText("Text  " + "拼写题  " + curItemData.getTags());
                holder.word_play.setBackground(mContext.getResources().getDrawable(R.mipmap.audio_play));
                holder.keyboardr.setVisibility(View.VISIBLE);
                holder.chosen_char.setVisibility(View.VISIBLE);
                holder.tv_ques_word.setVisibility(View.GONE);
                break;
        }
        container.addView(mViewItems.get(position));
        return mViewItems.get(position);
    }

    private void showChoiceOption(LinearLayout optionLayout, TextView a, String answer) {
        if (answer != null && !answer.trim().equals("")) {
            optionLayout.setVisibility(View.VISIBLE);
            CommonUtils.showTextWithUnderLine(a, answer);
        } else {
            optionLayout.setVisibility(View.GONE);
        }
    }

    class ViewHolder {
        LinearLayout ll_time_line;
        TextView ques_des;
        TextView ques_des_title;
        ImageView word_img;//the pic to the word
        ImageView word_play;//play word audio
        TextView tv_ques_word;
        TextView tv_ques_attach;
        LinearLayout chosen_char;

        //单选题目
        LinearLayout ll_ans_choices;
        LinearLayout ansA, ansB, ansC, ansD, ansE;
        TextView A, B, C, D, E, tv_rightanswer;

        TextView tv_question_type;

        RoundProgressBar word_read;
        RelativeLayout keyboardr;
        SuperGridView virtual_keyboard;
    }
}
