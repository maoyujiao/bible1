package com.iyuba.abilitytest.sqlite;

import com.iyuba.base.util.L;

/**
 * LOGUtils
 *
 * @author wayne
 * @date 2017/12/11
 */
class LOGUtils {
    public static void e(String tag, String sql) {
        L.e(tag + "  " + sql);
    }
}
