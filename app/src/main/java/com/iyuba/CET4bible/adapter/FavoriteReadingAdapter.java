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
    private final static int[] drawables = new int[]{
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3,
            R.drawable.icon4,
            R.drawable.icon5,
            R.drawable.icon6};
    private List mList;
    private Boolean isHome;

    public FavoriteReadingAdapter(Context mContext, List packNames, Boolean isHome) {
        super(mContext);
        this.mList = packNames;
        this.isHome = isHome;
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
            holder.ivAd.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(response.getMainImageUrl())
                    .error(R.drawable.nearby_no_icon2)
                    .placeholder(R.drawable.nearby_no_icon2)
                    .into(holder.ivAd);
            holder.bg_item_reading.setVisibility(View.INVISIBLE);
            holder.reading_title.setText(response.getTitle() + "（推广）");
            return;
        }
        holder.ivAd.setVisibility(View.GONE);

        final String packName = (String) mList.get(position);
        holder.reading_title.setText(packName);
        holder.bg_item_reading.setVisibility(View.VISIBLE);
        holder.bg_item_reading.setBg(drawables[position%6]);

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
        if (isHome){
            return 4 ;
        }

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
