package com.iyuba.core.protocol.news;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;

import java.io.IOException;

public class WordSynRequest extends BaseXMLRequest {
    String user;

    public WordSynRequest(String userid, String userName, int page) {
        this.user = userid;
        setAbsoluteURI("http://word.iyuba.cn/words/wordListService.jsp?u="
                + userid + "&pageCounts=50&pageNumber=" + page);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new WordSynResponse(user);
    }

}
