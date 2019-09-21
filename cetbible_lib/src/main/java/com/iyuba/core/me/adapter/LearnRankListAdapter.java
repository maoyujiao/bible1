package com.iyuba.core.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.biblelib.R;
import com.iyuba.core.manager.SocialDataManager;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.me.sqlite.mode.LearnRankUser;
import com.iyuba.core.thread.GitHubImageLoader;
import com.iyuba.core.util.StudyTimeTransformUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/4.
 */

public class LearnRankListAdapter extends BaseAdapter {

    private Context mContext;
    private List<LearnRankUser> rankUserList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Pattern p;
    private Matcher m;

    public LearnRankListAdapter(Context mContext, List<LearnRankUser> rankUserList) {
        this.mContext = mContext;
        this.rankUserList.addAll(rankUserList);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return rankUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(rankUserList.get(position).getSort());
    }

    public void resetList(List<LearnRankUser> list) {
        rankUserList.clear();
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        rankUserList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<LearnRankUser> list) {
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rank_learn_info_item, null);
        }
        LearnRankUser ru = rankUserList.get(position);
        Log.e("LearnRankListAdapter", String.valueOf(rankUserList.size()));
        String firstChar;

        ImageView rankLogoImage = findview(convertView, R.id.rank_logo_image);
        TextView rankLogoText = findview(convertView, R.id.rank_logo_text);
        ImageView userImage = findview(convertView, R.id.user_image);
        TextView userImageText = findview(convertView, R.id.user_image_text);
        TextView userName = findview(convertView, R.id.rank_user_name);
        TextView myRankTime = findview(convertView, R.id.tv_learn_ranking_total_time);
        TextView myRankEssay = findview(convertView, R.id.tv_learn_ranking_total_essay);
        TextView myRankWord = findview(convertView, R.id.tv_learn_ranking_total_words);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid  = rankUserList.get(position).getUid();
                Intent intent = new Intent();
                SocialDataManager.Instance().userid = uid;
                intent.setClass(mContext, PersonalHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mContext.startActivity(intent);
            }
        });
        userImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid  = rankUserList.get(position).getUid();
                Intent intent = new Intent();
                SocialDataManager.Instance().userid = uid;
                intent.setClass(mContext, PersonalHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mContext.startActivity(intent);
            }
        });
        firstChar = getFirstChar(ru.getName());
        switch (ru.getRanking()) {
            case "1":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_gold);

                if (ru.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                    myRankEssay.setText(ru.getTotalEssay());
                    myRankWord.setText(ru.getTotalWord());
                }
                break;
            case "2":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_silvery);

                if (ru.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                    myRankEssay.setText(ru.getTotalEssay());
                    myRankWord.setText(ru.getTotalWord());
                }
                break;
            case "3":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_copper);

                if (ru.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                    myRankEssay.setText(ru.getTotalEssay());
                    myRankWord.setText(ru.getTotalWord());
                }
                break;
            default:
                rankLogoImage.setVisibility(View.INVISIBLE);
                rankLogoText.setVisibility(View.VISIBLE);
                rankLogoText.setText(ru.getRanking());

                if (ru.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                        myRankEssay.setText(ru.getTotalEssay());
                        myRankWord.setText(ru.getTotalWord());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    myRankTime.setText(StudyTimeTransformUtil.getFormat(ru.getTotalTime()));
                    myRankEssay.setText(ru.getTotalEssay());
                    myRankWord.setText(ru.getTotalWord());
                }
                break;

        }
        return convertView;
    }

    private <T extends View> T findview(View convertView, int id) {
        return (T) convertView.findViewById(id);
    }

    private String getFirstChar(String name) {
        String subString;
        for (int i = 0; i < name.length(); i++) {
            subString = name.substring(i, i + 1);

            p = Pattern.compile("[0-9]*");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
                return subString;
            }
        }

        return "A";
    }
}
