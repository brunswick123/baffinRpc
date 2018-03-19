package com.my.baffinrpc.core.cluster.loadbalance;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

import java.util.List;

/***
 * 随机带权重的负载均衡策略
 */
@ExtensionImpl(name = "random", extension = LoadBalanceStrategy.class)
public class RandomLoadBalance extends AbstractLoadBalanceStrategy {
    @Override
    protected Invoker doSelect(List<Invoker> invokers) {
        boolean sameWeight = true;
        int weightSum = 0;
        for (int i = 0; i < invokers.size(); i++)
        {
            weightSum = weightSum + invokers.get(i).getUrl().getWeight();
            if (i > 0 && sameWeight)
                if (invokers.get(i).getUrl().getWeight() != invokers.get(i - 1).getUrl().getWeight())
                {
                    sameWeight = false;
                }
        }
        if (sameWeight)
        {
            int randomIndex = (int)(Math.random() * invokers.size());
            return invokers.get(randomIndex);
        }else
        {
            for (Invoker invoker : invokers) {
                weightSum = weightSum + invoker.getUrl().getWeight();

            }
            int weightSunRandom = (int) (Math.random() * weightSum);
            int temp = 0;
            for (Invoker invoker : invokers) {
                temp = temp + invoker.getUrl().getWeight();
                if (temp >= weightSunRandom)
                    return invoker;
            }
            return null;
        }
    }
}
