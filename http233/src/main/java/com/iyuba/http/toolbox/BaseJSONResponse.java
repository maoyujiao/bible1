package com.iyuba.http.toolbox;

import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.ErrorResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class BaseJSONResponse extends BaseHttpResponse {
    private InputStream inputStream = null;

    @Override
    public ErrorResponse parseInputStream(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        String doc = parseResponse(inputStream);
        if (!extractBody(null, doc)) { // If you don't need it, why do you create it? WTF.
            return new ErrorResponse(ErrorResponse.ERROR_PROTOCOL);
        }
        return null;
    }

    /**
     * get inputstream
     *
     * @return InputStream
     */
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
    protected abstract boolean extractBody(JSONObject headerElement, String bodyElement);

    private String parseResponse(InputStream is) throws IOException {
        if (is == null)
            return null;
        else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            byte[] baosByte = baos.toByteArray();
            return new String(baosByte, HttpHeaderParser.parseCharset(mResponseHeaders));

            // return baos.toString();
            //
            // Watch-Out!!!! Tips Here!!!!
            // The inner logic for the ByteArrayOutputStream's toString() here :
            // toString() --> new String(buf, 0, count) -->
            // new String(data, offset, byteCount, Charset.defaultCharset()) THEN
            // Charset.defaultCharset() --> getDefaultCharset() --> failed and return utf-8

        }
    }
}
