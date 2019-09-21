package com.iyuba.core.network;

import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.ErrorResponse;

/**
 * @author wuwei
 */
public interface IErrorReceiver {

    void onError(ErrorResponse errorResponse, BaseHttpRequest request,
                 int rspCookie);
}
