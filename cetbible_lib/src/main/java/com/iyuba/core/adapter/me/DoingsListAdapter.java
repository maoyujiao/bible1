package com.iyuba.core.adapter.me;
/**
 * 心情适配器
 *
 * @author 陈彤
 * @version 1.0
 */

import android.content.Context;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.sqlite.mode.me.DoingsInfo;
import com.iyuba.core.sqlite.mode.me.Emotion;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.Expression;

import java.util.ArrayList;

public class DoingsListAdapter extends BaseAdapter {
    public ArrayList<DoingsInfo> mList = new ArrayList<DoingsInfo>();
    private Context mContext;
    private ViewHolder viewHolder;

    /**
     * @param mContext
     * @param mList
     */
    public DoingsListAdapter(Context mContext, ArrayList<DoingsInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * @param mContext
     */
    public DoingsListAdapter(Context mContext) {
        this.mContext = mContext;
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

    public void setData(ArrayList<DoingsInfo> list) {
        mList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final DoingsInfo curDoings = mList.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_doings, null);
            viewHolder = new ViewHolder();
            viewHolder.replyNum = convertView
                    .findViewById(R.id.doingslist_replyNum);
            viewHolder.message = convertView
                    .findViewById(R.id.doingslist_message);
            viewHolder.time = convertView
                    .findViewById(R.id.doingslist_time);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.doingslist_userPortrait);
            viewHolder.username = convertView
                    .findViewById(R.id.doingslist_username);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.replyNum.setText(curDoings.replynum);
        String zhengze = "image[0-9]{2}|image[0-9]";
        curDoings.message = Emotion.replace(curDoings.message);
        try {
            SpannableString spannableString = Expression.getExpressionString(
                    mContext, curDoings.message, zhengze);
            viewHolder.message.setText(spannableString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        viewHolder.username.setText(curDoings.username);
        long time = Long.parseLong(curDoings.dateline) * 1000;
        viewHolder.time.setText(DateFormat.format("yyyy-MM-dd kk:mm", time));
        GitHubImageLoader.Instace(mContext).setCirclePic(mList.get(position).uid,
                viewHolder.userImageView);
        return convertView;
    }

    class ViewHolder {
        TextView username;
        TextView time;
        TextView replyNum;
        TextView message;
        ImageView userImageView;
    }
}
