package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.sqlite.mode.me.Emotion;
import com.iyuba.core.sqlite.mode.me.FindFriends;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.Expression;
import com.iyuba.core.util.ReadBitmap;

import java.util.ArrayList;

/**
 * 找朋友适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class FindFriendsListAdapter extends BaseAdapter {
    public ArrayList<FindFriends> mList = new ArrayList<FindFriends>();
    private Context mContext;
    private ViewHolder viewHolder;
    private int type;

    /**
     * @param mContext
     */
    public FindFriendsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param mList
     */
    public FindFriendsListAdapter(Context mContext,
                                  ArrayList<FindFriends> mList, int type) {
        this.mContext = mContext;
        this.mList = mList;
        this.type = type;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public FindFriends getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void setData(ArrayList<FindFriends> list, int type) {
        mList = list;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 无convertView，需要new出各个控件
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_findfriends,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.pic = convertView.findViewById(R.id.pic);
            viewHolder.username = convertView
                    .findViewById(R.id.username);
            viewHolder.content = convertView
                    .findViewById(R.id.content);
            viewHolder.gender = convertView
                    .findViewById(R.id.gender);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.username.setText(mList.get(position).userName);
        if (mList.get(position).doing != null) {
            String zhengze = "image[0-9]{2}|image[0-9]";
            Emotion emotion = new Emotion();
            mList.get(position).doing = Emotion
                    .replace(mList.get(position).doing);
            try {
                SpannableString spannableString = Expression
                        .getExpressionString(mContext,
                                mList.get(position).doing, zhengze);
                viewHolder.content.setText(spannableString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            viewHolder.content.setText(R.string.social_default_state);
        }
        if (mList.get(position).gender != null) {
            if (mList.get(position).gender.equals("2")) {
                viewHolder.gender.setImageBitmap(ReadBitmap.readBitmap(
                        mContext, R.drawable.user_info_female));
            } else {
                viewHolder.gender.setImageBitmap(ReadBitmap.readBitmap(
                        mContext, R.drawable.user_info_male));
            }
        } else {
            viewHolder.gender.setImageBitmap(ReadBitmap.readBitmap(mContext,
                    R.drawable.user_info_male));
        }
        GitHubImageLoader.Instace(mContext).setCirclePic(mList.get(position).userid,
                viewHolder.pic);
        switch (type) {
            case 0:// 公共账号
                break;
            case 1:// 周边的人
                int distance = (int) mList.get(position).distance;
                viewHolder.content.setText(distance
                        + mContext.getString(R.string.social_distance));
                break;
            case 2:// 共同应用
                viewHolder.content.setText(mContext
                        .getString(R.string.social_sameapp)
                        + mList.get(position).appName);
                break;
            case 3:// 好友推荐
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder {
        protected ImageView pic;
        protected TextView username;
        protected TextView content;
        protected ImageView gender;
    }
}
