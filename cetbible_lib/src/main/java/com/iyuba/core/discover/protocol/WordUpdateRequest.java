package com.iyuba.core.discover.protocol;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;

import java.io.IOException;


public class WordUpdateRequest extends BaseXMLRequest {

    public static final String MODE_INSERT = "insert";
    public static final String MODE_DELETE = "delete";
    String userId;
    String groupname = "Iyuba";
    String word;

    public WordUpdateRequest(String userId, String update_mode, String word) {
        this.userId = userId;
        this.word = word;
        setAbsoluteURI("http://word.iyuba.cn/words/updateWord.jsp?userId="
                + this.userId + "&mod=" + update_mode + "&groupName="
                + groupname + "&word=" + this.word);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new WordUpdateResponse();
    }

}
