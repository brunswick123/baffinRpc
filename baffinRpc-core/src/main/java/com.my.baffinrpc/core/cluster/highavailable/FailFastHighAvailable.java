package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.cluster.ClusterInvoker;
import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

/***
 * 调用失败直接抛出异常
 */
@ExtensionImpl(name = "failFast",extension = HighAvailableStrategy.class)
public class FailFastHighAvailable extends AbstractHighAvailableStrategy {
    @Override
    public  <T> Result invoke(List<Invoker> invokers, ClusterInvoker clusterInvoker, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception {
        Invoker invoker = doSelect(invokers, loadBalanceStrategy);
        if (invoker != null)
        {
            try
            {
                return invoker.invoke(invocation);
            }catch (Exception e)
            {
                throw new RPCFrameworkException(e);
            }
        }
        else
        {
            throw new RPCFrameworkException("invoker == null for " + invocation.getInterfaceName() + "." + invocation.getMethodName());
        }

    }
}
