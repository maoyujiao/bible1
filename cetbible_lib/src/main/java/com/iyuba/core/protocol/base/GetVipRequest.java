package com.iyuba.core.protocol.base;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;
import com.iyuba.core.util.MD5;

import java.io.IOException;

public class GetVipRequest extends BaseXMLRequest {

    public GetVipRequest(String userId, String appId, String productId) {
        setAbsoluteURI("http://app.iyuba.cn/pay/apiGetVip.jsp?" + "&userId="
                + userId + "&appId=" + appId + "&productId=" + productId
                + "&sign="
                + MD5.getMD5ofStr(appId + userId + productId + "iyuba")
                + "&format=xml");
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new GetVipResponse();
    }

}
