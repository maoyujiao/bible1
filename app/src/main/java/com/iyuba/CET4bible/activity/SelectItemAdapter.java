package com.iyuba.CET4bible.activity;

import android.content.Context;
import android.graphics.Paint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.CET4bible.R;

import java.util.List;

/**
 * SelectItemAdapter
 *
 * @author wayne
 * @date 2017/12/20
 */
public class SelectItemAdapter extends BaseAdapter {
    Context context;
    List<String> data;
    String word;
    SparseBooleanArray selectArray;

    public SelectItemAdapter(Context context, List<String> data, String word, SparseBooleanArray selectArray) {
        this.context = context;
        this.data = data;
        this.word = word;
        this.selectArray = selectArray;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_word, parent, false);
            holder = new Holder();
            holder.textView = convertView.findViewById(R.id.text);
            holder.clear = convertView.findViewById(R.id.clear);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textView.setText(data.get(position));

        holder.clear.setVisibility(View.GONE);
        if (data.get(position).equals(word)) {
            holder.clear.setVisibility(View.VISIBLE);
            holder.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clearOnClickListener != null) {
                        v.setTag(data.get(position));
                        clearOnClickListener.onClick(v);
                    }
                }
            });
        }

        if (selectArray.get(position, false)) {
            holder.textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.textView.setTextColor(context.getResources().getColor(R.color.darkgray));

            convertView.setOnClickListener(null);
        } else {
            holder.textView.getPaint().setFlags(0);
            holder.textView.setTextColor(context.getResources().getColor(R.color.black));

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(null, finalConvertView, position, position);
                    }
                }
            });
        }
        return convertView;
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class Holder {
        TextView textView;
        View clear;
    }

    private View.OnClickListener clearOnClickListener;

    public void setClearOnClickListener(View.OnClickListener clearOnClickListener) {
        this.clearOnClickListener = clearOnClickListener;
    }


}
