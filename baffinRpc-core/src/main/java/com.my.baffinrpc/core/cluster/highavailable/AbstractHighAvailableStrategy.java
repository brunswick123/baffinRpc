package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHighAvailableStrategy implements HighAvailableStrategy {

    protected List<Invoker> getAvailableInvokers(List<Invoker> invokers)
    {
        List<Invoker> availableInvokers = new ArrayList<>(invokers.size());
        if (invokers.size() > 0) {
            for (Invoker invoker : invokers) {
                if (invoker.isAvailable())
                    availableInvokers.add(invoker);
            }
        }
        return availableInvokers;
    }

    protected Invoker doSelect(List<Invoker> invokers, LoadBalanceStrategy loadBalanceStrategy)
    {
        return loadBalanceStrategy.select(getAvailableInvokers(invokers));
    }
}
