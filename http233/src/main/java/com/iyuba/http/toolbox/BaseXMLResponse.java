package com.iyuba.http.toolbox;

import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.ErrorResponse;
import com.iyuba.http.toolbox.xml.kXMLElement;
import com.iyuba.http.toolbox.xml.kXMLParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BaseXMLResponse extends BaseHttpResponse {
    private InputStream inputStream = null;

    @Override
    public ErrorResponse parseInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        kXMLElement doc = parseResponse(inputStream);
        if (!extractBody(null, doc)) {
            return new ErrorResponse(ErrorResponse.ERROR_PROTOCOL);
        }
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public boolean isAllowCloseInputStream() {
        return true;
    }

    /**
     * 协议回复内容提取抽象接口
     *
     * @param headerElement
     * @param bodyElement
     * @return true--success, false--fail
     */
    protected abstract boolean extractBody(kXMLElement headerEleemnt, kXMLElement bodyElement);

    private kXMLElement parseResponse(InputStream is) throws IOException {
        kXMLElement doc = new kXMLElement();
        try {
            doc.parseFromReader(new InputStreamReader(is, HttpHeaderParser.parseCharset(mResponseHeaders)));
            return doc;
        } catch (kXMLParseException e) {
            return null;
        }
    }

}