package com.iyuba.CET4bible.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.CET4bible.R;

import java.util.List;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.CET4bible.activity
 * @class describe
 * @time 2018/6/13 10:18
 * @change
 * @chang time
 * @class describe
 */
public class JpBlogAdapter extends RecyclerView.Adapter<JpBlogAdapter.MyHolder> {

    List<String> list ;

    public void setList(List list){
        this.list = list;
    }


    public enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_abilitymap,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(JpBlogAdapter.MyHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position < 1 ? ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);

        }
    }
}
