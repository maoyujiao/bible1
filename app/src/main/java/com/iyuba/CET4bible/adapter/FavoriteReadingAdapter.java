package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.activity.ReadingTest;
import com.iyuba.CET4bible.widget.CustomBgView;
import com.iyuba.base.BaseRecyclerViewAdapter;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.List;

/**
 * FavoriteAdapter
 *
 * @author wayne
 * @date 2017/12/12
 */
public class FavoriteReadingAdapter extends BaseRecyclerViewAdapter<FavoriteReadingAdapter.Holder> {
    private final int[] colorful = new int[]{R.color.item_color_1, R.color.item_color_2,
            R.color.item_color_3, R.color.item_color_4, R.color.item_color_5};

    private List mList;

    public FavoriteReadingAdapter(Context mContext, List packNames) {
        super(mContext);
        this.mList = packNames;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_reading, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mList.get(position) instanceof NativeResponse) {
            final NativeResponse response = (NativeResponse) mList.get(position);
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
            holder.bg_item_reading.setVisibility(View.INVISIBLE);

            holder.reading_title.setText(response.getTitle() + "（推广）");
            return;
        }

        final String packName = (String) mList.get(position);
        holder.reading_title.setText(packName);
        holder.bg_item_reading.setVisibility(View.VISIBLE);
        String month = packName.substring(5, 7);
        if (month.equals("12")) {
            holder.bg_item_reading.setBg(R.drawable.winter);
            holder.bg_item_reading.setText(packName.substring(0, 5));
            holder.bg_item_reading.setSubText("12月");
        } else {
            holder.bg_item_reading.setBg(R.drawable.summer);
            holder.bg_item_reading.setText(packName.substring(0, 5));
            holder.bg_item_reading.setSubText("6月");
        }
        holder.append.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(),
                colorful[position % colorful.length], mContext.getTheme()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReadingTest.class)
                        .putExtra("PackName", packName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CustomBgView bg_item_reading;
        TextView reading_title;
        View append;
        ImageView ivAd;

        public Holder(View itemView) {
            super(itemView);
            bg_item_reading = itemView.findViewById(R.id.bg_item_reading);
            reading_title = itemView.findViewById(R.id.reading_title);
            append = itemView.findViewById(R.id.append);
            ivAd = itemView.findViewById(R.id.iv_ad);
        }
    }
}
