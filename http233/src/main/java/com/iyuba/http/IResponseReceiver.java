package com.iyuba.http;

/**
 * The ResponseReceiver instance is a CALLBACK used to deal with a HTTP
 * response when the response is return successfully
 */
public interface IResponseReceiver {
    /**
     * @param response  the returned response
     * @param request   corresponding request
     * @param rspCookie
     */
    void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie);
}
