package com.iyuba.trainingcamp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.bean.LearningContent;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.adapter
 * @class describe
 * @time 2018/7/17 16:16
 * @change
 * @chang time
 * @class describe
 */
public class WordLearnAdapter extends RecyclerView.Adapter<WordLearnAdapter.ViewHolder> {

    /** ITEM_TYPE 单词item
     *  BLANK_TYPE 空的item
     */
    public static enum ITEM_TYPE {
        ITEM_TYPE,
        BLANK_TYPE
    }


    Context context;
    private List<LearningContent> mLearningContents;
    private int expandPosition = -1;
    private RecyclerView recyclerView;

    public WordLearnAdapter(Context context, List<LearningContent> learningContents) {
        this.context = context;
        this.mLearningContents = learningContents;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }


    @Override
    public int getItemViewType(int position) {
        return position  == 1 ? ITEM_TYPE.BLANK_TYPE.ordinal() : ITEM_TYPE.ITEM_TYPE.ordinal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_TYPE.ITEM_TYPE.ordinal()) {
            view = LayoutInflater.from(context).inflate(R.layout.trainingcamp_word_list_item, null);
        } else if (viewType == ITEM_TYPE.BLANK_TYPE.ordinal()) {
            view = LayoutInflater.from(context).inflate(R.layout.trainingcamp_word_list_total, null);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (position == mLearningContents.size()){
            return;
        }
        holder.WordEN.setText(mLearningContents.get(position).en);
        mLearningContents.get(position).cn.replace("v", "\nv");
        holder.WordCN.setText(mLearningContents.get(position).cn);
        holder.mPro.setText(mLearningContents.get(position).pro);
        holder.mWord_content.setText(mLearningContents.get(position).en);
        holder.mCn.setText(mLearningContents.get(position).cn);
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < mLearningContents.get(position).phrases.size(); i++) {
            buffer.append(mLearningContents.get(position).phrases.get(i));
        }
        holder.mPhrase.setText(buffer.toString());
        if (position == expandPosition) {
            holder.ll.setVisibility(View.VISIBLE);
        } else {
            holder.ll.setVisibility(View.GONE);
        }

        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == expandPosition) {
                    expandPosition = -1;
                    notifyDataSetChanged();
                    return;
                }
                LearningContent learningContent = mLearningContents.get(position);
                mLearningContents.remove(position);
                mLearningContents.add(0, learningContent);
                expandPosition = 0;
                notifyDataSetChanged();
//                recyclerView.scrollToPosition(0);

                ((LinearLayoutManager)recyclerView.getLayoutManager())
                        .scrollToPositionWithOffset(0, 0);
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLearningContents.size() + 1;
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

        public ViewHolder(View view) {
            super(view);
            WordEN = (TextView) view.findViewById(R.id.word);
            WordCN = (TextView) view.findViewById(R.id.content);
            rr = (RelativeLayout) view.findViewById(R.id.rr);
            ll = view.findViewById(R.id.expand);
            mWord_content = (TextView) view.findViewById(R.id.word_content);
            mPro = (TextView) view.findViewById(R.id.pro);
            mCn = (TextView) view.findViewById(R.id.cn);
            mPhrase = (TextView) view.findViewById(R.id.phrase);
        }
    }

    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }
}
