package com.my.baffinrpc.core.cluster.loadbalance;


import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

public class LeastActiveLoadBalance extends AbstractLoadBalanceStrategy {
    @Override
    protected Invoker doSelect(List<Invoker> invokers) {
        return null;
    }
}
