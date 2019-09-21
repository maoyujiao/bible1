package com.iyuba.http.toolbox;

import com.iyuba.http.BaseHttpRequest;

public abstract class BaseXMLRequest extends BaseHttpRequest {
    /**
     * Charset for request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/xml; charset=%s",
            PROTOCOL_CHARSET);

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

}