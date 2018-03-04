package com.my.baffinrpc.core.common.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FixedSizeThreadPoolFactory implements ThreadPoolFactory {

    @Override
    public ExecutorService newThreadPool(int corePoolSize, int maximumPoolSize, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(maximumPoolSize,threadFactory);
    }
}
