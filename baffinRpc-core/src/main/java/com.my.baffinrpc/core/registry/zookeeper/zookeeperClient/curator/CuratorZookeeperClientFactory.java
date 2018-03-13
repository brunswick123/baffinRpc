package com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.curator;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClient;
import com.my.baffinrpc.core.registry.zookeeper.zookeeperClient.ZookeeperClientFactory;

@ExtensionImpl(name = "curator",extension = ZookeeperClientFactory.class)
public class CuratorZookeeperClientFactory implements ZookeeperClientFactory {
    @Override
    public ZookeeperClient newZookeeperClient(String zookeeperAddress) {
        return new CuratorClient(zookeeperAddress);
    }
}
