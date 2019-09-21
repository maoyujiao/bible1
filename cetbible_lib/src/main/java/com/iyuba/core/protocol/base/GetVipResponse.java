package com.iyuba.core.protocol.base;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;

public class GetVipResponse extends BaseXMLResponse {

    public String result, msg, amount, VipFlg, VipEndTime;

    public GetVipResponse() {
    }

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {

        result = Utility.getSubTagContent(bodyElement, "result");
        msg = Utility.getSubTagContent(bodyElement, "msg");
        amount = Utility.getSubTagContent(bodyElement, "amount");
        VipFlg = Utility.getSubTagContent(bodyElement, "VipFlg");
        VipEndTime = Utility.getSubTagContent(bodyElement, "VipEndTime");
        return true;
    }

}
