package com.iyuba.adsdk.other;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class DensityUtils {

	public static float dp2px(float dpValue, Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics);
	}

	public static int b(float dpValue, Context context) {
		return (int) (dp2px(dpValue, context) + 0.5F);
	}
}

/*
 * Location: C:\Users\Administrator\Desktop\youdaosdk-3.7.2.jar Qualified Name:
 * com.iyuba.youdaosdkcopy.other.C JD-Core Version: 0.6.2
 */