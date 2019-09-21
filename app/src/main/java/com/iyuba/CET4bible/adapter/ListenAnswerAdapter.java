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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.manager.ListenDataManager;
import com.iyuba.core.listener.ResultIntCallBack;
import com.iyuba.core.sqlite.mode.test.CetAnswer;

import java.util.ArrayList;

/**
 * 四级选择题答案适配器
 *
 * @author 陈彤
 * @version 1.0
 */
public class ListenAnswerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CetAnswer> answers;
    private ResultIntCallBack callBack;
    private boolean finish;

    public ListenAnswerAdapter(Context context, boolean finish,
                               ResultIntCallBack callBack) {
        mContext = context;
        answers = ListenDataManager.Instance().answerList;
        this.finish = finish;
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        return answers.size();
    }

    @Override
    public Object getItem(int arg0) {
        return answers.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CetAnswer cetAnswer = answers.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_answer, null);
            viewHolder = new ViewHolder();
            viewHolder.number = convertView
                    .findViewById(R.id.number);
            viewHolder.your = convertView
                    .findViewById(R.id.your_answer);
            viewHolder.right = convertView
                    .findViewById(R.id.right_answer);
            viewHolder.oper = convertView.findViewById(R.id.oper);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText(cetAnswer.id);
        viewHolder.your.setText(cetAnswer.yourAnswer);
        if (finish) {
            viewHolder.right.setText(cetAnswer.rightAnswer);
            if (cetAnswer.yourAnswer.equals(cetAnswer.rightAnswer)) {
                viewHolder.your.setBackgroundResource(R.drawable.item_select);
            } else {
                viewHolder.your.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.app_color));
                if (cetAnswer.yourAnswer.equals("")) {
                    viewHolder.your.setText(R.string.no_answer);
                }
            }
        } else {
            viewHolder.right.setText(R.string.secret);
            if (cetAnswer.yourAnswer.equals("")) {
                viewHolder.your.setText(R.string.no_answer);
            }
        }

        final int pos = position;
        viewHolder.oper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callBack.setResult(pos); // 由于没有知识点fragment  所以要-1
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView your;
        TextView right;
        TextView number;
        TextView oper;
    }

}
