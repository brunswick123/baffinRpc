package com.my.baffinrpc.core.cluster.loadbalance;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

/***
 * 负载均衡策略
 */
@Extension
public interface LoadBalanceStrategy {

    /***
     * 选取invoker
     * @param invokers
     * @return
     */
    Invoker select(List<Invoker> invokers);
}
