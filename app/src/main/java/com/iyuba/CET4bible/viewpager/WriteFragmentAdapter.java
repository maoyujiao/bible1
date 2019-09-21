package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.write.WriteCommentFragment;
import com.iyuba.CET4bible.write.WriteExampleFragment;
import com.iyuba.CET4bible.write.WriteQuestionFragment;

public class WriteFragmentAdapter extends FragmentPagerAdapter {
    protected static String[] CONTENT;
    private Context mContext;

    public WriteFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        CONTENT = mContext.getResources().getStringArray(R.array.write_title);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new WriteQuestionFragment();
                break;
            case 1:
                fragment = new WriteExampleFragment();
                break;
            case 2:
                fragment = new WriteCommentFragment();
                break;
            default:
                fragment = new WriteQuestionFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    String[] title = {"试题", "范文", "点评"};
}
