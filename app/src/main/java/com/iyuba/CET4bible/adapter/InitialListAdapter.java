/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class InitialListAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mList;
    private int selectPosition = -1;

    public InitialListAdapter(Context context) {
        mContext = context;
        String[] jp = {
                "あ", "い", "う", "え", "お",
                "か", "き", "く", "け", "こ",
                "さ", "し", "す", "せ", "そ",
                "た", "ち", "つ", "て", "と",
                "な", "に", "ぬ", "ね", "の",
                "は", "ひ", "ふ", "へ", "ほ",
                "ま", "み", "む", "め", "も",
                "や", "ゆ", "よ", "ら", "り",
                "る", "れ", "ろ", "わ", "を",

                "ア", "イ", "ウ", "エ", "オ",
                "カ", "キ", "ク", "ケ", "コ",
                "サ", "シ", "ス", "セ", "ソ",
                "タ", "チ", "ツ", "テ", "ト",
                "ナ", "ニ", "ヌ", "ネ", "ノ",
                "ハ", "ヒ", "フ", "ヘ", "ホ",
                "マ", "ミ", "ム", "メ", "モ",
                "ヤ", "ユ", "ヨ", "ラ", "リ",
                "ル", "レ", "ロ", "ワ", "ヲ"
        };
        mList = BuildConfig.isEnglish ? mContext.getResources().getStringArray(R.array.initial) : jp;
    }

    public void setSelected(String temp) {
        for (int i = 0; i < mList.length; i++) {
            if (mList[i].equals(temp)) {
                this.selectPosition = i;
                break;
            }
        }
    }

    @Override
    public int getCount() {

        return mList.length;
    }

    @Override
    public Object getItem(int arg0) {

        return mList[arg0];
    }

    @Override
    public long getItemId(int arg0) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder curViewHolder;
        if (convertView == null) {
            curViewHolder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_initial, null);
            curViewHolder.content = convertView
                    .findViewById(R.id.content);
            curViewHolder.radio = convertView
                    .findViewById(R.id.initial_radio);
            convertView.setTag(curViewHolder);
        } else {
            curViewHolder = (ViewHolder) convertView.getTag();
        }
        curViewHolder.content.setText(mList[position]);
        if (selectPosition == position) {
            curViewHolder.radio.setChecked(true);
        } else {
            curViewHolder.radio.setChecked(false);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView content;
        RadioButton radio;
    }

}
