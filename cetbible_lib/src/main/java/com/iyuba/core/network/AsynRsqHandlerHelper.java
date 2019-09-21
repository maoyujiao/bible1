package com.iyuba.core.network;

import com.iyuba.core.protocol.BaseHttpRequest;
import com.iyuba.core.protocol.BaseHttpResponse;
import com.iyuba.core.protocol.BaseResponse;
import com.iyuba.core.protocol.ErrorResponse;

/**
 * 异步请求处理器
 *
 * @author wuwei
 *         <p>
 *         每一次请求都是通过一个线程来完成，这样可实现异步通讯，但，线程同步、中断等处可能会发生异常
 */
public class AsynRsqHandlerHelper extends Thread {

    private int rspCookie = -1;
    private BaseHttpRequest request;
    private IResponseReceiver rspReceiver;
    private IErrorReceiver errorReceiver;
    private INetStateReceiver stateReceiver;
    private boolean isWorking = false;
    private boolean isBad = false;
    private boolean isCanceled = false;

    // // 本地回复缓存,最大缓存数量为10
    // static private Cache rspCache = new Cache(10);

    synchronized static BaseHttpResponse getCacheResponse(
            BaseHttpRequest request) {
        int a = request.hashCode();
        Object aObject = Cache.Instance().get(a);
        return (BaseHttpResponse) aObject;
    }

    synchronized static void addCacheResponse(BaseHttpRequest request,
                                              BaseHttpResponse response) {
        Cache.Instance().add(new Integer(request.hashCode()), response);
    }

    public final boolean commitRequest(int rspCookie, BaseHttpRequest request,
                                       IResponseReceiver rspReceiver, IErrorReceiver errorReceiver,
                                       INetStateReceiver stateReceiver) {
        setCancelflag(false);
        setWorkflag(true);
        reset(rspCookie, request, rspReceiver, errorReceiver, stateReceiver);

        if (!isAlive()) {
            try {
                start();
            } catch (IllegalThreadStateException e) {
                setBadflag(true);
                setWorkflag(false);
                reset(-1, null, null, null, null);
                return false;
            }

        }

        synchronized (this) {
            notifyAll();
        }

        return true;
    }

    public final void cancel() {
        setCancelflag(true);
        if (isAlive()) {
            interrupt();
        }
    }

    public final boolean isWorking() {
        return isWorking;
    }

    public final boolean isBad() {
        return isBad;
    }

    private void reset(int rspCookie, BaseHttpRequest request,
                       IResponseReceiver rspReceiver, IErrorReceiver errorReceiver,
                       INetStateReceiver stateReceiver) {
        this.rspCookie = rspCookie;
        this.request = request;
        this.rspReceiver = rspReceiver;
        this.errorReceiver = errorReceiver;
        this.stateReceiver = stateReceiver;
    }

    private void setWorkflag(boolean isWorking) {
        this.isWorking = isWorking;
    }

    private void setBadflag(boolean isBad) {
        this.isBad = isBad;
    }

    private boolean isCanceled() {
        return isCanceled;
    }

    private void setCancelflag(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    @Override
    public void run() {

        BaseHttpRequest request = null;
        IResponseReceiver rspReceiver = null;
        IErrorReceiver errorReceiver = null;
        INetStateReceiver stateReceiver = null;
        int rspCookie = 0;

        try {
            while (!isWorking()) {
                synchronized (this) {
                    wait();
                }
            }

            request = this.request;
            rspReceiver = this.rspReceiver;
            errorReceiver = this.errorReceiver;
            stateReceiver = this.stateReceiver;
            rspCookie = this.rspCookie;
            reset(-1, null, null, null, null);

            if (isCanceled()) {
                setWorkflag(false);
                if (stateReceiver != null) {
                    stateReceiver.onCancel(request, rspCookie);
                }
                // continue;
            }

            // // 优先从缓存中读取
            // BaseHttpResponse cacheRsp = getCacheResponse(request);
            // if (cacheRsp != null && request.isGetCache) {
            // if (rspReceiver != null) {
            //
            // rspReceiver.onResponse(cacheRsp, request, rspCookie);
            //
            // // 让接收器有机会知道处理完全OK,null表示成功
            // if (errorReceiver != null) {
            // errorReceiver.onError(null, request, rspCookie);
            // }
            // }
            // }
            // // 缓存未命中，从服务器读取
            // else {
            BaseResponse rsp = RsqHandleHelper.getResponseImpl(rspCookie,
                    request, stateReceiver);

            if (isCanceled()) {
                setWorkflag(false);
                if (stateReceiver != null) {
                    stateReceiver.onCancel(request, rspCookie);
                }
                // continue;
            }
            if (rsp instanceof ErrorResponse) {
                if (errorReceiver != null) {

                    errorReceiver.onError((ErrorResponse) rsp, request,
                            rspCookie);
                }
            } else {
                // if (request.needCacheResponse()) {
                // addCacheResponse(request, (BaseHttpResponse) rsp);
                // }
                if (rspReceiver != null) {

                    rspReceiver.onResponse((BaseHttpResponse) rsp, request,
                            rspCookie);

                }

                // 让接收器有机会知道处理完全OK,null表示成功
                if (errorReceiver != null) {
                    errorReceiver.onError(null, request, rspCookie);
                }
            }
            // }

            setWorkflag(false);
        } catch (InterruptedException e) {// 对于线程中断异常的处理，这里可能会有问题，直接吞掉异常是否正确
            e.printStackTrace();
            setWorkflag(false);

            if (errorReceiver != null) {
                ErrorResponse rsp = new ErrorResponse(
                        ErrorResponse.ERROR_INTERRUPT);
                errorReceiver.onError(rsp, request, rspCookie);
            }
            if (stateReceiver != null) {
                stateReceiver.onCancel(request, rspCookie);
            }
        } catch (Exception e) {
            // 出现其他任何异常，置本处理器错误标志，
            // 告诉使用者自己无法再使用。
            e.printStackTrace();
            setBadflag(true);
            setWorkflag(false);
            if (stateReceiver != null) {
                stateReceiver.onCancel(request, rspCookie);
            }
            reset(-1, null, null, null, null);
            if (errorReceiver != null) {
                errorReceiver.onError(new ErrorResponse(
                                ErrorResponse.ERROR_THREAD, "work thread error!"),
                        request, rspCookie);
            }
        }

        super.run();
    }

}
