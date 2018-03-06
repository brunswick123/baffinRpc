package com.my.baffinrpc.core.common;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/***
 * rpc上下文类
 * 存储当前调用信息
 */
public class RpcContext {

    private volatile Future<?> future;
    private final AtomicBoolean asyncCall;
    private String URLString;

    private static final ThreadLocal<RpcContext> localRPCContext = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };


    private RpcContext() {
        future = null;
        asyncCall = new AtomicBoolean(false);
        URLString = "";
    }


    public static RpcContext getContext() {
        return localRPCContext.get();
    }

    public Future<?> getFuture() {
        if (!asyncCall.get())
            throw new IllegalStateException("Current rpc is not an async call, future is not available for sync call");
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }


    public void setAsyncCall(boolean asyncCall) {
        this.asyncCall.set(true);
    }

    public String getURLString() {
        return URLString;
    }

    public void setURLString(String URLString) {
        this.URLString = URLString;
    }
}

