package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.widget.CustomBgView;
import com.iyuba.CET4bible.write.WriteActivity;
import com.iyuba.base.BaseRecyclerViewAdapter;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.nativeads.ListVideoAdRenderer;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.video.VideoStrategy;

import java.util.List;

/**
 * FavoriteAdapter
 *
 * @author wayne
 * @date 2017/12/12
 */
public class FavoriteTranslateAdapter extends BaseRecyclerViewAdapter {
    private final static int[] colorful = new int[]{0xffe14438, 0xff826cab,
            0xff62aa46, 0xffe38f2b, 0xff4fbdf0};

    private final static int[] drawables = new int[]{
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3,
            R.drawable.icon4,
            R.drawable.icon5,
            R.drawable.icon6};
    private final ListVideoAdRenderer youdaoAdRenders;

    private List mList;
    private boolean isWrite = false;
    private boolean isHome = false;

    public FavoriteTranslateAdapter(Context mContext, List list , Boolean isHome ) {
        super(mContext);
        this.mList = list;
        this.isHome = isHome;

        final VideoStrategy videoStrategy = YouDaoAd.getYouDaoOptions()
                .getDefaultVideoStrategy();
        videoStrategy.setPlayVoice(true);
        videoStrategy.setVisiblePlay(true);
        //根据需要设置视频策略
        YouDaoAd.getYouDaoOptions().setVideoStrategy(videoStrategy);
        youdaoAdRenders = new ListVideoAdRenderer(
                new ViewBinder.Builder(R.layout.youdao_video_ad_item_small)
                        .videoId(R.id.mediaView).titleId(R.id.native_title)
                        .textId(R.id.native_content)
                        .build());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new VideoHolder(youdaoAdRenders.createAdView(mContext, parent));
        }
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_translate_write, parent, false));
    }

    public void setWrite(boolean isWrite) {
        this.isWrite = isWrite;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (getItemViewType(position) == 1) {
            final NativeResponse response = (NativeResponse) mList.get(position);
            response.recordImpression(holder.itemView);
            youdaoAdRenders.renderAdView(holder.itemView, response);
            VideoHolder h = (VideoHolder) holder;
            h.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(v);
                }
            });
            return;
        }


        final Holder curViewHolder = (Holder) holder;
        if (mList.get(position) instanceof NativeResponse) {
            final NativeResponse response = (NativeResponse) mList.get(position);
            response.recordImpression(curViewHolder.itemView);
            curViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(curViewHolder.itemView);
                }
            });
            curViewHolder.ivAd.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(response.getMainImageUrl())
                    .error(R.drawable.nearby_no_icon2)
                    .placeholder(R.drawable.nearby_no_icon2)
                    .into(curViewHolder.ivAd);
            curViewHolder.item_bg.setVisibility(View.GONE);

            curViewHolder.title.setText(response.getTitle());
            curViewHolder.desc.setText("推广");
            return;
        }

        curViewHolder.ivAd.setVisibility(View.GONE);

        final Write write = (Write) mList.get(position);
        curViewHolder.item_bg.setVisibility(View.VISIBLE);
        String month = write.num.substring(4, 6);
        if (month.equals("12")) {
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("12月");
        } else if (month.equals("06")) {
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("6月");
        } else {
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("1月");
        }

        curViewHolder.item_bg.setBg(drawables[position%6]);

        curViewHolder.title.setText(write.name);
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(write.title) && !TextUtils.isEmpty(write.catename)) {
            sb.append(write.title).append("--").append(write.catename);
        } else {
            sb.append(write.question);
        }
        curViewHolder.desc.setText(sb.toString());
        curViewHolder.append.setBackgroundColor(colorful[position % colorful.length]);


        curViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteActivity.class);
                intent.putExtra("type", isWrite ? "write" : "translate");
                intent.putExtra("write", write);
                WriteDataManager.Instance().write = write;
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isHome){
            return 4 ;
        }
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView append;
        CustomBgView item_bg;
        ImageView ivAd;

        public Holder(View itemView) {
            super(itemView);
            item_bg = itemView.findViewById(R.id.item_bg);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            append = itemView.findViewById(R.id.append);
            ivAd = itemView.findViewById(R.id.iv_ad);
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        View view;

        public VideoHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.ll);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (mList.get(position) instanceof NativeResponse) {
//            NativeResponse nativeResponse = (NativeResponse) mList.get(position);
//            if (nativeResponse.getVideoAd() != null) {
//                return 1;
//            }
//        }
        return 0;
    }
}
