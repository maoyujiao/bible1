package com.iyuba.core.me.pay;


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
