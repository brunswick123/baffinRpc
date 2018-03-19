package com.my.baffinrpc.core.common.constant;

/***
 * 默认配置
 */
public class DefaultConfig {
    public static final String TRANSPORT = "netty";
    public static final String SERIALIZATION = "fst";
    public static final int MAX_POOL_SIZE = 20;
    public static final int SERVICE_DEFAULT_PORT = 9999;
    public static final String ZOOKEEPER_CLIENT= "zk";
    public static final String PROXY = "cglib";
    public static final String MESSAGE = "base";
    public static final String HIGH_AVAILABLE_STRATEGY = "failover";
    public static final String LOAD_BALANCE_STRATEGY = "roundRobin";
    public static final int PROTOCOL_PORT_PLACE_HOLDER = -1;
    public static final int FAILOVER_RETRY = 5;
    public static final int DEFAULT_WEIGHT = 50;
}
