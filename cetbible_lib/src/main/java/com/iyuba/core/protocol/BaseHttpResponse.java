package com.iyuba.core.protocol;

import com.iyuba.core.network.INetStateReceiver;

import java.io.IOException;
import java.io.InputStream;

/**
 * http回复包接口
 *
 * @author zhouyin
 */
public interface BaseHttpResponse extends BaseResponse {

    /**
     * 从服务器端获取的http输入流解析接口
     *
     * @param rsqCookie     : 对应请求标识Cookie
     * @param request       ：该回复对应的请求
     * @param inputStream   ：回复输入流
     * @param len           : 输入流长度，为-1时表示无法获取输入流长度
     * @param stateRecevier : 状态接收器
     * @throws IOException
     * @return: 如果解析成功返回null, 否则返回相应错误回复包
     */
    ErrorResponse parseInputStream(int rspCookie,
                                   BaseHttpRequest request, InputStream inputStream, int len,
                                   INetStateReceiver stateReceiver) throws IOException;

    /**
     * 是否允许关闭输入流接口 此接口为性能需求而设，考虑到某些应用可能在最上层处理原始输入流更有效率
     *
     * @return
     */
    boolean isAllowCloseInputStream();

    /**
     * 获得输入流
     *
     * @return
     */
    InputStream getInputStream();
}
