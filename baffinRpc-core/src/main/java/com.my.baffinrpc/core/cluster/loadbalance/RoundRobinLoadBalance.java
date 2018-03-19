package com.my.baffinrpc.core.cluster.loadbalance;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 轮询的负载均衡策略
 */
@ExtensionImpl(name = "roundRobin", extension = LoadBalanceStrategy.class)
public class RoundRobinLoadBalance extends AbstractLoadBalanceStrategy {

    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    protected Invoker doSelect(List<Invoker> invokers) {
        return invokers.get(index.incrementAndGet() % invokers.size());
    }
}
