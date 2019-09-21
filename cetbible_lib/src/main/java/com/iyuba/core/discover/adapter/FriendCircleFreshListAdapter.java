package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.discover.sqlite.mode.FreshContent;
import com.iyuba.core.sqlite.mode.me.Emotion;
import com.iyuba.core.teacher.activity.ShowLargePicActivity;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.Expression;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FriendCircleFreshListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FreshContent> mList = new ArrayList<FreshContent>();

    public FriendCircleFreshListAdapter(Context context) {
        mContext = context;
    }

    public FriendCircleFreshListAdapter(Context context, ArrayList<FreshContent> list) {
        mContext = context;
        mList = list;
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

    public void addList(ArrayList<FreshContent> courseList) {
        mList.addAll(courseList);
    }

    public void clearList() {
        mList.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final FreshContent cp = mList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.friend_circle_fresh_item, null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.head_pic);
            viewHolder.name = convertView.findViewById(R.id.index_name);
            viewHolder.action = convertView.findViewById(R.id.index_action);
            viewHolder.content = convertView.findViewById(R.id.index_content);
            viewHolder.content_pic = convertView.findViewById(R.id.content_pic);
            viewHolder.time = convertView.findViewById(R.id.index_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GitHubImageLoader.Instace(mContext).setCirclePic(
                cp.uid, viewHolder.head);

//		GitHubImageLoader.Instance(mContext).setCirclePic("http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="+cp.uid, viewHolder.head, R.drawable.nearby_no_icon);

        viewHolder.name.setText(cp.username);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long time = sdf.parse(cp.dateline).getTime();
            viewHolder.time.setText(formatTime(time));
        } catch (ParseException e) {

            e.printStackTrace();
        }

        String title = cp.title;

        if (cp.idtype.equals("doid")) {
            if (cp.title.contains("：")) {
                title = cp.title
                        .substring(cp.title.indexOf("：") + 1);
            }
        }
        if (title == null) title = "";
        String zhengze = "image[0-9]{2}|image[0-9]";
        Emotion emotion = new Emotion();
        title = Emotion.replace(title);
        SpannableString spannableString = Expression.getExpressionString(
                mContext, title, zhengze);
        viewHolder.action.setText(spannableString);

//			String body=cp.body;
//			if(body==null) body="";
//			String content = emotion.replace(Html.fromHtml(body).toString());
//			SpannableString spannableString2 = Expression.getExpressionString(
//					mContext, content, zhengze);
//
        viewHolder.content.setText(Html.fromHtml(cp.body));


        if (cp.image.equals("") || cp.image == null) {
            viewHolder.content_pic.setVisibility(View.GONE);
        } else {
            viewHolder.content_pic.setVisibility(View.VISIBLE);
            String pic = "";
            if (cp.idtype.equals("picid")) {
                String theImage = cp.image.replace(".jpg", "-s.jpg");
                String pic1 = Constant.PIC_ABLUM__ADD + theImage;
                pic = Constant.PIC_ABLUM__ADD + cp.image;

                ImageLoader.getInstance().displayImage(pic, viewHolder.content_pic);

//				GitHubImageLoader.Instance(mContext).setPic(pic1, viewHolder.content_pic, R.drawable.nearby_no_icon);
            } else {
                pic = cp.image;
                ImageLoader.getInstance().displayImage(pic, viewHolder.content_pic);
//				GitHubImageLoader.Instance(mContext).setPic(cp.image, viewHolder.content_pic, R.drawable.nearby_no_icon);
            }
            Log.e("iyuba", pic);

            final String f = pic;
            viewHolder.content_pic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, ShowLargePicActivity.class);
                    intent.putExtra("pic", f.replace("-s.jpg", ".jpg"));
                    mContext.startActivity(intent);
                }

            });

        }

        return convertView;
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        Date date2 = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar.setTime(date);
        calendar2.setTime(date2);

        if (calendar2.get(Calendar.DAY_OF_YEAR)
                - calendar.get(Calendar.DAY_OF_YEAR) > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        } else if (System.currentTimeMillis() - time > 60 * 60 * 1000) {
            return (System.currentTimeMillis() - time) / (60 * 60 * 1000)
                    + "小时之前";
        } else if (System.currentTimeMillis() - time > 60 * 1000) {
            return (System.currentTimeMillis() - time) / (60 * 1000) + "分钟之前";
        } else if (System.currentTimeMillis() - time > 60) {

            return (System.currentTimeMillis() - time) / (1000) + "秒之前";

        } else if (System.currentTimeMillis() - time == 0) {
            return "1秒之前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        }

    }

    public class ViewHolder {
        ImageView head;
        TextView name;
        TextView action;
        TextView content;
        ImageView content_pic;
        TextView time;

    }
}