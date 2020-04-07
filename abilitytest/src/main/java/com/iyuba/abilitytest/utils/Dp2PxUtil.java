package com.iyuba.abilitytest.utils;

import android.content.res.Resources;
import android.util.TypedValue;


/**
 * Created by maoyujiao on 2019/9/24.
 */

public class Dp2PxUtil {
    public static float dp2px(int dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
