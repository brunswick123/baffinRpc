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
 * 调用失败后进行重试一定次数
 */
@ExtensionImpl(name = "failover",extension = HighAvailableStrategy.class)
public class FailoverHighAvailable extends AbstractHighAvailableStrategy {
    private int allowRetryTime;

    @Override
    public <T> Result invoke(List<Invoker> invokers, ClusterInvoker clusterInvoker, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception {
        int retryCount = 0;
        while (retryCount <= allowRetryTime)
        {
            if (retryCount > 1)
                invokers = directory.getInvokers(); //重新获取下全部的invoker
            Invoker invoker = doSelect(invokers, loadBalanceStrategy);
            if (invoker != null)
            {
                try {
                    return invoker.invoke(invocation);
                }
                catch (Exception e)
                {
                    //最后一次尝试如果还抛出异常 包装成RPCFrameworkException抛出
                    if (retryCount == allowRetryTime)
                        throw new RPCFrameworkException(e);
                }
            }
            else
            {
                if (retryCount == allowRetryTime)
                    throw new RPCFrameworkException("invoker == null for " + invocation.getInterfaceName() + "." + invocation.getMethodName());
            }
            retryCount++;
        }
        throw new RPCFrameworkException("invoke failed for " + invocation.getInterfaceName() + "." + invocation.getMethodName());
    }


}
