package com.my.baffinrpc.core.common;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * rpc上下文类
 * 存储当前调用信息
 */
public class RpcContext {

    private volatile Future<?> future;
    private volatile boolean asyncCall;
    private String URLString;

    /***
     * 每个线程独立有一个RpcContext实例
     */
    private static final ThreadLocal<RpcContext> localRPCContext = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };


    private RpcContext() {
        future = null;
        asyncCall = false;
        URLString = "";
    }


    public static RpcContext getContext() {
        return localRPCContext.get();
    }

    public Future<?> getFuture() {
        if (!asyncCall)
            throw new IllegalStateException("Current rpc is not an async call, future is not available for sync call");
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }


    public void setAsyncCall(boolean asyncCall) {
        this.asyncCall = asyncCall;
    }

    public String getURLString() {
        return URLString;
    }

    public void setURLString(String URLString) {
        this.URLString = URLString;
    }
}

