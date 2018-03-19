package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.cluster.ClusterInvoker;
import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.exception.RPCNoServiceProviderException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

/***
 * 遍历所有invoker,直到发现一个可用的invoker
 */
@ExtensionImpl(name = "firstAvailable",extension = HighAvailableStrategy.class)
public class FirstAvailableHighAvailable extends AbstractHighAvailableStrategy {
    @Override
    public  <T> Result invoke(List<Invoker> invokers, ClusterInvoker clusterInvoker, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception {
        for (Invoker invoker : invokers)
        {
            if (invoker.isAvailable())
            {
                try
                {
                    return invoker.invoke(invocation);
                }catch (Exception e)
                {
                    throw new RPCFrameworkException(e);
                }
            }
        }
        throw new RPCNoServiceProviderException("No invoker available for" + invocation.getInterfaceName() + "." + invocation.getMethodName());
    }
}
