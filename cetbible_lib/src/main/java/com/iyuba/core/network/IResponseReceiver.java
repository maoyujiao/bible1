package com.iyuba.core.network;

import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;

public interface IResponseReceiver {
    void onResponse(BaseHttpResponse response, BaseHttpRequest request,
                    int rspCookie);
}
