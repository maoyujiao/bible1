package com.iyuba.core.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.teacher.sqlite.mode.IyuTeacher;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;

/**
 * 老师列表适配器
 *
 * @author lmy
 * @version 1.0
 */
public class IyuTeacherListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<IyuTeacher> mList = new ArrayList<IyuTeacher>();
    private ViewHolder viewHolder;

    /**
     * @param mContext
     */
    public IyuTeacherListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param mContext
     * @param mList
     */
    public IyuTeacherListAdapter(Context mContext, ArrayList<IyuTeacher> mList) {
        this.mContext = mContext;
        this.mList = mList;
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

    public void addList(ArrayList<IyuTeacher> List) {
        mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.lib_list_item_teacher, null);
            viewHolder = new ViewHolder();


            convertView.setTag(viewHolder);
            viewHolder.teacher_img = convertView
                    .findViewById(R.id.teacher_img);
            viewHolder.teacher_star = convertView
                    .findViewById(R.id.teacher_star);
            viewHolder.teacher_name = convertView
                    .findViewById(R.id.teacher_name);
            viewHolder.teacher_desc = convertView
                    .findViewById(R.id.teacher_desc);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.teacher_name.setText(mList.get(position).tname + " ");
        viewHolder.teacher_desc.setText(mList.get(position).tonedesc);
        GitHubImageLoader.Instace(mContext).setPic(mList.get(position).tlevel,
                viewHolder.teacher_star, 0);
        GitHubImageLoader.Instace(mContext).setCirclePic(mList.get(position).timg,
                viewHolder.teacher_img, R.drawable.noavatar_small);


        return convertView;
    }

    public class ViewHolder {
        ImageView teacher_img, teacher_star;
        TextView teacher_name;
        TextView teacher_desc;
    }
}
