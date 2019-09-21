package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.discover.protocol.RequestAddAttention;
import com.iyuba.core.discover.protocol.ResponseAddAttention;
import com.iyuba.core.manager.AccountManager;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.sqlite.mode.SearchItem;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.widget.dialog.CustomToast;

import java.util.ArrayList;

/**
 * 查询结果 初步废弃了
 *
 * @author 陈彤
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context mContext;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    notifyDataSetChanged();
                    break;
                case 2:
                    CustomToast.showToast(mContext,
                            R.string.social_success_attention, 1000);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.social_attentioned,
                            1000);
                    break;
                case 4:
                    CustomToast.showToast(mContext,
                            R.string.social_failed_attention, 1000);
                    break;
            }
        }
    };
    private ArrayList<SearchItem> mList = new ArrayList<SearchItem>();
    private ViewHolder viewHolder;

    public SearchResultAdapter(Context mContext) {
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

    /*
     * public void clearList(){ mList.clear(); }
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final int pos = position;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.searchlist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.username = convertView
                    .findViewById(R.id.searchlist_username);
            viewHolder.userImageView = convertView
                    .findViewById(R.id.searchlist_portrait);
            viewHolder.followButton = convertView
                    .findViewById(R.id.follow_button);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.username.setText(mList.get(position).username);
        viewHolder.followButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ClientSession.Instance().asynGetResponse(
                        new RequestAddAttention(AccountManager
                                .Instace(mContext).userId, mList.get(pos).uid),
                        new IResponseReceiver() {
                            @Override
                            public void onResponse(BaseHttpResponse response,
                                                   BaseHttpRequest request, int rspCookie) {

                                ResponseAddAttention rs = (ResponseAddAttention) response;
                                if (rs.result.equals("500")) {
                                    handler.sendEmptyMessage(2);
                                } else if (rs.result.equals("502")) {
                                    handler.sendEmptyMessage(3);
                                } else if (rs.result.equals("503")) {
                                    handler.sendEmptyMessage(4);
                                }
                            }


                        });
            }
        });
        GitHubImageLoader.Instace(mContext).setCirclePic(mList.get(position).uid,
                viewHolder.userImageView);
        return convertView;
    }

    public void addList(ArrayList<SearchItem> list) {

        mList.clear();
        mList.addAll(list);
    }

    class ViewHolder {
        TextView username;
        ImageView userImageView;
        Button followButton;
    }

}
