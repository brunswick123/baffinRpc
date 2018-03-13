package com.my.baffinrpc.core.cluster.loadbalance;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

@ExtensionImpl(name = "random", extension = LoadBalanceStrategy.class)
public class RandomLoadBalance extends AbstractLoadBalanceStrategy {
    @Override
    protected Invoker doSelect(List<Invoker> invokers) {
        int size = invokers.size();
        int randomIndex = (int)(Math.random() * size);
        return invokers.get(randomIndex);
    }
}
