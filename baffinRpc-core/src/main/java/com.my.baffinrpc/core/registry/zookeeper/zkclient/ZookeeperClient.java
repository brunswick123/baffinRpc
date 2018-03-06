package com.my.baffinrpc.core.registry.zookeeper.zkclient;

import com.my.baffinrpc.core.annotation.Extension;

import java.util.List;

@Extension
public interface ZookeeperClient{
    void createPersistence(String path, String nodeData, boolean sequence);
    void createEphemeral(String path, String nodeData, boolean sequence);
    void delete(String path);
    boolean exists(String path);
    List<String> getChildrenData(String parentPath);
    void addListener(String path, ZookeeperListener zookeeperListener);
    void connect() throws InterruptedException;

}
