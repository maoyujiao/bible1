/**
 *
 */
package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.biblelib.R;
import com.iyuba.core.discover.protocol.RequestCancelAttention;
import com.iyuba.core.discover.protocol.ResponseCancelAttention;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.message.RequestAddAttention;
import com.iyuba.core.protocol.message.ResponseAddAttention;
import com.iyuba.core.sqlite.mode.me.MutualAttention;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;

/**
 * 相互关注适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class MutualAttentionAdapter extends BaseAdapter {
    public ArrayList<MutualAttention> mList = new ArrayList<MutualAttention>();
    private Context mContext;
    private ViewHolder viewHolder;
    private String fansId;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    break;
                case 2:
                    // 加关注
                    ClientSession.Instance().asynGetResponse(
                            new RequestAddAttention("10", fansId),
                            new IResponseReceiver() {
                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {


                                    ResponseAddAttention res = (ResponseAddAttention) response;
                                    if (res.result.equals("501")) {
                                        handler.sendEmptyMessage(5);
                                        handler.sendEmptyMessage(0);// 修改为已关注
                                    } else {
                                        handler.sendEmptyMessage(6);
                                    }
                                    handler.sendEmptyMessage(4);
                                }

                            });
                    break;
                case 3:
                    // 取消关注
                    ClientSession.Instance().asynGetResponse(
                            new RequestCancelAttention("10", fansId),
                            new IResponseReceiver() {

                                @Override
                                public void onResponse(BaseHttpResponse response,
                                                       BaseHttpRequest request, int rspCookie) {

                                    ResponseCancelAttention responseFansList = (ResponseCancelAttention) response;
                                    System.out.println(responseFansList.result
                                            + "!!!!!!!!!!!!");
                                    if (responseFansList.result.equals("510")) {
                                        handler.sendEmptyMessage(5);
                                        handler.sendEmptyMessage(0);// 修改为已关注
                                    } else {
                                        handler.sendEmptyMessage(5);
                                    }
                                    handler.sendEmptyMessage(4);
                                }

                            });
                    break;
                case 4:
                    //

                    break;
                case 5:
                    Toast.makeText(mContext, R.string.social_success_attention,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(mContext, R.string.social_cancle_attention,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * @param mContext
     * @param mList
     */
    public MutualAttentionAdapter(Context mContext,
                                  ArrayList<MutualAttention> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    /**
     * @param mContext
     */
    public MutualAttentionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    public void addList(ArrayList<MutualAttention> fansList) {
        mList.addAll(fansList);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MutualAttention curFans = mList.get(position);
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.mutualattentionlist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.username = convertView
                    .findViewById(R.id.mutualattentionlist_username);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.mutualattentionlist_portrait);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.username.setText(curFans.fusername);
        GitHubImageLoader.Instace(mContext).setCirclePic(curFans.followuid,
                viewHolder.userImageView);
        return convertView;
    }

    class ViewHolder {
        TextView username;
        ImageView userImageView;
    }
}
