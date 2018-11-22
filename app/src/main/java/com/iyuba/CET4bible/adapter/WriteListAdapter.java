package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WriteDataManager;
import com.iyuba.CET4bible.sqlite.mode.Write;
import com.iyuba.CET4bible.widget.CustomBgView;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WriteListAdapter extends BaseAdapter {
    private final static int[] colorful = new int[]{0xffe14438, 0xff826cab,
            0xff62aa46, 0xffe38f2b, 0xff4fbdf0};
    private Context mContext;
    private ArrayList<Write> mList;
    private CustomBgView item_bg;
    private StringBuffer sb;

    public WriteListAdapter(Context context) {
        mContext = context;
        mList = WriteDataManager.Instance().writeList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Write getItem(int arg0) {
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
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_write, null);
            curViewHolder.item_bg = convertView.findViewById(R.id.item_bg);
            curViewHolder.title = convertView.findViewById(R.id.title);
            curViewHolder.desc = convertView.findViewById(R.id.desc);
            curViewHolder.append = convertView.findViewById(R.id.append);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }

        final Write write = mList.get(position);
        String month = write.num.substring(4, 6);
        if (month.equals("12")) {
            curViewHolder.item_bg.setBg(R.drawable.winter);
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("12月");
        } else if (month.equals("06")) {
            curViewHolder.item_bg.setBg(R.drawable.summer);
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("6月");
        } else {
            curViewHolder.item_bg.setBg(R.drawable.winter);
            curViewHolder.item_bg.setText(write.num.substring(0, 4) + "年");
            curViewHolder.item_bg.setSubText("1月");
        }
        curViewHolder.title.setText(write.name);
        sb = new StringBuffer();
        if (!TextUtils.isEmpty(write.title) && !TextUtils.isEmpty(write.catename)) {
            sb.append(write.title).append("--").append(write.catename);
        } else {
            sb.append(write.question);
        }
        curViewHolder.desc.setText(sb.toString());
        curViewHolder.append.setBackgroundColor(colorful[position % colorful.length]);
        return convertView;
    }


    public class ViewHolder {
        TextView title;
        TextView desc;
        ImageView append;
        CustomBgView item_bg;
    }

}
