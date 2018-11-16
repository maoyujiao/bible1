package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.app.GoldApp;
import com.iyuba.trainingcamp.bean.AbilityQuestion;
import com.iyuba.trainingcamp.utils.FilePath;
import com.iyuba.trainingcamp.utils.FileUtils;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.adapter
 * @class describe
 * @time 2018/8/7 17:00
 * @change
 * @chang time
 * @class describe
 */
public class ExamQuestionAdapter extends RecyclerView.Adapter<ExamQuestionAdapter.ViewHolder>{

    List<AbilityQuestion.TestListBean> list;
    Context mContext;
    int expandPosition;

    public ExamQuestionAdapter(Context context ,List<AbilityQuestion.TestListBean> list){
        this.list = list;
        mContext = context;
    }
    @NonNull
    @Override
    public ExamQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trainingcamp_exam_result_item,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExamQuestionAdapter.ViewHolder holder, final int position) {
        holder.en.setText(list.get(position).getQuestion());
        if (position == expandPosition) {
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setText("");
            List <String> explains = getExplains(position);
            for (String explain :explains){
                holder.more.append(explain);
            }
        } else {
            holder.more.setVisibility(View.GONE);
        }
        if (list.get(position).getResult().equals("1")){
            holder.result.setImageResource(R.drawable.trainingcamp_icon_true);
        }else {
            holder.result.setImageResource(R.drawable.trainingcamp_icon_false);
        }
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == expandPosition) {
                    expandPosition = -1;
                    notifyDataSetChanged();
                    return;
                }
                expandPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView en;
        ImageView result;
        TextView seeMore;
        TextView more;
        public ViewHolder(View itemView) {
            super(itemView);

            en = itemView.findViewById(R.id.en);
            result = itemView.findViewById(R.id.result);
            seeMore = itemView.findViewById(R.id.see_more);
            more = itemView.findViewById(R.id.more);
        }
    }
    private List<String> getExplains(int position){
        List<String> s = FileUtils.ReadTxtFile(FilePath.getTxtPath()+ list.get(position).Explains);
        return s;
    }
}
