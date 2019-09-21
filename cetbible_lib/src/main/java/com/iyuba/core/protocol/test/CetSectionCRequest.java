package com.iyuba.core.protocol.test;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;

import java.io.IOException;

public class CetSectionCRequest extends BaseXMLRequest {
    int type;

    public CetSectionCRequest(String url, int type) {
        this.type = type;
        setAbsoluteURI(url);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new CetSectionCResponse(type);
    }

}
