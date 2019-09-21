package com.iyuba.core.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.sqlite.mode.ListenWordDetail;

import java.util.ArrayList;
import java.util.List;

public class LsDetailAdapter extends BaseAdapter {
    private List<ListenWordDetail> mList = new ArrayList<ListenWordDetail>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder curViewHolder;

    public LsDetailAdapter(Context mContext, List<ListenWordDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    public LsDetailAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addList(ArrayList<ListenWordDetail> lwdList) {
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

        final ListenWordDetail curDetail = mList.get(position);

        final int curPosition = position;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.wrword_frequent, null);

            curViewHolder = new ViewHolder();
            curViewHolder.lesson = convertView
                    .findViewById(R.id.lesson);
            curViewHolder.lessonId = convertView
                    .findViewById(R.id.lessonId);
            curViewHolder.testNum = convertView
                    .findViewById(R.id.test_num);
            curViewHolder.testWd = convertView
                    .findViewById(R.id.test_wd);
            curViewHolder.time = convertView.findViewById(R.id.time);

            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        curViewHolder.lesson.setText(curDetail.lesson);
        curViewHolder.lessonId.setText(curDetail.lessonId);
        curViewHolder.testNum.setText(curDetail.testNum);
        curViewHolder.testWd.setText(curDetail.testWd);
        curViewHolder.time.setText(curDetail.time);

        return convertView;
    }

    public static class ViewHolder {
        TextView lesson;
        TextView lessonId;
        TextView testNum;
        TextView testWd;
        TextView time;
    }
}
