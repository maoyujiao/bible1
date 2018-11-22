package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.widget.CustomBgView;

import java.util.ArrayList;
import java.util.List;

public class ReadingAdapter extends BaseAdapter {
    private final int[] colorful = new int[]{R.color.item_color_1, R.color.item_color_2,
            R.color.item_color_3, R.color.item_color_4, R.color.item_color_5};

    private Context mContext;
    private List<String> packNames = new ArrayList<String>();

    public ReadingAdapter(List<String> packNames, Context mContext) {
        this.packNames = packNames;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return packNames.size();
    }

    @Override
    public Object getItem(int position) {
        return packNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String packName = packNames.get(position);

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_reading, null);
            viewHolder = new ViewHolder();
            viewHolder.PackName = convertView.findViewById(R.id.reading_title);
            viewHolder.bgView = convertView.findViewById(R.id.bg_item_reading);
            viewHolder.append = convertView.findViewById(R.id.append);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String month = packName.substring(5, 7);
        if (month.equals("12")) {
            viewHolder.bgView.setBg(R.drawable.winter);
            viewHolder.bgView.setText(packName.substring(0, 5));
            viewHolder.bgView.setSubText("12月");
        } else {
            viewHolder.bgView.setBg(R.drawable.summer);
            viewHolder.bgView.setText(packName.substring(0, 5));
            viewHolder.bgView.setSubText("6月");
        }
        viewHolder.append.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(),
                colorful[position % colorful.length], mContext.getTheme()));

        viewHolder.PackName.setText(packName);
        return convertView;
    }

    public class ViewHolder {
        private TextView PackName;
        private CustomBgView bgView;
        private View append;
    }
}
