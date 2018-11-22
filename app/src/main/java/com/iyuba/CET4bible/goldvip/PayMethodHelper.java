package com.iyuba.CET4bible.goldvip;


public class PayMethodHelper {

    public static int mapPositionToMethod(int position) {
        switch (position) {
            case 0:
                return PayMethodAdapter.PayMethod.ALIPAY;
            case 1:
                return PayMethodAdapter.PayMethod.WEIXIN;
          /*  case 2:
                return PayMethodAdapter.PayMethod.BANKCARD;*/
        }
        return PayMethodAdapter.PayMethod.ALIPAY;
    }
}
