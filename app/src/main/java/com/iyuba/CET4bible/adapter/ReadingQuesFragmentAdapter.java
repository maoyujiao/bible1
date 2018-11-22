package com.iyuba.CET4bible.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iyuba.CET4bible.fragment.ReadingQuesFragment;
import com.iyuba.CET4bible.sqlite.mode.ReadingAnswer;
import com.iyuba.CET4bible.sqlite.mode.ReadingExplain;

import java.util.List;

public class ReadingQuesFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<ReadingAnswer> readingAnswers;
    private List<ReadingExplain> readingExplains;

    public ReadingQuesFragmentAdapter(Context context, FragmentManager fm,
                                      List<ReadingAnswer> readingAnswers, List<ReadingExplain> readingExplains) {
        super(fm);
        this.readingAnswers = readingAnswers;
        this.readingExplains = readingExplains;
    }

    @Override
    public Fragment getItem(int arg0) {

        return ReadingQuesFragment.newInstance(mContext, arg0, readingAnswers, readingExplains);
    }

    @Override
    public int getCount() {

        return readingAnswers.size();
    }

}
