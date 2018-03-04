package com.my.baffinrpc.core.common.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface ResponseFuture<V> {
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException;
    boolean isDone();
}
