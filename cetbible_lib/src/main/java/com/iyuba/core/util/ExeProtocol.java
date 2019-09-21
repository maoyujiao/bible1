/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.util;

import com.iyuba.core.listener.ProtocolResponse;
import com.iyuba.core.network.ClientSession;
import com.iyuba.core.network.INetStateReceiver;
import com.iyuba.core.network.IResponseReceiver;
import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseJSONRequest;
import com.iyuba.core.protocol.BaseXMLRequest;
import com.iyuba.core.protocol.ErrorResponse;

/**
 * 执行网络交互
 *
 * @author 陈彤
 */
public class ExeProtocol {

    public static void exe(BaseJSONRequest request,
                           final ProtocolResponse protocolResponse) {
        ClientSession.Instance().asynGetResponse(request,
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {
                        protocolResponse.finish(response);
                    }
                }, null, new INetStateReceiver() {

                    @Override
                    public void onStartSend(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {


                    }

                    @Override
                    public void onStartRecv(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {


                    }

                    @Override
                    public void onStartConnect(BaseHttpRequest request,
                                               int rspCookie) {


                    }

                    @Override
                    public void onSendFinish(BaseHttpRequest request,
                                             int rspCookie) {


                    }

                    @Override
                    public void onSend(BaseHttpRequest request, int rspCookie,
                                       int len) {


                    }

                    @Override
                    public void onRecvFinish(BaseHttpRequest request,
                                             int rspCookie) {


                    }

                    @Override
                    public void onRecv(BaseHttpRequest request, int rspCookie,
                                       int len) {


                    }

                    @Override
                    public void onNetError(BaseHttpRequest request,
                                           int rspCookie, ErrorResponse errorInfo) {
                        protocolResponse.error();
                    }

                    @Override
                    public void onConnected(BaseHttpRequest request,
                                            int rspCookie) {


                    }

                    @Override
                    public void onCancel(BaseHttpRequest request, int rspCookie) {


                    }
                });
    }

    public static void exe(BaseXMLRequest request,
                           final ProtocolResponse protocolResponse) {
        ClientSession.Instance().asynGetResponse(request,
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response,
                                           BaseHttpRequest request, int rspCookie) {
                        protocolResponse.finish(response);
                    }
                }, null, new INetStateReceiver() {

                    @Override
                    public void onStartSend(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {


                    }

                    @Override
                    public void onStartRecv(BaseHttpRequest request,
                                            int rspCookie, int totalLen) {


                    }

                    @Override
                    public void onStartConnect(BaseHttpRequest request,
                                               int rspCookie) {


                    }

                    @Override
                    public void onSendFinish(BaseHttpRequest request,
                                             int rspCookie) {


                    }

                    @Override
                    public void onSend(BaseHttpRequest request, int rspCookie,
                                       int len) {


                    }

                    @Override
                    public void onRecvFinish(BaseHttpRequest request,
                                             int rspCookie) {


                    }

                    @Override
                    public void onRecv(BaseHttpRequest request, int rspCookie,
                                       int len) {


                    }

                    @Override
                    public void onNetError(BaseHttpRequest request,
                                           int rspCookie, ErrorResponse errorInfo) {
                        protocolResponse.error();
                    }

                    @Override
                    public void onConnected(BaseHttpRequest request,
                                            int rspCookie) {


                    }

                    @Override
                    public void onCancel(BaseHttpRequest request, int rspCookie) {


                    }
                });
    }

}