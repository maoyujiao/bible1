/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.sqlite.mode.Blog;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.Random;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class BlogListAdapter extends BaseAdapter {
    private final static int[] colorful = new int[]{0xffe14438, 0xff826cab,
            0xff62aa46, 0xffe38f2b, 0xff4fbdf0};
    private Context mContext;
    private ArrayList mList = new ArrayList<Blog>();

    public BlogListAdapter(Context context, ArrayList list) {
        mContext = context;
        mList = list;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder curViewHolder;
        if (convertView == null) {
            curViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_blog, null);
            curViewHolder.title = convertView
                    .findViewById(R.id.title);
            curViewHolder.time = convertView.findViewById(R.id.time);
            curViewHolder.desc = convertView.findViewById(R.id.desc);
            curViewHolder.seetimes = convertView
                    .findViewById(R.id.seetime);
            curViewHolder.append = convertView
                    .findViewById(R.id.append);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        if (mList.get(position) instanceof NativeResponse) {
            NativeResponse nativeResponse = (NativeResponse) mList.get(position);
            nativeResponse.recordImpression(convertView);

            curViewHolder.title.setText(nativeResponse.getTitle());
            curViewHolder.title.getPaint().setFakeBoldText(true);
            curViewHolder.desc.setText("");
            curViewHolder.time.setText("推广");
            curViewHolder.append.setBackgroundColor(colorful[position % colorful.length]);
            curViewHolder.seetimes.setText(
                    mContext.getString(R.string.see1) + new Random().nextInt(999) + mContext.getString(R.string.see2)
            );
        }

        if (mList.get(position) instanceof Blog) {

            final Blog blog = (Blog) mList.get(position);
            curViewHolder.title.setText(blog.title);
            curViewHolder.title.getPaint().setFakeBoldText(false);
            curViewHolder.time.setText(blog.createtime.split(" ")[0]);
            curViewHolder.desc.setText(Html.fromHtml(blog.desccn));
            curViewHolder.append.setBackgroundColor(colorful[position
                    % colorful.length]);
            curViewHolder.seetimes.setText(mContext.getString(R.string.see1) + blog.readcount + mContext.getString(R.string.see2));
        }
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        TextView time;
        TextView desc;
        TextView seetimes;
        ImageView append;
    }

}
