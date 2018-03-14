package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.cluster.ClusterInvoker;
import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

/***
 * 高可用策略
 */
@Extension
public interface HighAvailableStrategy {

    /***
     * 实现高可用地调用
     * @param invokers
     * @param clusterInvoker
     * @param invocation
     * @param directory
     * @param loadBalanceStrategy
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Result invoke(List<Invoker> invokers, ClusterInvoker clusterInvoker, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception;
}

