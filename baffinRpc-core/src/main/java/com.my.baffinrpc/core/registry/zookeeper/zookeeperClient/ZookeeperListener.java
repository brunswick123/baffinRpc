package com.my.baffinrpc.core.registry.zookeeper.zookeeperClient;

import java.util.List;

public interface ZookeeperListener {
    void notify(List<String> newDataStringList);
}
