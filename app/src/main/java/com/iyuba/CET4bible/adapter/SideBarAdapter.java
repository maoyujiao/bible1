package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;

import java.util.ArrayList;

public class SideBarAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> datas = new ArrayList<String>();
    private int[] picIds = new int[]{R.drawable.iv_me, R.drawable.iv_discover, R.drawable.iv_set};

    public SideBarAdapter(Context mContext, ArrayList<String> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public Object getItem(int position) {

        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_sidebar, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_fuction = convertView.findViewById(R.id.tv_fuction);
            viewHolder.iv_pic = convertView.findViewById(R.id.iv_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_fuction.setText(datas.get(position));
        viewHolder.iv_pic.setImageResource(picIds[position]);
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_fuction;
        private ImageView iv_pic;
    }
}
