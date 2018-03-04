package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

public class FailoverHighAvailable extends AbstractHighAvailableStrategy {
    private static final int allowRetryTime = 3;
    @Override
    protected <T> Result doInvoke(List<Invoker> invokers, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception {
        int retryCount = 0;
        while (retryCount <= allowRetryTime)
        {
            if (retryCount > 1) {
                invokers = directory.getInvokers();
            }
            Invoker invoker = doSelect(invokers, loadBalanceStrategy);
            if (invoker != null)
            {
                try {
                    invocation.setUrl(invoker.getUrl());
                    invocation.setFromURL(invocation.getUrl());
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
                if (retryCount == allowRetryTime) {
                    throw new RPCFrameworkException("invoker == null for " + invocation.getInterfaceName() + "." + invocation.getMethodName());
                }
            }
            retryCount++;
        }
        throw new RPCFrameworkException("invoke failed for " + invocation.getInterfaceName() + "." + invocation.getMethodName());
    }
}
