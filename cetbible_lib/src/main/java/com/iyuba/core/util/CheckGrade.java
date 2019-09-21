/**
 *
 */
package com.iyuba.core.util;

import com.iyuba.biblelib.R;
import com.iyuba.configation.RuntimeManager;

/**
 * 根据积分判断等级
 *
 * @author 陈彤
 */
public class CheckGrade {

    public static int Check(String points) {
        int i = 0, j = 0;
        i = Integer.parseInt(points);
        if (i >= 0 && i < 51) {
            j = 1;
        } else if (i > 50 && i < 201) {
            j = 2;
        } else if (i > 200 && i < 501) {
            j = 3;
        } else if (i > 500 && i < 1001) {
            j = 4;
        } else if (i > 1000 && i < 2001) {
            j = 5;
        } else if (i > 2000 && i < 4001) {
            j = 6;
        } else if (i > 4000 && i < 7001) {
            j = 7;
        } else if (i > 7000 && i < 12001) {
            j = 8;
        } else if (i > 12000 && i < 20001) {
            j = 9;
        } else if (i > 20001) {
            j = 10;
        }
        return j;
    }

    public static String CheckLevelName(String points) {
        String[] name = RuntimeManager.getContext().getResources()
                .getStringArray(R.array.level);
        int pos = Check(points) - 1;
        if (pos < 0) {
            pos = 0;
        }
        return name[pos];
    }
}
