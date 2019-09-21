package com.iyuba.core.discover.activity;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.iyuba.configation.RuntimeManager;

/**
 * @author yao
 */
public class THSActivity extends Activity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        RuntimeManager.setApplication(getApplication());
        RuntimeManager.setDisplayMetrics(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        mContext = this;
        RuntimeManager.setApplication(getApplication());
        RuntimeManager.setDisplayMetrics(this);
    }


}
