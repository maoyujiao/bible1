package com.iyuba.core.me.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.iyuba.core.me.fragment.LearnRankFragment;
import com.iyuba.core.me.fragment.ListenRankFragment;
import com.iyuba.core.me.fragment.ReadRankFragment;
import com.iyuba.core.me.fragment.TestRankFragment;

/**
 * 作者：renzhy on 17/1/5 15:15
 * 邮箱：renzhongyigoo@gmail.com
 */
public class RankingPagerAdapter extends FragmentStatePagerAdapter {

    private TestRankFragment testRankFragment;
    private ListenRankFragment listenRankFragment;
    private LearnRankFragment learnRankFragment;
    private ReadRankFragment readRankFragment;
    private int timeType = 0;
    private String[] mTitles = new String[]{"学习排行","做题排行", "听力排行"};

    public RankingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    Fragment currentFragment;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public void share(){
        if (currentFragment instanceof ListenRankFragment){
          ((ListenRankFragment) currentFragment).share();
        } else if (currentFragment instanceof ReadRankFragment){
          ((ReadRankFragment) currentFragment).share();
        } else if (currentFragment instanceof LearnRankFragment){
            ((LearnRankFragment)currentFragment).share();
        } else
            ((TestRankFragment)currentFragment).share();

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            if (testRankFragment == null){
                testRankFragment = new TestRankFragment();
//                return testRankFragment;
            }
            return new TestRankFragment();
        }else if (position == 2){
            if (listenRankFragment == null){
                listenRankFragment = new ListenRankFragment();
                return new ListenRankFragment();
            }
            return new ListenRankFragment();
        }
//        else if (position == 3){
//            if (readRankFragment == null){
//                readRankFragment = new ReadRankFragment();
//                return new ReadRankFragment();
//            }
//            return  new ReadRankFragment();
//        }
        else if(learnRankFragment == null){
            learnRankFragment = new LearnRankFragment();
            return new LearnRankFragment();
        }
        return new LearnRankFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof LearnRankFragment) {
            ((LearnRankFragment) object).updateLearnRank(timeType);
        } else if (object instanceof TestRankFragment) {
            ((TestRankFragment) object).updateTestRank(timeType);
        }else if (object instanceof ListenRankFragment) {
            ((ListenRankFragment) object).updateTestRank(timeType);
        }
        return super.getItemPosition(object);
//		return POSITION_NONE;
    }

    public void setTimeType(int type) {
        timeType = type;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}
