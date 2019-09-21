package com.iyuba.core.me.adapter;
/**
 * 动态适配器
 *
 * @author 陈彤
 * @version 1.0
 */

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.me.sqlite.mode.NewDoingsInfo;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;


public class NewDoingsListAdapter extends BaseAdapter {
    public ArrayList<NewDoingsInfo> mList = new ArrayList<NewDoingsInfo>();
    private Context mContext;
    private ViewHolder viewHolder;

    /**
     * @param mContext
     * @param mList
     */
    public NewDoingsListAdapter(Context mContext, ArrayList<NewDoingsInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * @param mContext
     */
    public NewDoingsListAdapter(Context mContext) {
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

    public void clearList() {
        mList.clear();
    }

    public void addList(ArrayList<NewDoingsInfo> list) {
        mList.addAll(list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final NewDoingsInfo curDoings = mList.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_new_doings, null);
            viewHolder = new ViewHolder();
            viewHolder.replyNum = convertView
                    .findViewById(R.id.doingslist_replyNum);
//			viewHolder.message = (TextView) convertView
//					.findViewById(R.id.doingslist_message);
            viewHolder.body = convertView
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

        viewHolder.body.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        if (curDoings.idtype.equals("questionid") || curDoings.idtype.equals("picid")) {

            viewHolder.body.setText(Html.fromHtml(
                    "<html><head></head><body><em><font color=\"#0077d5\">" + curDoings.title + "：</em><font color=\"#808080\">" + curDoings.body + "</body></html>"));
//			viewHolder.body.setText(curDoings.title+ curDoings.body);

        } else if (curDoings.idtype.equals("uid") || curDoings.idtype.equals("doid")) {

            viewHolder.body.setText(Html.fromHtml(
                    "<html><head></head><body><em><font color=\"#0077d5\">" + curDoings.title + "</em>" + "</body></html>"));
//			viewHolder.body.setText(curDoings.title);

        }
//		else{
//			Emotion emotion = new Emotion();
////			curDoings.message = emotion.replace(curDoings.message);
//			curDoings.body = emotion.replace(curDoings.body);
//			try {
////				SpannableString spannableString = Expression.getExpressionString(
////						mContext, curDoings.message, zhengze);
////				viewHolder.message.setText(spannableString);
//				SpannableString spannableString = Expression.getExpressionString(
//						mContext, curDoings.body, zhengze);
//				viewHolder.body.setText(spannableString);
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			}
//		}
        if (TextUtils.isEmpty(curDoings.username) || "null".equals(curDoings.username)) {
            curDoings.username = curDoings.uid;
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
        //		TextView message;
        TextView body;
        ImageView userImageView;
    }
}
