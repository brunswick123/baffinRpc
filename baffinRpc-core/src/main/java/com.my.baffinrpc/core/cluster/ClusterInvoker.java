package com.my.baffinrpc.core.cluster;

import com.my.baffinrpc.core.cluster.highavailable.HighAvailableStrategy;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

public class ClusterInvoker implements Invoker {

    private final Directory directory;
    private final HighAvailableStrategy highAvailableStrategy;
    private final LoadBalanceStrategy loadBalanceStrategy;

    public <T> ClusterInvoker(Directory<T> directory, HighAvailableStrategy highAvailableStrategy, LoadBalanceStrategy loadBalanceStrategy) {
        this.directory = directory;
        this.highAvailableStrategy = highAvailableStrategy;
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    @Override
    public Result invoke(Invocation invocation) throws Exception {
        List<Invoker> invokers = directory.getInvokers();
        /*if (invokers == null || invokers.size() == 0)
            throw new RPCFrameworkException("No invoker is found for " + getInterface().getName() + "." + invocation.getMethodName());*/
        return highAvailableStrategy.invoke(invokers,this,invocation,directory,loadBalanceStrategy);
    }

    @Override
    public URL getUrl() {
        //todo not supported
        throw new UnsupportedOperationException("getUrl() is not supported for VirtualInvoker");
    }

    @Override
    public Class<?> getInterface() {
        return directory.getInterface();
    }

    @Override
    public boolean isAvailable() {
        List<Invoker> invokers = directory.getInvokers();
        if (invokers == null || invokers.size() == 0)
            return false;
        else
        {
            for (Invoker invoker : invokers)
            {
                if (invoker.isAvailable())
                    return true;
            }
            return false;
        }
    }

    @Override
    public void destroy() {
        List<Invoker> invokers = directory.getInvokers();
        if (invokers != null && invokers.size() > 0)
        {
            for (Invoker invoker : invokers)
                invoker.destroy();
        }
    }

}
