package com.my.baffinrpc.core.common.future;

import com.my.baffinrpc.core.common.model.Result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureAdapter<V> implements Future<V> {

    private final ResponseFuture<Result> responseFuture;

    public FutureAdapter(ResponseFuture<Result> responseFuture) {
        this.responseFuture = responseFuture;
    }

    //async rpc can not be cancelled
    @Override
    public final boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    //async rpc an not be cancelled
    @Override
    public final boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return responseFuture.isDone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get() throws InterruptedException, ExecutionException {
        try {
            return (V)(responseFuture.get().recreateInvokeResult());
        } catch (Throwable throwable) {
            throw new ExecutionException(throwable);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException{
        try {
            return (V)(responseFuture.get(timeout,unit).recreateInvokeResult());
        } catch (InterruptedException e)
        {
            throw e;
        } catch (Throwable throwable) {
            throw new ExecutionException(throwable);
        }
    }
}
