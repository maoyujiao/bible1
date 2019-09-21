package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.configation.Constant;
import com.iyuba.core.sqlite.mode.Word;
import com.iyuba.core.util.TextAttr;
import com.iyuba.core.widget.Player;

import java.util.ArrayList;

/**
 * 单词列表适配器
 *
 * @author 陈彤
 * @version 1.0
 */

public class WordListAdapter extends BaseAdapter {
    public boolean modeDelete = false;
    public ViewHolder viewHolder;
    private Context mContext;
    private ArrayList<Word> mList = new ArrayList<Word>();

    public WordListAdapter(Context context) {
        mContext = context;
    }

    public WordListAdapter(Context context, ArrayList<Word> list) {
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

    public void setData(ArrayList<Word> list) {
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Word word = mList.get(position);
        final int pos = position;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_word, null);
            viewHolder = new ViewHolder();
            viewHolder.key = convertView.findViewById(R.id.word_key);
            viewHolder.pron = convertView
                    .findViewById(R.id.word_pron);
            viewHolder.key.setTextColor(Color.BLACK);
            viewHolder.def = convertView.findViewById(R.id.word_def);

            viewHolder.deleteBox = convertView
                    .findViewById(R.id.checkBox_isDelete);
            viewHolder.speaker = convertView
                    .findViewById(R.id.word_speaker);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (modeDelete) {
            viewHolder.deleteBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteBox.setVisibility(View.GONE);
        }
        if (mList.get(pos).isDelete) {
            viewHolder.deleteBox.setImageResource(R.drawable.check_box_checked);
        } else {
            viewHolder.deleteBox.setImageResource(R.drawable.check_box);
        }
        viewHolder.key.setText(word.key);
        if (word.pron != null) {
            StringBuffer sb = new StringBuffer();
            sb.append('[').append(word.pron).append(']');
            viewHolder.pron.setText(TextAttr.decode(sb.toString()));
        }
        viewHolder.def.setText(word.def.replaceAll("\n", ""));
        if (Constant.APP_CONSTANT.isEnglish()){
            if (word.audioUrl != null && word.audioUrl.length() != 0) {
                viewHolder.speaker.setVisibility(View.VISIBLE);
            } else {
                viewHolder.speaker.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.speaker.setVisibility(View.GONE);
        }
        viewHolder.speaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Player player = new Player(mContext, null);
                String url = word.audioUrl;
                player.playUrl(url);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView def;
        TextView key, pron;
        ImageView deleteBox;
        ImageView speaker;
    }

}
