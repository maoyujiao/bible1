package com.iyuba.CET4bible.iyulive.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.fragment.MicroClassListBean;
import com.iyuba.configation.Constant;
import com.iyuba.core.thread.GitHubImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 作者：renzhy on 16/7/7 16:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public class HomeCourseListAdapter extends BaseAdapter {

    private Context mContext;
    private String allPicUrl;
    private ArrayList<MicroClassListBean.DataBean> liveList;
    private OnItemClickListener onCBItemClickListener;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    //微课adapter
    public HomeCourseListAdapter(Context mContext, ArrayList<MicroClassListBean.DataBean> list) {
        this.mContext = mContext;
        liveList = list;
    }

    //添加微课的集合
    public void addList(ArrayList<MicroClassListBean.DataBean> live) {
        liveList.addAll(live);
        Log.d("测试", "HomeCourseListAdapter: " + liveList.size());
    }


    public void clearList() {
        //liveList.clear();
        //notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return liveList.size() > 1 ? 1 : liveList.size();
    }

    //获取微课的课程yly
    @Override
    public MicroClassListBean.DataBean getItem(int position) {
        return liveList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //微课课程的getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MicroClassListBean.DataBean cp = liveList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.microclass_item_common_mobclasslist, null);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.coursePack_title);
            viewHolder.tvclassNum = convertView.findViewById(R.id.tv_courseNum1);
            viewHolder.courseNum = convertView.findViewById(R.id.coursePack_courseNum);
            viewHolder.content = convertView.findViewById(R.id.coursePackDesc_content);
            viewHolder.pic = convertView.findViewById(R.id.coursePack_pic);
            viewHolder.oldPrice = convertView.findViewById(R.id.tv_coursePack_oldprice);
            viewHolder.newPrice = convertView.findViewById(R.id.tv_coursePack_newprice);
            viewHolder.viewCount = convertView.findViewById(R.id.tv_coursePack_viewCount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.title.setText(cp.getName());
        viewHolder.content.setText(cp.getDesc());
        viewHolder.tvclassNum.setText(cp.getClassNum() + "");
        viewHolder.oldPrice.setText("原:" + Integer.parseInt(cp.getRealprice()));
        viewHolder.newPrice.setText("现:" + Integer.parseInt(cp.getPrice()) + "爱语币");
        DecimalFormat df = new java.text.DecimalFormat("#.0");
        if (cp.getViewCount() > 10000) {
            viewHolder.viewCount.setText(df.format((float) (cp.getViewCount() / 10000.0)) + "万");
        } else {
            viewHolder.viewCount.setText(cp.getViewCount() + "");
        }
        allPicUrl = Constant.MOB_CLASS_PACK_IMAGE + cp.getPic() + ".jpg";
        GitHubImageLoader.Instace(mContext).setPic(allPicUrl, viewHolder.pic, R.drawable.nearby_no_icon);
        //GitHubImageLoader.Instace(mContext).setPic(allPicUrl, viewHolder.pic,R.drawable.nearby_no_icon);
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        TextView tvclassNum;
        ImageView courseNum;
        TextView content;
        ImageView pic;
        TextView oldPrice;
        TextView newPrice;
        TextView viewCount;
    }

    // 用来设置每个item的接听
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
