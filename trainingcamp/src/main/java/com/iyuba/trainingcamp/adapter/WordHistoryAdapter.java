package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.WordHistory;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.adapter
 * @class describe
 * @time 2018/10/11 18:15
 * @change
 * @chang time
 * @class describe
 */
public class WordHistoryAdapter extends RecyclerView.Adapter<WordHistoryAdapter.ViewHolder> {


    Context context;
    private List<WordHistory> mLearningContents;
    private int expandPosition = -1;
    private RecyclerView recyclerView;
    private boolean showIcon = true;


    public WordHistoryAdapter(Context context, List<WordHistory> learningContents) {
        this.context = context;
        this.mLearningContents = learningContents;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


//    @Override
//    public int getItemViewType(int position) {
//        return position - mLearningContents.size() == 0 ? WordListAdapter.ITEM_TYPE.BLANK_TYPE.ordinal() : WordListAdapter.ITEM_TYPE.ITEM_TYPE.ordinal();
//    }

    @NonNull
    @Override
    public WordHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.trainingcamp_word_list_item, parent,false);

        WordHistoryAdapter.ViewHolder holder = new WordHistoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WordHistoryAdapter.ViewHolder holder, final int position) {
        if (position == mLearningContents.size()) {
            return;
        }
        holder.WordEN.setText(mLearningContents.get(position).getEn());
        holder.WordCN.setText(mLearningContents.get(position).getCn());
        if ("true".equals(mLearningContents.get(position).isPassed())){
            holder.mResult.setImageResource(R.drawable.trainingcamp_icon_true);
        }else {
            holder.mResult.setImageResource(R.drawable.trainingcamp_icon_false);
        }

//
//        holder.rr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (true) {
//                    return;
//                }
//                if (position == expandPosition) {
//                    expandPosition = -1;
//                    notifyDataSetChanged();
//                    return;
//                }
//                LearningContent learningContent = mLearningContents.get(position);
//                mLearningContents.remove(position);
//                mLearningContents.add(0, learningContent);
//                expandPosition = 0;
//                notifyDataSetChanged();
////                recyclerView.scrollToPosition(0);
//
//                ((LinearLayoutManager) recyclerView.getLayoutManager())
//                        .scrollToPositionWithOffset(0, 0);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mLearningContents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView WordEN;
        TextView WordCN;
        RelativeLayout rr;
        LinearLayout ll;
        TextView mWord_content;
        TextView mPro;
        TextView mCn;
        TextView mPhrase;
        ImageView mResult;

        public ViewHolder(View view) {
            super(view);
            WordEN = (TextView) view.findViewById(R.id.word);
            WordCN = (TextView) view.findViewById(R.id.content);
            rr = (RelativeLayout) view.findViewById(R.id.rr);
            ll = view.findViewById(R.id.expand);
            mResult = view.findViewById(R.id.result);
            mWord_content = (TextView) view.findViewById(R.id.word_content);
            mPro = (TextView) view.findViewById(R.id.pro);
            mCn = (TextView) view.findViewById(R.id.cn);
            mPhrase = (TextView) view.findViewById(R.id.phrase);
        }
    }

}
