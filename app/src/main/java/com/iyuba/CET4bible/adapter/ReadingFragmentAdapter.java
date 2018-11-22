package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.fragment.ReadingFragment;

public class ReadingFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public ReadingFragmentAdapter(Context mContext, FragmentManager fm) {
        super(fm);
        this.mContext = mContext;

    }

    @Override
    public Fragment getItem(int position) {
        return ReadingFragment.newInstance(mContext, position);

    }

    @Override
    public int getCount() {

        return 2;
    }

}
