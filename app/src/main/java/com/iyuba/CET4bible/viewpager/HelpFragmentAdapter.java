package com.iyuba.CET4bible.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.BuildConfig;
import com.iyuba.CET4bible.R;

public class HelpFragmentAdapter extends FragmentPagerAdapter {


    protected static int[] CONTENT = new int[]{R.drawable.help1,
            R.drawable.help2, R.drawable.help3, R.drawable.help4};

    public HelpFragmentAdapter(FragmentManager fm) {
        super(fm);
        initContents();
    }

    private void initContents() {
        CONTENT = new int[]{R.drawable.help1,
                R.drawable.help2, R.drawable.help3, R.drawable.help4};

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
