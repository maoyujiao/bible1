package com.iyuba.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.iyuba.base.util.T;
import com.iyuba.base.widget.MyViewPager;

/**
 * BaseFragment
 */
public class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;
    protected MyViewPager containerVp;


    public void setContainerVp(MyViewPager containerVp) {
        this.containerVp = containerVp;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity;
    }

    public void showShort(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showShort(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void showLong(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showLong(@StringRes int id) {
        T.showShort(mContext, id);
    }
}
