package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iyuba.core.manager.DataManager;

public class TestTpyeFragmentAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    public TestTpyeFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ListeningFragment();
        } else if (position == 1) {
            // 翻译
            Fragment fragment = new WriteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", position);
            fragment.setArguments(bundle);
            return fragment;
        } else if (position == 2) {
            // 写作
            Fragment fragment = new WriteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", position);
            fragment.setArguments(bundle);
            return fragment;
        } else if (position == 3) {
            return new FillInBlankFragment();
        } else if (position == 4) {
            return new ParagraphMatchingFragment();
        } else if (position == 5) {
            return new ReadingFragment();
        } else {
            return TestFragment.newInstance(mContext, position);
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    String[] title = {"听力", "翻译", "写作", "选词填空", "段落匹配", "仔细阅读"};

}
