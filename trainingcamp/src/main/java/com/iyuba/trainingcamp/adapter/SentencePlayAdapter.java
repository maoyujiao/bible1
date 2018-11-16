package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.LearningContent;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.widget.GoldMediaPlayer;

import java.util.List;


/**
 * @author yq QQ:1032006226
 */
public class SentencePlayAdapter extends RecyclerView.Adapter<SentencePlayAdapter.ViewHolder> {

    private static final int PLAYER_STATE_PAUSE = 2;
    private static final int PLAYER_STATE_PLAY = 1;
    private static final int PLAYER_STATE_STOP = 0;
    private int mPlayState;

    private String currentUrl = "";
    private List<LearningContent> sentences;
    private Context mContext;
    private GoldMediaPlayer player;
    private int playIndex; //正在播放的序号

    public SentencePlayAdapter(Context context, List<LearningContent> list) {
        this.sentences = list;
        mContext = context;
    }

    @NonNull
    @Override
    public SentencePlayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trainingcamp_sentence_play, null);
        ViewHolder holder = new ViewHolder(view);
        player = new GoldMediaPlayer();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SentencePlayAdapter.ViewHolder holder, final int position) {

        holder.SentenceView.setText(sentences.get(position).en);
        holder.SentenceCn.setText(sentences.get(position).cn);
        if (position != playIndex) {
            holder.play.setImageResource(R.drawable.trainingcamp_icon_play);
            holder.follow.setImageResource(R.drawable.trainingcamp_icon_play_record);
        }


        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPronounce(holder,holder.play, position, 1);

                holder.follow.setImageResource(R.drawable.trainingcamp_icon_play_record);
                playIndex = position;
                notifyDataSetChanged();

            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playIndex = position;
                notifyDataSetChanged();
                startPronounce(holder,holder.follow, position, 0);
                holder.play.setImageResource(R.drawable.trainingcamp_icon_play);
            }
        });

    }

    private void startPronounce(ViewHolder holder,ImageView v , int position, int fromUser) {
        String url = "";
        switch (fromUser) {
            case 1:
                url = "http://static2.iyuba.com/" + GoldApp.getApp(mContext).LessonType + "/sounds/" + sentences.get(position).pro;
                holder.follow.setImageResource(R.drawable.trainingcamp_icon_play);
                break;
            case 0:
                url = FilePath.getRecordPath()+ sentences.get(position).id +".amr";
                holder.play.setImageResource(R.drawable.trainingcamp_icon_play_record);

                break;
        }
        if (mPlayState == PLAYER_STATE_PLAY) {
            if (currentUrl.equals(url)) {
                currentUrl = url;
                player.pause();
                mPlayState = PLAYER_STATE_PAUSE;
                if (fromUser == 0) {
                    v.setImageResource(R.drawable.trainingcamp_icon_play_record);
                } else {
                    v.setImageResource(R.drawable.trainingcamp_icon_play);
                }
            } else {
                currentUrl = url;
                player.stopRestart(url);
                if (fromUser == 0) {
                    v.setImageResource(R.drawable.trainingcamp_icon_pause_record);
                } else {
                    v.setImageResource(R.drawable.trainingcamp_icon_pause);
                }
            }

        } else if (mPlayState == PLAYER_STATE_PAUSE) {
            if (playIndex == position) {
                if (currentUrl.equals(url)) {

                    currentUrl = url;
                    player.start();

                }
                mPlayState = PLAYER_STATE_PLAY;
                if (fromUser == 0) {
                    v.setImageResource(R.drawable.trainingcamp_icon_pause_record);
                } else {
                    v.setImageResource(R.drawable.trainingcamp_icon_pause);
                }
            } else {
                playIndex = position;
                if (fromUser == 0) {
                    v.setImageResource(R.drawable.trainingcamp_icon_play_record);
                } else {
                    v.setImageResource(R.drawable.trainingcamp_icon_play);
                }
                player.stopRestart(url);
                currentUrl = url;
                mPlayState = PLAYER_STATE_PLAY;

            }

        } else if (mPlayState == PLAYER_STATE_STOP) {
            currentUrl = url;
            playIndex = position;
            if (fromUser == 0) {
                v.setImageResource(R.drawable.trainingcamp_icon_pause_record);
            } else {
                v.setImageResource(R.drawable.trainingcamp_icon_pause);
            }
            mPlayState = PLAYER_STATE_PLAY;
            player.stopRestart(url);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView SentenceView;
        TextView SentenceCn;
        ImageView play;
        ImageView follow;

        public ViewHolder(View itemView) {
            super(itemView);
            SentenceView = itemView.findViewById(R.id.sentnence_read);
            SentenceCn = itemView.findViewById(R.id.sentnence_cn);
            play = itemView.findViewById(R.id.play);
            follow = itemView.findViewById(R.id.follow);
        }
    }
}
