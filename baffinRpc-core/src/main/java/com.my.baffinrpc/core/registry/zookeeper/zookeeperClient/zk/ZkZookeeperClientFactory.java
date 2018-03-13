package com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.zk;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClient;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClientFactory;

@ExtensionImpl(name = "zk",extension = ZookeeperClientFactory.class)
public class ZkZookeeperClientFactory implements ZookeeperClientFactory {
    @Override
    public ZookeeperClient newZookeeperClient(String zookeeperAddress) {
        return new ZkClient(zookeeperAddress);
    }
}
