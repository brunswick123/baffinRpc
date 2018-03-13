package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.cluster.Cluster;
import com.my.baffinrpc.core.common.constant.DefaultConfig;

public class ClusterConfig {
    private final String highAvailableStrategy;
    private final String loadBalanceStrategy;

    public ClusterConfig(Builder builder)
    {
        this.highAvailableStrategy = builder.highAvailableStrategy;
        this.loadBalanceStrategy = builder.loadBalanceStrategy;
    }

    public String getHighAvailableStrategy() {
        return highAvailableStrategy;
    }


    public String getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }


    public static class Builder
    {
        private String highAvailableStrategy;
        private String loadBalanceStrategy;

        public Builder()
        {
            this.highAvailableStrategy = DefaultConfig.HIGH_AVAILABLE_STRATEGY;
            this.loadBalanceStrategy = DefaultConfig.LOAD_BALANCE_STRATEGY;
        }

        public Builder highAvailableStrategy(String highAvailableStrategy)
        {
            this.highAvailableStrategy = highAvailableStrategy;
            return this;
        }

        public Builder loadBalanceStrategy(String loadBalanceStrategy)
        {
            this.loadBalanceStrategy = loadBalanceStrategy;
            return this;
        }

        public ClusterConfig build()
        {
            return new ClusterConfig(this);
        }
    }
}
