package com.my.baffinrpc.core.cluster.highavailable;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.cluster.ClusterInvoker;
import com.my.baffinrpc.core.cluster.Directory;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.ResultFactory;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.AbstractInvoker;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@ExtensionImpl(name = "failback",extension = HighAvailableStrategy.class)
public class FailbackHighAvailable extends AbstractHighAvailableStrategy {

    private final ConcurrentMap<Invocation,Invoker> failedMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    private static final int failbackDelayInMilliseconds = 3000;
    private final AtomicBoolean executorServiceStart = new AtomicBoolean(false);
    private static final Logger logger = Logger.getLogger(FailbackHighAvailable.class);


    @Override
    public <T> Result invoke(List<Invoker> invokers, ClusterInvoker clusterInvoker, Invocation invocation, Directory<T> directory, LoadBalanceStrategy loadBalanceStrategy) throws Exception {
        Invoker invoker = doSelect(invokers, loadBalanceStrategy);
        if (invoker != null)
        {
            try
            {
                Result result = invoker.invoke(invocation);
                failedMap.remove(invocation); //调用成功移除failedMap中的记录 如果本身map中没有 移除也没有影响
                return result;
            }catch (Exception e)
            {
                logger.info("Invoke failed for " + invocation.getInterfaceName() + "." +
                        invocation.getMethodName() + " due to " + e + ", try again in " + failbackDelayInMilliseconds + " milliseconds");
                addToFailedMap(invocation, clusterInvoker);
                return ResultFactory.newVoidResult();
            }
        }
        else
        {
            logger.info("invoker == null for " + invocation.getInterfaceName() + "." + invocation.getMethodName() +
                    ", try again in " + failbackDelayInMilliseconds + " milliseconds");
            addToFailedMap(invocation, clusterInvoker);
            return ResultFactory.newVoidResult();
        }
    }

    private void addToFailedMap(Invocation invocation, Invoker invoker)
    {
        failedMap.putIfAbsent(invocation,invoker);
        if (!executorServiceStart.get())
        {
            synchronized (this)
            {
                if (!executorServiceStart.get())
                {
                    scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            if (failedMap.size() > 0)
                            {
                                for(Map.Entry<Invocation,Invoker> entry: failedMap.entrySet()) {
                                    Invocation invocation = entry.getKey();
                                    Invoker invoker = entry.getValue();
                                    try {
                                        invoker.invoke(invocation);
                                    }catch (Exception e) {
                                        logger.error("Retry invoke failed again due to " + e + ", try again later");
                                    }
                                }
                            }
                        }
                    },failbackDelayInMilliseconds,failbackDelayInMilliseconds,TimeUnit.MILLISECONDS);
                    executorServiceStart.set(true);
                }
            }
        }
    }
}
