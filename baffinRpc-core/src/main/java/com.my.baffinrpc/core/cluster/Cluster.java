package com.my.baffinrpc.core.cluster;

import com.my.baffinrpc.core.cluster.highavailable.HighAvailableStrategy;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.protocol.invoker.Invoker;


public interface Cluster {
    <T> Invoker createVirtualInvoker(Directory<T> directory, HighAvailableStrategy haStrategy, LoadBalanceStrategy loadBalanceStrategy);
}
