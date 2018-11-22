/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.WordDataManager;
import com.iyuba.CET4bible.sqlite.mode.Cet4RootWord;
import com.iyuba.CET4bible.sqlite.mode.Cet4Word;
import com.iyuba.core.util.TextAttr;

import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Cet4WordListAdapter extends BaseAdapter {
    private final static int[] colorful = new int[]{0xffe14438, 0xff826cab,
            0xff62aa46, 0xffe38f2b, 0xff4fbdf0};
    private Context mContext;
    private ArrayList<Cet4Word> mList;
    private ArrayList<Cet4RootWord> mRootList;

    public Cet4WordListAdapter(Context context) {
        mContext = context;
    }

    public void setList(ArrayList<Cet4Word> list) {
        mList = list;

    }

    public void setRootList(ArrayList<Cet4RootWord> list) {
        mRootList = list;
    }


    @Override
    public int getCount() {

        if (WordDataManager.Instance().cate.equals("2")) {
            return mRootList.size();
        } else {
            return mList.size();
        }

    }

    @Override
    public Object getItem(int arg0) {

        if (WordDataManager.Instance().cate.equals("2")) {
            return mRootList.get(arg0);
        } else {
            return mList.get(arg0);
        }
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (WordDataManager.Instance().cate.equals("2")) {
            final ViewHolder curViewHolder;
            final Cet4RootWord word = mRootList.get(position);
            if (convertView == null) {
                curViewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_cet_word, null);
                curViewHolder.word = convertView.findViewById(R.id.word);
                curViewHolder.desc = convertView.findViewById(R.id.desc);
                convertView.setTag(curViewHolder);
            } else {
                curViewHolder = (ViewHolder) convertView.getTag();
            }
            curViewHolder.word.setText("词根" + word.groupflg);
            curViewHolder.word.setTextColor(Color.parseColor("#808080"));
            curViewHolder.desc.setText(word.grouptitle);
            curViewHolder.desc.setTextColor(Color.parseColor("#000000"));
            return convertView;
        } else {
            final ViewHolder curViewHolder;
            final Cet4Word word = mList.get(position);
            if (convertView == null) {
                curViewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_cet_word, null);
                curViewHolder.word = convertView.findViewById(R.id.word);
                curViewHolder.pron = convertView.findViewById(R.id.pron);
                curViewHolder.desc = convertView.findViewById(R.id.desc);
//			curViewHolder.append = (ImageView) convertView
//					.findViewById(R.id.append);
//			curViewHolder.star1 = (ImageView) convertView
//					.findViewById(R.id.star1);
//			curViewHolder.star3 = (ImageView) convertView
//					.findViewById(R.id.star3);
//			curViewHolder.star2 = (ImageView) convertView
//					.findViewById(R.id.star2);

                convertView.setTag(curViewHolder);
            } else {
                curViewHolder = (ViewHolder) convertView.getTag();
            }
            curViewHolder.word.setText(word.word);
            StringBuffer sb = new StringBuffer();
            sb.append('[').append(word.pron).append(']');
            curViewHolder.pron.setText(TextAttr.decode(sb.toString()));
            curViewHolder.desc.setText(word.def);
//		curViewHolder.append.setBackgroundColor(colorful[position
//				% colorful.length]);
            int star = Integer.parseInt(word.star);
//		if (star > 19) {
//			curViewHolder.star3.setBackgroundResource(R.drawable.star);
//		}
//		if (star > 15) {
//			curViewHolder.star2.setBackgroundResource(R.drawable.star);
//		}
//		if (star > 11) {
//			curViewHolder.star1.setBackgroundResource(R.drawable.star);
//		}
            return convertView;
        }

    }


    public class ViewHolder {
        TextView word, pron;
        TextView desc;
//		ImageView append, star1, star2, star3;
    }

}
