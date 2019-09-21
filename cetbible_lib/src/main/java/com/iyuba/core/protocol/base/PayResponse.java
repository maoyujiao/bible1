package com.iyuba.core.protocol.base;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;

/**
 * @author zqq
 *         <p>
 *         功能：支付回复  result 0是失败，1 是成功  msg 为此次操作信息。  amount
 *         若购买成功，返回此用户当前（购买之后）的amount值
 */
public class PayResponse extends BaseXMLResponse {
    public String result;
    public String msg;
    public String amount;
    public String vipflag;
    public String VipEndTime;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {
        try {
            result = Utility.getSubTagContent(bodyElement, "result");
            msg = Utility.getSubTagContent(bodyElement, "msg");
            amount = Utility.getSubTagContent(bodyElement, "amount");
            vipflag = Utility.getSubTagContent(bodyElement, "VipFlg");
            VipEndTime = Utility.getSubTagContent(bodyElement, "VipEndTime");
        } catch (Exception e) {

        }
        return true;
    }

}
