package com.iyuba.http;

/**
 * The ErrorReceiver instance is a CALLBACK used to deal with the error response
 * returned in case of an unsuccessful HTTP request processing.
 */
public interface IErrorReceiver {

    /**
     * @param errorResponse the returned error response
     * @param request       corresponding HTTP request
     * @param rspCookie
     */
    void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie);
}
