package com.my.baffinrpc.core.common.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

public interface ThreadPoolFactory {
    ExecutorService newThreadPool(int corePoolSize, int maximumPoolSize, ThreadFactory threadFactory);
}
