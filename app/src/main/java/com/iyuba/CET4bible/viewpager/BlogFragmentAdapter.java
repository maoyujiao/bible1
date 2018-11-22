package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.R;

public class BlogFragmentAdapter extends FragmentPagerAdapter {
    protected static String[] CONTENT;
    private Context mContext;

    public BlogFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        CONTENT = mContext.getResources().getStringArray(R.array.blog_title);
    }

    @Override
    public Fragment getItem(int position) {
        return BlogFragment.newInstance(mContext, position);
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }
}
