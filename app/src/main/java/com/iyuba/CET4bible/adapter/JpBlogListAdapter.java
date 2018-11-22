package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.iyuba.configation.Constant;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.nativeads.ListVideoAdRenderer;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.ViewBinder;
import com.youdao.sdk.video.VideoStrategy;

import java.util.ArrayList;
import java.util.Random;

public class JpBlogListAdapter extends BaseAdapter {
    private final static int[] colorful = new int[]{0xffe14438, 0xff826cab,
            0xff62aa46, 0xffe38f2b, 0xff4fbdf0};
    private Context mContext;
    private ArrayList mList = new ArrayList<>();
    ListVideoAdRenderer youdaoAdRenders = null;

    public JpBlogListAdapter(Context context, ArrayList list) {
        mContext = context;
        mList = list;

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
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder curViewHolder;
        if (convertView == null || convertView.getTag() == null) {
            curViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (Constant.APP_CONSTANT.isEnglish()) {
                convertView = vi.inflate(R.layout.item_blog, null);
            } else {
                convertView = vi.inflate(R.layout.item_blog_jp, null);
            }
            curViewHolder.title = convertView.findViewById(R.id.title);
            curViewHolder.time = convertView.findViewById(R.id.time);
            curViewHolder.desc = convertView.findViewById(R.id.desc);
            curViewHolder.seetimes = convertView.findViewById(R.id.seetime);
            curViewHolder.append = convertView.findViewById(R.id.append);
            curViewHolder.imageView = convertView.findViewById(R.id.iv_image);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            if (mList.get(position) instanceof NativeResponse) {
                View adView = convertView;

                NativeResponse bean = (NativeResponse) mList.get(position);
                if (bean.getVideoAd() == null) {
                    curViewHolder.title.setText(bean.getTitle());
                    curViewHolder.title.getPaint().setFakeBoldText(true);
                    curViewHolder.desc.setVisibility(View.GONE);
                    curViewHolder.time.setText("推广  ");
                    curViewHolder.seetimes.setText("浏览 " + new Random().nextInt(999) + " 次 ");
                    curViewHolder.append.setBackgroundColor(colorful[position % colorful.length]);
                    if (Constant.APP_CONSTANT.isEnglish()) {
                        curViewHolder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(bean.getMainImageUrl()).dontAnimate()
                                .placeholder(R.drawable.nearby_no_icon2)
                                .dontAnimate()
                                .into(curViewHolder.imageView);

                    } else {
                        curViewHolder.imageView.setVisibility(View.GONE);
                    }
                } else {
                    adView = youdaoAdRenders.createAdView(mContext, parent);
                    youdaoAdRenders.renderAdView(adView, bean);
                }
                bean.recordImpression(adView);
                return adView;
            }


            if (mList.get(position) instanceof Blog) {
                final Blog blog = (Blog) mList.get(position);
                curViewHolder.title.setText(blog.title);
                curViewHolder.title.getPaint().setFakeBoldText(false);
                try {
                    curViewHolder.time.setText(blog.createtime);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    curViewHolder.time.setText("");
                }
                curViewHolder.desc.setVisibility(View.GONE);
                curViewHolder.append.setBackgroundColor(colorful[position % colorful.length]);
                curViewHolder.seetimes.setText("浏览 " + blog.readcount + " 次 ");
                if (Constant.APP_CONSTANT.isEnglish()) {
                    curViewHolder.imageView.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(blog.url).dontAnimate()
                            .placeholder(R.drawable.nearby_no_icon2)
                            .into(curViewHolder.imageView);
                } else {
                    curViewHolder.imageView.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        TextView time;
        TextView desc;
        TextView seetimes;
        ImageView append;
        ImageView imageView;
    }

}
