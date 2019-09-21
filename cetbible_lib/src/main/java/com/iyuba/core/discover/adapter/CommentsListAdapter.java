package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.discover.sqlite.mode.BlogComment;

import java.util.List;

public class CommentsListAdapter extends BaseAdapter {
    final Html.ImageGetter imageGetter = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            source = source.substring(source.lastIndexOf("/") + 1);
            source = "/mnt/sdcard/com.iyuba.iyubaclient/comcom/" + source;
            drawable = Drawable.createFromPath(source);
            //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            drawable.setBounds(0, 0, 30, 30);
            System.out.println("drawable.getIntrinsicWidth()==" + drawable.getIntrinsicWidth() + "   Height===" + drawable.getIntrinsicHeight());
            return drawable;
        }
    };
    private Context mContext;
    private List<BlogComment> blogComments;
    private ViewHolder viewHolder;

    public CommentsListAdapter(Context context, List<BlogComment> blogComments) {
        this.mContext = context;
        this.blogComments = blogComments;
    }

    @Override
    public int getCount() {

        return blogComments.size();
    }

    @Override
    public Object getItem(int position) {

        return blogComments.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vInflater.inflate(R.layout.comment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.userName = convertView
                    .findViewById(R.id.comment_userName);
            viewHolder.content = convertView
                    .findViewById(R.id.comment_content);
            viewHolder.time = convertView
                    .findViewById(R.id.comment_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        long time = Long.parseLong(blogComments.get(position).dateline) * 1000;
        viewHolder.userName.setText(blogComments.get(position).author);
        //viewHolder.content.setText(blogComments.get(position).message);
        viewHolder.content.setText(Html.fromHtml(replaceExpressPath(blogComments.get(position).message), imageGetter, null));
        viewHolder.time.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss", time));
        return convertView;
    }

    private String replaceExpressPath(String str) {

        str = str.replaceAll("(.*?)src=\"(.*?)", "$1src=\"http://iyuba.cn/$2");
        return str;
    }

    class ViewHolder {
        TextView userName;
        TextView content;
        TextView time;
    }

}
