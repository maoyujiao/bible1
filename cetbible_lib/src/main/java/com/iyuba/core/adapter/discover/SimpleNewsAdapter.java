package com.iyuba.core.adapter.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.sqlite.mode.CommonNews;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;

/**
 * 简版新闻列表适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class SimpleNewsAdapter extends BaseAdapter {
    public boolean modeDelete = false;
    private Context mContext;
    private ArrayList<CommonNews> mList = new ArrayList<CommonNews>();

    public SimpleNewsAdapter(Context context, ArrayList<CommonNews> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public CommonNews getItem(int arg0) {

        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CommonNews news = mList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_common_news, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView
                    .findViewById(R.id.artical_title);
            viewHolder.time = convertView
                    .findViewById(R.id.artical_time);
            viewHolder.content = convertView
                    .findViewById(R.id.artical_content);
            viewHolder.pic = convertView
                    .findViewById(R.id.news_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(news.title);
        viewHolder.time.setText(news.time);
        viewHolder.content.setText(news.content);
        GitHubImageLoader.Instace(mContext).setPic(news.picUrl, viewHolder.pic,
                R.drawable.nearby_no_icon);
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        TextView time;
        TextView content;
        ImageView pic;
    }
}
