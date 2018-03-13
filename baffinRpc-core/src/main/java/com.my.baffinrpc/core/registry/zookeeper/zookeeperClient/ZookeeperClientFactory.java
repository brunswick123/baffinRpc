package com.my.baffinrpc.core.registry.zookeeper.zookeeperClient;

import com.my.baffinrpc.core.annotation.Extension;

@Extension
public interface ZookeeperClientFactory
{
    ZookeeperClient newZookeeperClient(String zookeeperAddress);
}
