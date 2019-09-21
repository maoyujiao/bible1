package com.iyuba.core.adapter.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.sqlite.mode.me.School;

import java.util.ArrayList;

/**
 * 学校列表
 *
 * @author 陈彤
 * @version 1.0
 * @time 14.5.28
 */

public class SchoolListAdapter extends BaseAdapter {
    public ViewHolder viewHolder;
    private Context mContext;
    private ArrayList<School> mList = new ArrayList<School>();

    public SchoolListAdapter(Context context, ArrayList<School> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void setData(ArrayList<School> list) {
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final School school = mList.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_school, null);
            viewHolder = new ViewHolder();
            viewHolder.province = convertView
                    .findViewById(R.id.province);
            viewHolder.school = convertView
                    .findViewById(R.id.schoolname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.province.setText(school.province);
        String school_name = school.school_name.trim();
        viewHolder.school.setText(school_name);
        return convertView;
    }

    public class ViewHolder {
        private TextView province;
        private TextView school;
    }

}
