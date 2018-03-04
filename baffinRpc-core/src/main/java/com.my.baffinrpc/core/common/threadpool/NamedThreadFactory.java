package com.my.baffinrpc.core.common.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolCount = new AtomicInteger(1);
    private final AtomicInteger threadCount = new AtomicInteger(1);
    private final String prefix;

    public NamedThreadFactory()
    {
        this("pool" + poolCount.incrementAndGet());
    }

    public NamedThreadFactory(String poolName) {
        this.prefix = poolName + "-thread-";
    }


    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,prefix + threadCount.incrementAndGet());
    }
}
