package com.iyuba.http;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientSession {
    private static final String TAG = ClientSession.class.getSimpleName();
    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final int POOL_SIZE = CORES + 1;
    private static final int POOL_SIZE_MAX = CORES * 2 + 1;
    private static final BlockingQueue<Runnable> REQUEST_POOL_QUEUE = new LinkedBlockingQueue<Runnable>(
            56);
    private static final ThreadFactory REQUEST_FACTORY = new ThreadFactory() {
        private final AtomicInteger COUNT = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "REQUEST #" + COUNT.getAndIncrement());
        }
    };
    private static final ExecutorService REQUEST_POOL = new ThreadPoolExecutor(POOL_SIZE,
            POOL_SIZE_MAX, 3, TimeUnit.SECONDS, REQUEST_POOL_QUEUE, REQUEST_FACTORY);
    private static ClientSession instance;
    /**
     * The default ErrorReceiver instance, just log out error info.
     */
    public IErrorReceiver defErrorReceiver = new IErrorReceiver() {

        @Override
        public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error Type Code : ").append(errorResponse.getErrorType()).append("\n");
            sb.append("Error Description : ").append(errorResponse.getErrorDesc());
            LOGUtils.e(TAG, sb.toString());
        }
    };

    /**
     * 协议请求状态接收机
     */
    public INetStateReceiver defStateReceiver;

    private ClientSession() {

    }

    public static synchronized ClientSession getInstance() {
        if (instance == null) {
            instance = new ClientSession();
        }
        return instance;
    }

    /**
     * 异步获取请求回复接口 使用默认错误接收器及网络状态接收器作为通知接口,并采用互斥方式，防止多线程并发请求
     *
     * @param request
     * @param receiver return: 见下一接口说明
     */
    public int asynGetResponse(BaseHttpRequest request, IResponseReceiver receiver) {
        return asynGetResponse(request, receiver, defErrorReceiver, defStateReceiver);
    }

    /**
     * 异步获取请求回复接口 使用默认错误接收器及网络状态接收器作为通知接口,并采用互斥方式，防止多线程并发请求
     *
     * @param request
     * @param receiver return: 见下一接口说明
     */
    public int asynGetResponse(BaseHttpRequest request, IResponseReceiver receiver,
                               IErrorReceiver defErrorReceiver) {
        return asynGetResponse(request, receiver, defErrorReceiver, defStateReceiver);
    }

    /**
     * 异步获取请求回复接口 采用互斥方式，防止多线程并发请求
     *
     * @param request
     * @param rspReceiver
     * @param errReceiver
     * @param stateReceiver return: 标识该请求的cookie，上层可利用该cookie来取消此次请求
     */
    synchronized public int asynGetResponse(BaseHttpRequest request, IResponseReceiver rspReceiver,
                                            IErrorReceiver errReceiver, INetStateReceiver stateReceiver) {
        synchronized (getAsynRsqLock()) {
            return asynGetResponseWithoutLock(request, rspReceiver, errReceiver, stateReceiver);
        }
    }

    /**
     * 异步获取请求回复接口 使用默认错误接收器及网络状态接收器作为通知接口,非锁定方式，这里考虑到上层可能一次会批量调用
     * 接口多次，为提高效率，在调用之前需要上层负责锁定，以下接口同样如此。
     *
     * @param request
     * @param receiver return: 见下一接口说明
     */
    public int asynGetResponseWithoutLock(BaseHttpRequest request, IResponseReceiver receiver) {
        return asynGetResponseWithoutLock(request, receiver, defErrorReceiver, defStateReceiver);
    }

    /**
     * 异步获取请求回复接口
     *
     * @param request
     * @param rspReceiver
     * @param errReceiver
     * @param stateReceiver return: 标识该请求的cookie，上层可利用该cookie来取消此次请求
     */
    public int asynGetResponseWithoutLock(BaseHttpRequest request, IResponseReceiver rspReceiver,
                                          IErrorReceiver errReceiver, INetStateReceiver stateReceiver) {

        REQUEST_POOL.execute(new AsynRequestHandler(233, request, rspReceiver, errReceiver,
                stateReceiver));

        return 233;
    }

    /**
     * 获取异步请求锁
     *
     * @return
     */
    public Object getAsynRsqLock() {
        return this;
    }

    /**
     * 同步获取请求回复接口 调用此接口后在得到回复前会一直阻塞注, 当出现任何错误时获取到得回复实际类型为ErrorResponse,
     * 故调用此接口后需要利用instanceof关键字判断是否是错误回复， 并进行相应错误处理。
     * <p>
     * After Android API 15, you can't send a network request in the UI thread, so
     * this method should not be used anymore!!
     *
     * @param request
     * @return
     */
    @Deprecated
    public BaseResponse syncGetResponse(BaseHttpRequest request) {
        return RequestHandleHelper.getResponse(-1, request, null);
    }

}
