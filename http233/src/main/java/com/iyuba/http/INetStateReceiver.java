package com.iyuba.http;

// Is this stuff really necessary??
public interface INetStateReceiver {
    void onStartConnect(BaseHttpRequest request, int rspCookie);

    void onConnected(BaseHttpRequest request, int rspCookie);

    void onStartSend(BaseHttpRequest request, int rspCookie, int totalLen);

    void onSend(BaseHttpRequest request, int rspCookie, int len);

    void onSendFinish(BaseHttpRequest request, int rspCookie);

    void onStartRecv(BaseHttpRequest request, int rspCookie, int totalLen);

    void onRecv(BaseHttpRequest request, int rspCookie, int len);

    void onRecvFinish(BaseHttpRequest request, int rspCookie);

    void onNetError(BaseHttpRequest request, int rspCookie,
                    ErrorResponse errorInfo);

    void onCancel(BaseHttpRequest request, int rspCookie);
}
