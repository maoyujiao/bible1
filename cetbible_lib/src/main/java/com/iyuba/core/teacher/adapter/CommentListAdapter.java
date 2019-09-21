package com.iyuba.core.teacher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.listener.OnPlayStateChangedListener;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.sqlite.op.CommentAgreeOp;
import com.iyuba.core.teacher.sqlite.mode.AnswerInfo;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.widget.Player;
import com.iyuba.core.widget.dialog.CustomToast;

import java.util.ArrayList;
import java.util.List;

import okio.Options;

public class CommentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<AnswerInfo> mList = new ArrayList<AnswerInfo>();
    private Player mediaPlayer;
    private String uid = "0";
    private boolean playingVoice = false;
    private ImageView tempVoice;
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
                        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.abc_ab_share_pack_holo_dark);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,)

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

    public CommentListAdapter(Context context, List<AnswerInfo> commentList) {
        mContext = context;
        mList = commentList;

        //取登录人的id
        if (AccountManager.Instace(mContext).checkUserLogin()) {
            uid = AccountManager.Instace(mContext).userId;
        } else {
            uid = "0";
        }
    }

    //查看此用户是否已点赞
    private int checkAgree(String commentId, String uid) {
        return new CommentAgreeOp(mContext).findDataByAll(commentId, uid);
    }

    public void setData(ArrayList<AnswerInfo> Comments) {
        mList = Comments;
    }

    public void addList(ArrayList<AnswerInfo> Comments) {
        mList.addAll(Comments);
    }

    public void pausePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AnswerInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AnswerInfo comment = mList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.lib_list_item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.userIcon = convertView.findViewById(R.id.user_icon);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.commentInfo = convertView.findViewById(R.id.comment_info);
            viewHolder.commentDisc = convertView.findViewById(R.id.comment_disc);
            viewHolder.otherContentc = convertView.findViewById(R.id.other_contentc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GitHubImageLoader.Instace(mContext).setPic(comment.userimg, viewHolder.userIcon, R.drawable.noavatar_small);
        comment.time = comment.time.substring(0, 19);
        viewHolder.userName.setText((TextUtils.isEmpty(comment.username) ||
                "null".equals(comment.username)) ?
                comment.uid + "" : comment.username);
        viewHolder.commentInfo.setText(comment.time);


        if (comment.type == 1) {
            viewHolder.otherContentc.setVisibility(View.GONE);
            viewHolder.commentDisc.setText(comment.answer);
        } else {
            viewHolder.otherContentc.setVisibility(View.VISIBLE);
            viewHolder.commentDisc.setVisibility(View.GONE);

        }


        viewHolder.otherContentc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playingVoice = true;//

                if (tempVoice != null) {// 播放之前先停止其他的播放
                    handlerv.removeMessages(1);
                    tempVoice.setImageResource(R.drawable.chatfrom_voice_playing);
                }
                tempVoice = viewHolder.otherContentc;
//				playVoice("http://www.iyuba.cn/question/" + comment.answer);// 播放
                playVoice("http://www.iyuba.cn/question/" + comment.audio);// 播放
                voiceAnimation(v);// 播放的动画
            }
        });


        return convertView;
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
    public void stopVoice() {
        if (mediaPlayer != null && playingVoice) {
            mediaPlayer.reset();
        }
    }

    public class ViewHolder {
        ImageView userIcon;
        TextView userName;
        TextView commentInfo;
        TextView commentDisc;
        ImageView otherContentc;
    }
}
