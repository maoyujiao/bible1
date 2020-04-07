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
import com.iyuba.core.me.pay.ViewHolder;
import com.iyuba.core.me.sqlite.mode.ListenRankUser;
import com.iyuba.core.thread.GitHubImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;

/**
 * Created by 15730 on 2018/4/17.
 */

public class ListenRankListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ListenRankUser> rankUserList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Pattern p;
    private Matcher m;

    public ListenRankListAdapter(Context mContext, List<ListenRankUser> rankUserList) {
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

    public void resetList(List<ListenRankUser> list) {
        rankUserList.clear();
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        rankUserList.clear();
        notifyDataSetChanged();
    }

    public void addList(List<ListenRankUser> list) {
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rank_info_item, null);
        }
        ListenRankUser ru = rankUserList.get(position);
        Log.e("LearnRankListAdapter", String.valueOf(rankUserList.size()));
        String firstChar;

        ImageView rankLogoImage = ViewHolder.get(convertView, R.id.rank_logo_image);
        TextView rankLogoText = ViewHolder.get(convertView, R.id.rank_logo_text);
        ImageView userImage = ViewHolder.get(convertView, R.id.user_image);
        TextView userImageText = ViewHolder.get(convertView, R.id.user_image_text);
        TextView userName = ViewHolder.get(convertView, R.id.rank_user_name);
        TextView userInfo = ViewHolder.get(convertView, R.id.rank_user_info);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid  = rankUserList.get(position).getUid();
                String name  = rankUserList.get(position).getName();
                mContext.startActivity(PersonalHomeActivity.buildIntent(mContext,
                        uid,
                        name, 0));
            }
        });
        userImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid  = rankUserList.get(position).getUid();
                String name  = rankUserList.get(position).getName();
                mContext.startActivity(PersonalHomeActivity.buildIntent(mContext,
                        uid,
                        name, 0));
            }
        });

        firstChar = getFirstChar(ru.getName());
        switch (ru.getRanking()) {
            case 1:
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
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                    userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                }
                break;
            case 2:
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
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                    userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                }
                break;
            case 3:
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
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                    userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                }
                break;
            default:
                rankLogoImage.setVisibility(View.INVISIBLE);
                rankLogoText.setVisibility(View.VISIBLE);
                rankLogoText.setText(ru.getRanking()+"");

                if (ru.getImgSrc().equals("http://static1.iyuba.cn/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                        userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName() == ""?ru.getUid()+"":ru.getName());
                    userInfo.setText("文章总数" + ru.getTotalEssay() + "\n单词总数:" + ru.getTotalWord());
                }
                break;

        }
        return convertView;
    }

    private <T extends View> T findview(View convertView, int id) {
        return (T) convertView.findViewById(id);
    }

    private String getFirstChar(String name) {
        if (name == null ){
            return "A";
        }
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
