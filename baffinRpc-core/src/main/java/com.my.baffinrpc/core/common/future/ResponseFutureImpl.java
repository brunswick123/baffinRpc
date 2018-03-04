package com.my.baffinrpc.core.common.future;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.message.Response;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResponseFutureImpl implements ResponseFuture<Result> {

    private final static Map<UUID,ResponseFutureImpl> FUTURE_MAP = new ConcurrentHashMap<>();
    private volatile Result result;
    private AtomicBoolean done = new AtomicBoolean(false);
    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public Result get() throws InterruptedException, ExecutionException {
        return get(5000,TimeUnit.SECONDS);
    }

    @Override
    public Result get(long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException {
        if(!latch.await(timeout,timeUnit))
            throw new RPCFrameworkException("method invoke time out");
        return result;
    }

    @Override
    public boolean isDone() {
        return done.get();
    }

    public static Map<UUID, ResponseFutureImpl> futureMap() {
        return FUTURE_MAP;
    }

    public static void received(Response response)
    {
        ResponseFutureImpl responseFuture = FUTURE_MAP.get(response.getMessageId());
        if (responseFuture != null)
        {
            responseFuture.doReceived(response);
        }
    }

    private void doReceived(Response response)
    {
        result = response.getResult();
        done.set(true);
        latch.countDown();
    }
}
