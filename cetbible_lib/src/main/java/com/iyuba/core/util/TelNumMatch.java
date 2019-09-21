/**
 *
 */
package com.iyuba.core.util;


/**
 * 用于判断一串数字是否是手机号
 *
 * @author Administrator
 */
public class TelNumMatch {

    static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1}))[0-9]{8}$";

    String mobPhnNum;

    public TelNumMatch(String mobPhnNum) {
        this.mobPhnNum = mobPhnNum;
    }

    public int matchNum() {
        int flag;
        if (mobPhnNum.length() == 11) {
            if (mobPhnNum.matches(YD)) {
                flag = 1;
            } else if (mobPhnNum.matches(LT)) {
                flag = 2;
            } else if (mobPhnNum.matches(DX)) {
                flag = 3;
            } else {
                flag = 4;
            }
        } else {
            flag = 5;
        }
        return flag;
    }
}