package com.iyuba.core.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.sqlite.mode.WordDetail;

import java.util.ArrayList;
import java.util.List;


public class WdDetailAdapter extends BaseAdapter {
    private List<WordDetail> mList = new ArrayList<WordDetail>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder curViewHolder;

    public WdDetailAdapter(Context mContext, List<WordDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    public WdDetailAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addList(ArrayList<WordDetail> lwdList) {
        mList.addAll(lwdList);
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {

        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WordDetail curDetail = mList.get(position);

        final int curPosition = position;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.word_detail, null);

            curViewHolder = new ViewHolder();
            curViewHolder.word = convertView
                    .findViewById(R.id.word);
            curViewHolder.frequent = convertView
                    .findViewById(R.id.frequent);

            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        curViewHolder.word.setText(curDetail.word);
        curViewHolder.frequent.setText(curDetail.frequent);

        return convertView;
    }


    public static class ViewHolder {
        TextView word;
        TextView frequent;
    }
}
