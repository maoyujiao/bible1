package com.iyuba.core.teacher.adapter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.activity.Login;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.manager.QuestionManager;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.op.CommentAgreeOp;
import com.iyuba.core.teacher.activity.QuezActivity;
import com.iyuba.core.teacher.protocol.AgreeAgainstRequest;
import com.iyuba.core.teacher.sqlite.mode.AnswerInfo;
import com.iyuba.core.teacher.sqlite.mode.AnswerType;
import com.iyuba.core.teacher.sqlite.mode.Chat;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.ExeProtocol;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.dialog.CustomToast;

import java.util.List;

/**
 * expandableListView
 */
public class ChatAdapter extends BaseExpandableListAdapter {
    TextView t;
    AnswerType answerType;
    private Context mContext;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    break;

                case 1:
                    notifyDataSetChanged();
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.agree_already);
                    break;
                case 4:
                    CustomToast.showToast(mContext, R.string.comment_agree);
                    break;

            }

        }
    };
    private List<AnswerInfo> group;
    private List<List<Chat>> child;
    private Player mediaPlayer;
    private ImageView tempVoice;
    private boolean playingVoice = false;
    private int voiceCount;
    Handler handlerv = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    break;
                case 1:
                    // 通过不断切换图片来表示动画
                    if (voiceCount % 3 == 0) {
                        ((ImageView) msg.obj)
                                .setImageResource(R.drawable.chatfrom_voice_playing_f1);
                    } else if (voiceCount % 3 == 1) {
                        ((ImageView) msg.obj)
                                .setImageResource(R.drawable.chatfrom_voice_playing_f2);
                    } else if (voiceCount % 3 == 2) {
                        ((ImageView) msg.obj)
                                .setImageResource(R.drawable.chatfrom_voice_playing_f3);
                    }
                    voiceCount++;
                    handlerv.sendMessageDelayed(handlerv.obtainMessage(1, msg.obj),
                            500);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.comment_already);
                    break;
                case 4:
                    CustomToast.showToast(mContext, R.string.comment_agree);
                    break;
                case 5:
                    CustomToast.showToast(mContext, R.string.comment_disagree);
                    break;
            }
        }
    };

    public ChatAdapter(Context context, List<AnswerInfo> group,
                       List<List<Chat>> child, TextView tv, AnswerType a) {
        this.mContext = context;
        this.group = group;
        this.child = child;
        t = tv;
        answerType = a;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(childPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void pausePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // 播放语音
    private void playVoice(String url) {
        stopVoice();
        if (mediaPlayer == null) {
            mediaPlayer = new Player(mContext,
                    new OnPlayStateChangedListener() {

                        @Override
                        public void playFaild() {
                        }

                        @Override
                        public void playCompletion() {
                            playingVoice = false;
                            handlerv.removeMessages(1, tempVoice);
                            tempVoice.setImageResource(R.drawable.chatfrom_voice_playing);
                        }
                    });
        }
        mediaPlayer.playUrl(url);
    }

    /**
     * ��ʾ��group
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        final AnswerInfo info = group.get(groupPosition);
        //用于判断此用户是否有回复
        answerType.hasanswer = 1;

        //用于判断此老师是否回复过这个问题，回复过则调用追问接口，否则调用回复接口
        if (AccountManager.Instace(mContext).userId != null && AccountManager.Instace(mContext).userId.equals(info.uid + "")
                && AccountManager.Instace(mContext).isteacher.equals("1")) {
            answerType.isanswer = 1;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lib_group_item_chatting, null);
            holder = new GroupViewHolder();
            holder.answerImage = convertView.findViewById(R.id.answer_image);
            holder.answerName = convertView.findViewById(R.id.answer_name);
            holder.answerInfo = convertView.findViewById(R.id.answer_info);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        //TODO Image

        GitHubImageLoader.Instace(mContext).setCirclePic(info.timg + "", holder.answerImage, R.drawable.noavatar_small);


        holder.answerName.setText((TextUtils.isEmpty(info.username) || "null".equals(info.username))
                ? info.uid + "" : info.username)
        ;
        if (info.time == null || "null".equals(info.time)) {
            info.time = "";
        }
        if (info.location == null || "null".equals(info.location)) {
            info.location = "";
        }
        info.time = info.time.substring(0, 19);
        holder.answerInfo.setText(info.time + " " + info.location);
        convertView.setClickable(true);
        return convertView;

    }

    /**
     * ��ʾ��child
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        final Chat chat = child.get(groupPosition).get(childPosition);
        final AnswerInfo info = group.get(groupPosition);
        if (convertView == null) {
//			if(info.uid==chat.fromid) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lib_chatting_item_msg_text_left, null);
//			} else {
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.lib_chatting_item_msg_text_r, null);
//			}


            holder = new ChildViewHolder();
            holder.textContent = convertView.findViewById(R.id.text_content);
            holder.otherLayout = convertView.findViewById(R.id.other_layout);
            holder.otherContent = convertView.findViewById(R.id.other_content);
            holder.timeText = convertView.findViewById(R.id.time_text);
            holder.bottomView = convertView.findViewById(R.id.bottom_view);
            holder.questing = convertView.findViewById(R.id.questing);
            holder.quesContent = convertView.findViewById(R.id.questing_text);
            holder.agree_icon = convertView.findViewById(R.id.agree_icon);
            if (QuestionManager.getInstance().question.getUid().equals(AccountManager.Instace(mContext).userId)) {
                holder.quesContent.setText(" 追问问题");
                info.type = 1;
            } else {
                holder.quesContent.setText(" 向他提问");
                info.type = 2;
            }
            if (AccountManager.Instace(mContext).userId != null && AccountManager.Instace(mContext).userId.equals(info.uid + "")) {
                info.type = 3;
                holder.quesContent.setText(" 回复问题");

            }
            if (AccountManager.Instace(mContext).userId != null && (AccountManager.Instace(mContext).isteacher.equals("1"))) {
                //holder.questing.setVisibility(View.GONE);
            }

            holder.attention = convertView.findViewById(R.id.attention);
            holder.agree = convertView.findViewById(R.id.agree);
            holder.attentionText = convertView.findViewById(R.id.attention_text);
            holder.agreeText = convertView.findViewById(R.id.agree_text);
            holder.agreeText.setText("" + info.agreeCount + "");
            convertView.setTag(holder);

        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        if (chat.type == 1) {
            holder.textContent.setVisibility(View.VISIBLE);
            holder.otherLayout.setVisibility(View.GONE);
            holder.textContent.setText(chat.content);

            if (info.uid == chat.fromid) {
                holder.textContent.setBackgroundDrawable(
                        mContext.getResources().getDrawable(R.drawable.chatfrom_bg));
            } else {

                RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                para.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.textContent.setLayoutParams(para);

                holder.textContent.setBackgroundDrawable(
                        mContext.getResources().getDrawable(R.drawable.chatto_bg));
            }
        } else {
            holder.textContent.setVisibility(View.GONE);
            holder.otherLayout.setVisibility(View.VISIBLE);

            holder.otherContent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    playingVoice = true;//

                    if (tempVoice != null) {// 播放之前先停止其他的播放
                        handlerv.removeMessages(1);
                        tempVoice.setImageResource(R.drawable.chatfrom_voice_playing);
                    }
                    tempVoice = holder.otherContent;
                    playVoice("http://www.iyuba.cn/question/" + chat.content);// 播放
                    voiceAnimation(v);// 播放的动画
                }
            });


        }

        if (childPosition == child.get(groupPosition).size() - 1) {
            holder.bottomView.setVisibility(View.VISIBLE);
        } else {
            holder.bottomView.setVisibility(View.GONE);
        }

        holder.questing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (info.type == 1) {
                    answerType.aid = info.answerid;
                    t.setHint("向" + (TextUtils.isEmpty(info.username) ? info.uid + "" : info.username) + "追问");
                    answerType.sub = 2;//追问
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else if (info.type == 2) {
                    Intent intent = new Intent();
                    intent.putExtra("askuid", info.uid + "");//向某人提问的uid
                    intent.setClass(mContext, QuezActivity.class);
                    t.setHint("讨论");
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mContext.startActivity(intent);

                } else if (info.type == 3) {
                    answerType.aid = info.answerid;
                    answerType.sub = 2;//同样是追问
                    t.setHint("回复问题" + "");
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }


            }
        });

        holder.attention.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                SocialDataManager.Instance().userid = info.uid + "";
                intent.setClass(mContext, PersonalHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mContext.startActivity(intent);
            }
        });


        if (checkAgree("a" + info.answerid, AccountManager.Instace(mContext).getId()) == 1) {
            holder.agree_icon.setBackgroundResource(R.drawable.agree_press);
            holder.agreeText.setText("" + info.agreeCount + "");
        } else {

            holder.agree_icon.setBackgroundResource(R.drawable.agree_normal);
            holder.agreeText.setText("" + info.agreeCount + "");
        }

        //点赞
        holder.agree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (!AccountManager.Instace(mContext).checkUserLogin()) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    mContext.startActivity(intent);
                    return;
                }

                if (checkAgree("a" + info.answerid, AccountManager.Instace(mContext).getId()) == 1) {
                    handler.sendEmptyMessage(3);
                } else {
                    info.agreeCount = info.agreeCount + 1;
                    handler.sendEmptyMessage(0);

                    String id = AccountManager.Instace(mContext).getId();
                    String name = AccountManager.Instace(mContext).getUserName();

                    ExeProtocol.exe(new AgreeAgainstRequest(id, TextUtils.isEmpty(name) ? id : name,
                            "answerid", "" + info.answerid), new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            handler.sendEmptyMessage(4);
                        }

                        @Override
                        public void error() {

                        }
                    });
                    new CommentAgreeOp(mContext).saveData(
                            "a" + info.answerid, AccountManager.Instace(mContext).getId(), "agree");

                }

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //查看此用户是否已点赞
    private int checkAgree(String commentId, String uid) {
        return new CommentAgreeOp(mContext).findDataByAll(commentId, uid);
    }

    //以下是关于语音播放的代码
    // 停止播放动画
    private void voiceStopAnimation(View v) {
        handlerv.removeMessages(1, v);
    }

    //播放动画
    private void voiceAnimation(View v) {
        voiceStopAnimation(v);
        voiceCount = 0;
        tempVoice = (ImageView) v;
        handlerv.obtainMessage(1, tempVoice).sendToTarget();
    }

    // 播放语音评论之前先在这里reset播放器
    private void stopVoice() {
        if (mediaPlayer != null && playingVoice) {
            mediaPlayer.reset();
        }
    }

    class GroupViewHolder {
        ImageView answerImage;
        TextView answerName;
        TextView answerInfo;
    }

    class ChildViewHolder {
        TextView textContent;
        View otherLayout;
        ImageView otherContent;
        TextView timeText;
        View bottomView;
        View questing;
        TextView quesContent;
        View attention;
        View agree;
        ImageView agree_icon;
        TextView attentionText;
        TextView agreeText;


    }

}
