package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.ListenCBlankFragment;
import com.iyuba.CET4bible.listening.ListenCOriginalFragment;
import com.iyuba.CET4bible.listening.ListenCTestFragment;

public class ListenCFragmentAdapter extends FragmentStatePagerAdapter {
    protected static String[] CONTENT;
    private Context mContext;
    private int time;
    private boolean submit;

    public ListenCFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        CONTENT = mContext.getResources()
                .getStringArray(R.array.listen_c_title);
    }

    @Override
    public int getItemPosition(Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }

    public void setTimes(int args) {
        time = args;
    }

    public void setSubmit(boolean args) {
        submit = args;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new ListenCOriginalFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("time", time);
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new ListenCTestFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("submit", submit);
                fragment.setArguments(bundle1);
                break;
            case 2:
                fragment = new ListenCBlankFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("submit", submit);
                fragment.setArguments(bundle2);
                break;
            default:
                fragment = new ListenCTestFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }
}
