package com.iyuba.CET4bible.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iyuba.CET4bible.R;
import com.iyuba.CET4bible.listening.ListenCommentFragment;
import com.iyuba.CET4bible.listening.ListenOriginalFragment;
import com.iyuba.CET4bible.listening.ListenTestFragment;
import com.iyuba.CET4bible.listening.RankFragment;
import com.iyuba.CET4bible.listening.ReadEvaluateFragment;

public class ListenFragmentAdapter extends FragmentStatePagerAdapter {
    protected static String[] CONTENT;
    private Context mContext;
    private String section;
    private String examYear ;

    public ListenFragmentAdapter(Context context, FragmentManager fm, String section ,String examYear) {
        super(fm);
        mContext = context;
        CONTENT = mContext.getResources().getStringArray(R.array.listen_title);
        this.section = section;
        this.examYear = examYear;
    }

    @Override
    public int getItemPosition(Object object) {
        super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new ListenOriginalFragment();
                break;
            case 2:
                fragment = new ReadEvaluateFragment();
                break;
            case 0:
                fragment = new ListenTestFragment();
                Bundle bundle = new Bundle();
                bundle.putString("section", section);
                bundle.putString("examtime", examYear);
                fragment.setArguments(bundle);
                break;
//            case 3:
//                fragment = new ListenCommentFragment();
//                break;
            case 3:
                fragment = new RankFragment();
                break;
            default:
                fragment = new ListenTestFragment();
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
        return CONTENT[position];
    }

}

