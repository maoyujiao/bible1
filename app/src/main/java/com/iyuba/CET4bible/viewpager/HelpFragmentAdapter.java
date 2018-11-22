package com.iyuba.CET4bible.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.R;

public class HelpFragmentAdapter extends FragmentPagerAdapter {
    protected static final int[] CONTENT = new int[]{R.raw.help1,
            R.raw.help2, R.raw.help3};

    public HelpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HelpFragment.newInstance(CONTENT[position]);
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }
}
