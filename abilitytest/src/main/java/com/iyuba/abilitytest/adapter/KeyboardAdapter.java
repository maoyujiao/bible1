package com.iyuba.abilitytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.iyuba.abilitytest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class KeyboardAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> letters = new ArrayList<String>();

    public KeyboardAdapter(Context mContext, List<String> letters) {
        this.mContext = mContext;
        this.letters.addAll(letters);
        this.letters.add("mmm");
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int i) {
        return letters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 19) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_keyboard, null);
            }
            TextView tv = convertView.findViewById(R.id.keyboard_text);
            ImageView iv = convertView.findViewById(R.id.keyboard_ensure);
            tv.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.VISIBLE);

            return convertView;
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gv_keyboard, null);
            }
            TextView tv = convertView.findViewById(R.id.keyboard_text);
            ImageView iv = convertView.findViewById(R.id.keyboard_ensure);

            tv.setVisibility(View.VISIBLE);
            iv.setVisibility(View.INVISIBLE);

            tv.setText(letters.get(position));
            return convertView;
        }
    }
}
