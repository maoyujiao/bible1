package com.iyuba.http;

import android.os.Process;

public class AsynRequestHandler implements Runnable {
    private static final String TAG = AsynRequestHandler.class.getSimpleName();

    private int rspCookie = -1;
    private BaseHttpRequest request;
    private IResponseReceiver rspReceiver;
    private IErrorReceiver errorReceiver;
    private INetStateReceiver stateReceiver;

    public AsynRequestHandler(int rspCookie, BaseHttpRequest request,
                              IResponseReceiver rspReceiver, IErrorReceiver errorReceiver, INetStateReceiver stateReceiver) {
        this.rspCookie = rspCookie;
        this.request = request;
        this.rspReceiver = rspReceiver;
        this.errorReceiver = errorReceiver;
        this.stateReceiver = stateReceiver;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        BaseResponse rsp = RequestHandleHelper.getResponse(rspCookie, request, stateReceiver);

        if (rsp instanceof ErrorResponse) {
            if (errorReceiver != null) {
                errorReceiver.onError((ErrorResponse) rsp, request, rspCookie);
            }
        } else {
            if (rspReceiver != null) {
                rspReceiver.onResponse((BaseHttpResponse) rsp, request, rspCookie);
            }
        }
    }

}
