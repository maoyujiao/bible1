package com.iyuba.core.protocol.base;

import com.iyuba.core.network.xml.XmlSerializer;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseXMLRequest;
import com.iyuba.core.util.TextAttr;

import java.io.IOException;

/**
 * 获取网页单词本
 *
 * @author Administrator
 */
public class DictRequest extends BaseXMLRequest {

    public DictRequest(String word) {
        word = TextAttr.encode(word);
        setAbsoluteURI("http://word.iyuba.cn/words/apiWord.jsp?q=" + word);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) {


    }

    @Override
    public BaseHttpResponse createResponse() {

        return new DictResponse();
    }

}
