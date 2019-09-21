package com.iyuba.http.toolbox;

import com.iyuba.http.BaseHttpRequest;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.ClientSession;
import com.iyuba.http.ErrorResponse;
import com.iyuba.http.IErrorReceiver;
import com.iyuba.http.IResponseReceiver;

/**
 * Use this stuff to deal with request and response while we just need to deal
 * the situation of response success and network error
 */
public class ExeProtocol {

    public static <T extends BaseHttpRequest> void exe(T request,
                                                       final ProtocolResponse protocolResponse) {
        ClientSession.getInstance().asynGetResponse(request, new IResponseReceiver() {
            @Override
            public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                protocolResponse.finish(response);
            }
        }, new IErrorReceiver() {

            @Override
            public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                protocolResponse.error();
            }
        }, null);
    }
}