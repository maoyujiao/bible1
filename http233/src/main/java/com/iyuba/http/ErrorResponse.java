package com.iyuba.http;

/**
 * 错误回复包，所有与服务器交互后的错误均在此汇总， 包括网络错误，服务器错误，协议错误以及未知错误等
 *
 * @author zhouyin
 */
public class ErrorResponse implements BaseResponse {
    // public final static int ERROR_PARAM_INVALID = 0; // 请求地址参数格式错误
    // public final static int ERROR_NET_NO_CONNECTION = 1;// 手机当前没有可用连接
    // public final static int ERROR_NET_DISCONNECTED = 2; // 无法连接服务器或断开
    // public final static int ERROR_NET_TIMEOUT = 3; // 网络连接超时
    // public final static int ERROR_NULL_RESULT = 4; // 没有获取到任何结果
    // public final static int ERROR_SERVER = 5; // 服务器内部错误
    // public final static int ERROR_PROTOTOL = 6; // 协议解析错误
    // public final static int ERROR_THREAD = 7; // 工作线程错误
    // public final static int ERROR_UNKOWN = 8; // 未知错误
    // public final static int ERROR_INVALID_RESULT = 9; // 服务器无法获取有效结果
    // public final static int ERROR_CLIENT_NET_DISCONNECTED = 10;// 本地网络不可用
    // //////////////////////////////////////////////////////////////////////////////
    public final static int ERROR_UNKNOWN = 900;// 其他未知错误

    public final static int ERROR_NET_CONNECTION = 911;// 连接异常
    public final static int ERROR_ClLIENT_PROTOCOL = 912;// Client协议异常
    public final static int ERROR_NET_TIMEOUT = 913;// 网络连接超时
    public final static int ERROR_NET_SOCKET = 914;// Socket异常
    public final static int ERROR_NET_IO = 915;// IO异常

    public final static int ERROR_PROTOCOL = 921;// 协议解析异常
    public final static int ERROR_INVALID_RESULT = 922;// 服务正常，但返回结果不可用
    public final static int ERROR_SERVER = 923;// 应答出现错误
    public final static int ERROR_PARAM_INVALID = 924; // 请求地址参数格式错误

    public final static int ERROR_THREAD = 931;// 工作线程错误
    public final static int ERROR_INTERRUPT = 932;// 线程中断错误

    private int errorType;

    /**
     * when the default constructor is called, just set the error type to a
     * default value(unknown)
     */
    public ErrorResponse() {
        this.errorType = ERROR_UNKNOWN;
    }

    /**
     * @param type error type code, the value should be one of the value offered by
     *             ErrorResponse class
     */
    public ErrorResponse(int type) {
        this.errorType = type;
    }

    public final int getErrorType() {
        return errorType;
    }

    public final String getErrorDesc() {
        switch (errorType) {
            case ERROR_UNKNOWN:
                return "The cause of this error is unknown yet!!";
            case ERROR_NET_IO:
                return "An internet io exception caused this error!!";
            case ERROR_PARAM_INVALID:
                return "The malformed param of url caused this error!!";
            case ERROR_THREAD:
                return "The worker thread's failure caused this error!!";
            case ERROR_PROTOCOL:
                return "The failure of parsing response content caused this error!!";
            // TODO more error type to be continued
            default:
                return "null";
        }
    }

}
