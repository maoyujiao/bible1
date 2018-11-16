package com.iyuba.trainingcamp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iyuba.trainingcamp.R;
import com.iyuba.trainingcamp.utils.Constants;
import com.iyuba.trainingcamp.utils.SP;

/**
 * @author yq QQ:1032006226
 * @name bible
 * @class name：com.iyuba.gold.activity
 * @class describe
 * @time 2018/10/9 18:09
 * @change
 * @chang time
 * @class describe
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
    }

    private void initTheme() {
        int  theme = (int) SP.get(getApplicationContext(),"theme",Constants.ORANGE);
        setTheme(R.style.OrangeThem);
        switch (theme) {
            case Constants.BLUE:
                setTheme(R.style.BlueTheme);
                break;
            case Constants.GREEN:
                setTheme(R.style.GreenTheme);
                break;
            case Constants.RED:
                setTheme(R.style.RedTheme);
                break;
            case Constants.ORANGE:
                setTheme(R.style.OrangeThem);
                break;
            default:
                setTheme(R.style.OrangeThem);
                break;
        }
    }
}
