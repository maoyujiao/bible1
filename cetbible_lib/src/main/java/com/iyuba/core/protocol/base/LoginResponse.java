package com.iyuba.core.protocol.base;

import com.iyuba.core.network.xml.Utility;
import com.iyuba.core.network.xml.kXMLElement;
import com.iyuba.core.protocol.BaseXMLResponse;

public class LoginResponse extends BaseXMLResponse {
    public String result, uid, username, imgsrc, vip, validity, amount, isteacher;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {

        try {
            result = Utility.getSubTagContent(bodyElement, "result");
            uid = Utility.getSubTagContent(bodyElement, "uid");
            username = Utility.getSubTagContent(bodyElement, "username");
            imgsrc = Utility.getSubTagContent(bodyElement, "imgSrc");
            vip = Utility.getSubTagContent(bodyElement, "vipStatus");
            validity = Utility.getSubTagContent(bodyElement, "expireTime");
            amount = Utility.getSubTagContent(bodyElement, "Amount");
            isteacher = Utility.getSubTagContent(bodyElement, "isteacher");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
