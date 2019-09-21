package com.iyuba.abilitytest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityLessonInfoEntity;
import com.iyuba.abilitytest.listener.OnRecyclerViewItemClickListener;
import com.iyuba.abilitytest.utils.TestedUtil;
import com.iyuba.abilitytest.widget.CustomBgView;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;

/**
 * Created by liuzhenli on 2017/9/6.
 */

public class AbilityTestListAdapter extends RecyclerView.Adapter<AbilityTestListAdapter.ViewHolder> {

    private ArrayList titleInfos;
    private Context mContext;
    private OnRecyclerViewItemClickListener listener;

    public AbilityTestListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(ArrayList titleInfos) {
        this.titleInfos = titleInfos;
    }

    public void setOnitemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ability_testlist, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (titleInfos.get(position) instanceof NativeResponse) {
            holder.season.setVisibility(View.GONE);
            holder.ivAd.setVisibility(View.VISIBLE);

            final NativeResponse response = (NativeResponse) titleInfos.get(position);
            response.recordImpression(holder.itemView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    response.handleClick(holder.itemView);
                }
            });

            Glide.with(mContext).load(response.getMainImageUrl())
                    .error(R.drawable.nearby_no_icon2)
                    .placeholder(R.drawable.nearby_no_icon2)
                    .into(holder.ivAd);
            holder.rtv_title.setText("(推广)" + response.getTitle());
            return;
        }


        AbilityLessonInfoEntity titleInfo = (AbilityLessonInfoEntity) titleInfos.get(position);

        String month = titleInfo.lessonName.split("-")[1].substring(0, 2);
        if (month.equals("12")) {
            holder.season.setBg(R.drawable.winter);
            holder.season.setText(titleInfo.lessonName.substring(0, 4));
            holder.season.setSubText("12月");
        } else {
            holder.season.setBg(R.drawable.summer);
            holder.season.setText(titleInfo.lessonName.substring(0, 4));
            holder.season.setSubText("6月");
        }


        holder.season.setVisibility(View.VISIBLE);
        holder.ivAd.setVisibility(View.GONE);
        holder.rtv_title.setText(titleInfo.lessonName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        });
        if (TestedUtil.getInstance().isTested(titleInfo.lessonId + "")) {
            holder.rtv_title.setTextColor(mContext.getResources().getColor(R.color.text_purple_color));
        } else {
            holder.rtv_title.setTextColor(mContext.getResources().getColor(R.color.text_blue_color));
        }
    }

    @Override
    public int getItemCount() {
        return titleInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundTextView rtv_title;
        CustomBgView season;
        ImageView ivAd;

        public ViewHolder(View itemView) {
            super(itemView);
            rtv_title = itemView.findViewById(R.id.rtv_title);
            season = itemView.findViewById(R.id.view_season);
            ivAd = itemView.findViewById(R.id.iv_ad);
        }
    }
}
