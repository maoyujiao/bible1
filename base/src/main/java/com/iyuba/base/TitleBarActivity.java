package com.iyuba.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * TitleBarActivity
 *
 * @author wayne
 * @date 2017/11/18
 */
public abstract class TitleBarActivity extends BaseActivity {
    private ViewGroup mContentView;
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView tvTitleRight;
    private ImageView ivTitleRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (ViewGroup) getLayoutInflater().inflate(R.layout.base_activity_title_bar, null);

        mToolbar = mContentView.findViewById(R.id.toolbar);
        tvTitle = mContentView.findViewById(R.id.tv_title_bar_title);
        tvTitleRight = mContentView.findViewById(R.id.tv_title_bar_right);
        ivTitleRight = mContentView.findViewById(R.id.iv_title_bar_right);

    }

    public void setTitleBarTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitleBarTitle(int title) {
        tvTitle.setText(title);
    }

    public TextView getTitleBarTitle() {
        return tvTitle;
    }

    public TextView getTitleBarRightButton() {
        return tvTitleRight;
    }

    public ImageView getTitleBarRightImageView() {
        return ivTitleRight;
    }

    public Toolbar getTitleBar() {
        return mToolbar;
    }

    public void setDefaultBackButton() {
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        ((ViewGroup) mContentView.getChildAt(0)).addView(view);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(View view) {
        ((ViewGroup) mContentView.getChildAt(0)).addView(view);
        super.setContentView(mContentView);
    }
}
