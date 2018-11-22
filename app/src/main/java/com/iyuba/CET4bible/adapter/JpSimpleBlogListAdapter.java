package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.Blog;

import java.util.ArrayList;

public class JpSimpleBlogListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Blog> blogs = new ArrayList<>();

    public JpSimpleBlogListAdapter(Context context, ArrayList<Blog> blogs) {
        this.mContext = context;
        this.blogs = blogs;
    }

    @Override
    public int getCount() {
        return blogs == null ? 0 : (blogs.size() > 3 ? 3 : blogs.size());
    }

    @Override
    public Object getItem(int position) {

        return blogs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.blog_item_simplify, null);
            holder.tv_index = convertView.findViewById(R.id.tv_index);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_createTime = convertView.findViewById(R.id.tv_createTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int index = position + 1;
        holder.tv_index.setText(String.valueOf(index));
        holder.tv_title.setText(blogs.get(position).title);
        try {
            holder.tv_createTime.setText(blogs.get(position).createtime);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.tv_createTime.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_index;
        private TextView tv_title;
        private TextView tv_createTime;

    }

}
