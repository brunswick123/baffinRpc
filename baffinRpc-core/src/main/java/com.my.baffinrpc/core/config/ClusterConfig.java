package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.common.constant.DefaultConfig;

public class ClusterConfig {
    private final String highAvailableStrategy;
    private final String loadBalanceStrategy;
    private final int failoverRetry;

    public ClusterConfig(Builder builder)
    {
        this.highAvailableStrategy = builder.highAvailableStrategy;
        this.loadBalanceStrategy = builder.loadBalanceStrategy;
        this.failoverRetry = builder.failoverRetry;
    }

    public String getHighAvailableStrategy() {
        return highAvailableStrategy;
    }


    public String getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public int getFailoverRetry() {
        return failoverRetry;
    }

    public static class Builder
    {
        private String highAvailableStrategy;
        private String loadBalanceStrategy;
        private int failoverRetry;

        public Builder()
        {
            this.highAvailableStrategy = DefaultConfig.HIGH_AVAILABLE_STRATEGY;
            this.loadBalanceStrategy = DefaultConfig.LOAD_BALANCE_STRATEGY;
            this.failoverRetry = DefaultConfig.FAILOVER_RETRY;
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

        public Builder failoverRetry(int failoverRetry)
        {
            this.failoverRetry = failoverRetry;
            return this;
        }



        public ClusterConfig build()
        {
            return new ClusterConfig(this);
        }
    }
}
