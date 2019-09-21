package com.iyuba.core.me.adapter;
/**
 * 心情评论适配器
 *
 * @author 陈彤
 * @version 1.0
 */

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.me.sqlite.mode.DoingsCommentInfo;
import com.iyuba.core.me.sqlite.mode.Emotion;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.Expression;

import java.util.ArrayList;

public class DoingsCommentAdapter extends BaseAdapter {
    public ArrayList<DoingsCommentInfo> mList = new ArrayList<DoingsCommentInfo>();
    DoingsCommentInfo item = new DoingsCommentInfo();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    break;
            }
        }
    };
    private Context mContext;
    private ViewHolder viewHolder;

    /**
     * @param mContext
     * @param mList
     */
    public DoingsCommentAdapter(Context mContext,
                                ArrayList<DoingsCommentInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * @param mContext
     */
    public DoingsCommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    public void setData(ArrayList<DoingsCommentInfo> doingsCommentList) {
        mList = doingsCommentList;
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void clearList() {
        mList.clear();
        handler.sendEmptyMessage(0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_doings, null);
            viewHolder = new ViewHolder();
            viewHolder.message = convertView
                    .findViewById(R.id.doingslist_message);
            viewHolder.time = convertView
                    .findViewById(R.id.doingslist_time);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.doingslist_userPortrait);
            viewHolder.username = convertView
                    .findViewById(R.id.doingslist_username);
            viewHolder.replyCommentNum = convertView
                    .findViewById(R.id.doingslist_replyNum);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mList.get(position).message.equals("")
                || mList.get(position).message == null) {
            viewHolder.message.setText("");
        } else {
            String zhengze = "image[0-9]{2}|image[0-9]";
            Emotion emotion = new Emotion();
            mList.get(position).message = emotion
                    .replace(mList.get(position).message);
            try {
                SpannableString spannableString = Expression
                        .getExpressionString(mContext,
                                mList.get(position).message, zhengze);
                viewHolder.message.setText(spannableString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        String name = mList.get(position).username;
        if (TextUtils.isEmpty(name) || "null".equals(name)) {
            name = mList.get(position).uid;
        }

        viewHolder.username.setText(name);
        if (mList.get(position).userBitmap != null) {
            viewHolder.userImageView
                    .setImageBitmap(mList.get(position).userBitmap);
        } else {
            viewHolder.userImageView.setImageResource(R.drawable.defaultavatar);
        }
        long time = Long.parseLong(mList.get(position).dateline) * 1000;
        viewHolder.time.setText(DateFormat.format("yyyy-MM-dd kk:mm", time));
        viewHolder.userImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PersonalHome.class);
                intent.putExtra("fanuid", mList.get(position).uid);
                mContext.startActivity(intent);
            }

        });
        GitHubImageLoader.Instace(mContext).setCirclePic(mList.get(position).uid,
                viewHolder.userImageView);
        return convertView;
    }

    class ViewHolder {
        TextView username;
        TextView time;
        TextView message;
        ImageView userImageView;
        TextView replyCommentNum;
    }
}
