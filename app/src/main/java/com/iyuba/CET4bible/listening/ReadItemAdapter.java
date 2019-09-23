package com.iyuba.CET4bible.listening;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.op.NewTypeTextAOp;
import com.iyuba.abilitytest.network.AbilityTestRequestFactory;
import com.iyuba.abilitytest.network.SendGoodResponse;
import com.iyuba.abilitytest.network.SpeakRank;
import com.iyuba.abilitytest.network.SpeakRankWork;
import com.iyuba.trainingcamp.utils.ACache;
import com.iyuba.trainingcamp.utils.ToastUtil;
import com.lid.lib.LabelTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.cet4.activity.adapter
 * @class describe
 * @time 2019/1/23 14:17
 * @change
 * @chang time
 * @class describe
 */
public class ReadItemAdapter extends RecyclerView.Adapter<ReadItemAdapter.ViewHolder> {

    private Context context;
    private List<SpeakRankWork> speaks;
    private List<SpeakRankWork> currentWorks = new ArrayList<>();

    private MediaPlayer mPlayer;
    private String userName;
    private String userImage;
    private String para;

    public ReadItemAdapter(String userImageUrl, String userName, String para) {
        userImage = userImageUrl;
        mPlayer = new MediaPlayer();
        this.para = para;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        context = group.getContext();
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_read_rank_work, group, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        Timber.tag("diao").d("onBindViewHolder: %s", currentWorks.get(i).imgsrc);
        Glide.with(context).load(userImage).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.mImageUserHead.setImageBitmap(resource);
            }
        });

        holder.mTextUsername.setText(userName);
        holder.mTextScore.setText(currentWorks.get(i).score + "");
        holder.mTextTime.setText(currentWorks.get(i).createdate);
        holder.mTextDownvoteCount.setText(currentWorks.get(i).getDownvoteCount() + "");
        holder.mTextUpvoteCount.setText(currentWorks.get(i).getUpvoteCount() + "");
        holder.item = currentWorks.get(i);
        holder.mTextReadSentence.setText(getSentence(currentWorks.get(i)));
        holder.mTextLabelStub.setLabelTextSize(26);
        holder.mTextLabelStub.setLabelBackgroundColor(R.color.colorPrimary);
        holder.mTextLabelStub.setLabelText(holder.item.idindex + "");


        if (holder.item.shuoshuoType == 4) {
            holder.mTextLabelStub.setLabelBackgroundColor(Color.parseColor("#FBA474"));
            holder.mTextLabelStub.setLabelText("播音");
        } else {
            holder.mTextLabelStub.setLabelBackgroundColor(R.color.colorPrimary);
            holder.mTextLabelStub.setLabelText("跟读");
        }


    }

    @Override
    public int getItemCount() {
        return currentWorks.size();
    }

    public void setData(List<SpeakRankWork> list) {
        speaks = list;
        for (int i = 0; i < speaks.size(); i++) {
//            if (speaks.get(i).idindex.split("000")[0].equals(para)||speaks.get(i).shuoshuoType==4) {
                currentWorks.add(speaks.get(i));
//            } else {
//
//            }
        }
        Collections.sort(currentWorks);
    }

    private String getSentence(SpeakRankWork speakRankWork) {
        NewTypeTextAOp helper = new NewTypeTextAOp(context);

        if ((speakRankWork.idindex + "").contains("000")) {
            String a = speakRankWork.idindex.split("000")[0];
            String b = speakRankWork.idindex.split("000")[1];
            if (speakRankWork.paraid == 1) {
                return helper.getSentence(speakRankWork.voaId, a, b, "A");
            } else if (speakRankWork.paraid == 2) {
                return helper.getSentence(speakRankWork.voaId, a, b, "B");
            } else if (speakRankWork.paraid == 3) {
                return helper.getSentence(speakRankWork.voaId, a, b, "C");
            }
            return helper.getSentence(speakRankWork.voaId, a, b, ACache.get(context).getAsString("testType"));
        }
        return "";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_user_head)
        CircleImageView mImageUserHead;
        @BindView(R.id.text_username)
        TextView mTextUsername;
        @BindView(R.id.text_time)
        TextView mTextTime;
        @BindView(R.id.linear_name_container)
        LinearLayout mLinearNameContainer;
        @BindView(R.id.text_label_stub)
        LabelTextView mTextLabelStub;
        @BindView(R.id.image_body)
        ImageView mImageBody;
        @BindView(R.id.text_read_sentence)
        TextView mTextReadSentence;
        @BindView(R.id.linear_audio_comment_container)
        LinearLayout mLinearAudioCommentContainer;
        @BindView(R.id.text_score)
        TextView mTextScore;
        @BindView(R.id.image_share)
        ImageView mImageShare;
        @BindView(R.id.image_upvote)
        ImageView mImageUpvote;
        @BindView(R.id.text_upvote_count)
        TextView mTextUpvoteCount;
        @BindView(R.id.image_downvote)
        ImageView mImageDownvote;
        @BindView(R.id.text_downvote_count)
        TextView mTextDownvoteCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        SpeakRankWork item;


        @OnClick(R.id.image_body)
        public void onMImageBodyClicked() {
            AnimationDrawable animationDrawable = (AnimationDrawable) mImageBody.getBackground();

            if (mPlayer.isPlaying()){
                mPlayer.pause();
                animationDrawable.stop();
            }else {
                playUrl(item.getShuoShuoUrl());
                animationDrawable.start();
            }
        }

        @OnClick(R.id.text_read_sentence)
        public void onMTextReadSentenceClicked() {
        }

        @OnClick(R.id.linear_audio_comment_container)
        public void onMLinearAudioCommentContainerClicked() {
        }

        @OnClick(R.id.text_score)
        public void onMTextScoreClicked() {
        }

        @OnClick(R.id.image_share)
        public void onMImageShareClicked() {
        }

        @OnClick(R.id.image_upvote)
        public void onMImageUpvoteClicked() {
            Call<SendGoodResponse> call = AbilityTestRequestFactory.getPublishApi().sendGood(item.id, "61001");
            call.enqueue(new Callback<SendGoodResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<SendGoodResponse> call, Response<SendGoodResponse> response) {
                    if (response.body().getResultCode().equals("001")) {
                        ToastUtil.showToast(context, "点赞成功");
                        mTextUpvoteCount.setText(String.valueOf(item.getUpvoteCount() + 1));

                    }
                }

                @Override
                public void onFailure(Call<SendGoodResponse> call, Throwable t) {

                }
            });
        }

        @OnClick(R.id.image_downvote)
        public void onMImageDownvoteClicked() {
            Call<SendGoodResponse> call = AbilityTestRequestFactory.getPublishApi().sendGood(item.id, "61002");
            call.enqueue(new Callback<SendGoodResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<SendGoodResponse> call, Response<SendGoodResponse> response) {
                    if (response.body().getResultCode().equals("001")) {
                        mTextDownvoteCount.setText(String.valueOf(item.getDownvoteCount() + 1));

                    }
                }

                @Override
                public void onFailure(Call<SendGoodResponse> call, Throwable t) {

                }
            });
        }


    }

    private void playUrl(String url) {
        Timber.tag("diao").d(url);

        mPlayer.reset();
        try {
            Timber.tag("diao").d(url);
            mPlayer.setDataSource(url.replace("iyuba.com", "iyuba.cn"));
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void releasePlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
