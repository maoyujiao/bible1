package com.iyuba.abilitytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iyuba.abilitytest.R;
import com.iyuba.abilitytest.entity.AbilityResult;

import java.util.ArrayList;

/**
 * @author LiuZhenli on 2016/12/13 09:38 Email: 848808263@qq.com
 * @version 1.0.0
 */

public class AbilityTestResultAdapter extends BaseAdapter {
    class Score {
        String type;
        int total;
        int right;

    }

    private Context mcontext = null;
    private AbilityResult ar = null;
    private ArrayList<Score> scoreList;
    private int itemCount;

    public AbilityTestResultAdapter(Context context, AbilityResult ar, int itemCount) {
        this.mcontext = context;
        this.ar = ar;
        this.itemCount = itemCount;
        scoreList = getScoreList(ar);
    }

    private ArrayList<Score> getScoreList(AbilityResult ar) {
        ArrayList<Score> scores = new ArrayList<>();
        switch (itemCount) {
            case 10:
                if (!ar.Score10.equals("-1"))
                    scores.add(getScore(ar.Score10));
            case 9:
                if (!ar.Score9.equals("-1"))
                scores.add(getScore(ar.Score9));
            case 8:
                if (!ar.Score8.equals("-1"))
                scores.add(getScore(ar.Score8));
            case 7:
                if (!ar.Score7.equals("-1"))
                scores.add(getScore(ar.Score7));
            case 6:
                if (!ar.Score6.equals("-1"))
                scores.add(getScore(ar.Score6));
            case 5:
                if (!ar.Score5.equals("-1"))
                scores.add(getScore(ar.Score5));
            case 4:
                if (!ar.Score4.equals("-1"))
                scores.add(getScore(ar.Score4));
            case 3:
                if (!ar.Score3.equals("-1"))
                scores.add(getScore(ar.Score3));
            case 2:
                if (!ar.Score2.equals("-1"))
                scores.add(getScore(ar.Score2));
            case 1:
                if (!ar.Score1.equals("-1"))
                scores.add(getScore(ar.Score1));
                break;
        }
        return scores;
    }

    private Score getScore(String str) {
        Score score = new Score();
        score.total = Integer.parseInt(str.split("\\+\\+")[0]);
        score.right = Integer.parseInt(str.split("\\+\\+")[1]);
        score.type = str.split("\\+\\+")[2];
        return score;
    }

    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder viewHolder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_ability_result, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_ability_result_name = convertView.findViewById(R.id.tv_ability_result_name);
            viewHolder.pb_ability_result = convertView.findViewById(R.id.pb_ability_result);
            viewHolder.tv_ability_result_score = convertView.findViewById(R.id.tv_ability_result_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_ability_result_name.setText(scoreList.get(position).type);
        if (scoreList.get(position).total == 0) {
            viewHolder.pb_ability_result.setProgress(0);
            viewHolder.tv_ability_result_score.setText("0");
        } else {
            viewHolder.pb_ability_result.setProgress(scoreList.get(position).right * 100 / scoreList.get(position).total);
            viewHolder.tv_ability_result_score.setText(scoreList.get(position).right * 100 / scoreList.get(position).total + "");
        }
//        viewHolder.pb_ability_result.setProgress(scoreList.get(position).right * 100 / scoreList.get(position).total);
//        viewHolder.tv_ability_result_score.setText(scoreList.get(position).right * 100 / scoreList.get(position).total + "");
        return convertView;
    }

    class ViewHolder {
        TextView tv_ability_result_name;
        ProgressBar pb_ability_result;
        TextView tv_ability_result_score;

    }
}
