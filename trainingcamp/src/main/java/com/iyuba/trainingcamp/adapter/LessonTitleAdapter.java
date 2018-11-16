package com.iyuba.trainingcamp.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.LessonIdBean;
import com.iyuba.trainingcamp.db.DailyWordDBHelper;
import com.iyuba.trainingcamp.event.LessonSelectEvent;
import com.iyuba.trainingcamp.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.trainingcamp.adapter
 * @class describe
 * @time 2018/11/5 15:51
 * @change
 * @chang time
 * @class describe
 */
public class LessonTitleAdapter extends RecyclerView.Adapter<LessonTitleAdapter.ViewHolder> {


    DailyWordDBHelper mHelper;

    private Context mContext;
    private List<LessonIdBean.LessonListBean> list;
    private Fragment mFragment;

    private int lesson = 0 ;
    public LessonTitleAdapter(List<LessonIdBean.LessonListBean> list , PopupWindow window) {
        this.list = list ;
    }

    @NonNull
    @Override
    public LessonTitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainingcamp_lessontitle,parent,false);
        ViewHolder holder = new ViewHolder(view);
        mHelper = new DailyWordDBHelper(parent.getContext());
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final LessonTitleAdapter.ViewHolder holder, final int position) {
        if (GoldApp.getApp(mContext).LessonType.contains("cet")){
            holder.lessonTitle.setText("Lesson"+(position+1));

        }else {
            holder.lessonTitle.setText(mHelper.getVoaInfo(list.get(position*3).getLessonid()).getTitle_cn());

        }
        String date = mHelper.selectDataById(GoldApp.getApp(mContext).getUserId(),
                list.get(position*3).getLessonid()).getDate();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (format.parse(TimeUtils.getCurTime()).getTime()>=
                    format.parse(date).getTime()){
                holder.lessonTitle.setTextColor(mContext.getResources().getColor(android.R.color.black));
                holder.lock.setVisibility(View.INVISIBLE);
            }else {
                holder.lessonTitle.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
                holder.lock.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.lessonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LessonSelectEvent(position));
//                iSelect.selectLesson(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size()/3;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView lessonIndex ;
        TextView lessonTitle ;
        ImageView lock ;
        public ViewHolder(View itemView) {
            super(itemView);
            lessonTitle = itemView.findViewById(R.id.lesson_title);
            lock = itemView.findViewById(R.id.lock);
        }
    }

}
