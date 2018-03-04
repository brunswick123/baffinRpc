package com.my.baffinrpc.core.cluster.loadbalance;

import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

public abstract class AbstractLoadBalanceStrategy implements LoadBalanceStrategy {
    @Override
    public Invoker select(List<Invoker> invokers) {
        if (invokers == null || invokers.size() == 0)
            return null;
        if (invokers.size() == 1)
            return invokers.get(0);
        return doSelect(invokers);
    }
    protected abstract Invoker doSelect(List<Invoker> invokers);

}
