package com.iyuba.core.me.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.sqlite.mode.Fans;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;

/**
 * 粉丝适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class FanListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Fans> mList = new ArrayList<Fans>();
    private ViewHolder viewHolder;

    /**
     * @param mContext
     */
    public FanListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param mList
     */
    public FanListAdapter(Context mContext, ArrayList<Fans> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        if (position == 0) {
            return 0;
        } else {
            return mList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void setData(ArrayList<Fans> List) {
        mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Fans curFans = mList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_fans, null);
            viewHolder = new ViewHolder();
            viewHolder = new ViewHolder();
            viewHolder.username = convertView
                    .findViewById(R.id.username);
            viewHolder.message = convertView
                    .findViewById(R.id.content);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(curFans.username) || "null".equals(curFans.username)) {
            curFans.username = curFans.uid;
        }

        viewHolder.username.setText(curFans.username);
        viewHolder.message.setText(curFans.doing);
        GitHubImageLoader.Instace(mContext).setCirclePic(curFans.uid,
                viewHolder.userImageView);
        return convertView;
    }

    public class ViewHolder {
        TextView username;
        TextView message;// 当前状态
        ImageView userImageView;
    }
}
