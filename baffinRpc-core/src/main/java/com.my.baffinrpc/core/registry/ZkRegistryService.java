package com.my.baffinrpc.core.registry;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.registry.zookeeper.CuratorClient;
import com.my.baffinrpc.core.registry.zookeeper.ZkClient;
import com.my.baffinrpc.core.registry.zookeeper.ZookeeperClient;
import com.my.baffinrpc.core.registry.zookeeper.ZookeeperListenerImpl;

import java.util.ArrayList;
import java.util.List;

public class ZkRegistryService implements RegistryService {

    private ZookeeperClient zookeeperClient;
    private static final String PATH_PREFIX = "/baffinRpc-";

    public ZkRegistryService(String address)
    {
        zookeeperClient = new ZkClient(address);
    }

    @Override
    public void register(String serviceName, URL url) {
        final String servicePath = PATH_PREFIX + serviceName;
        if (!zookeeperClient.exists(servicePath))
        {
            zookeeperClient.createPersistence(servicePath,"",false);
        }
        zookeeperClient.createEphemeral(servicePath + "/" + getSubPath(url), url.buildString(),false);
    }

    @Override
    public void unregister(String serviceName) {
        zookeeperClient.delete(PATH_PREFIX + serviceName);
    }

    /***
     * 订阅zookeeper数据变化监听器
     * 对于临时节点,当临时节点所属的zookeeperClient断开和zookeeper连接时,临时节点并不会马上删除,
     * 要等zookeeper中maxSessionTimeout过后,连接判断超时,临时节点才会被删除
     * 用于服务发现时,服务下线不可用后,服务地址的变化要maxSessionTimeout后才会被通知到
     *
     */
    @Override
    public void subscribe(String serviceName, NotifyListener listener) {
        zookeeperClient.addListener(PATH_PREFIX + serviceName, new ZookeeperListenerImpl(listener));
    }

    @Override
    public List<URL> find(String serviceName) {
        List<URL> urls = new ArrayList<>();
        for (String string : zookeeperClient.getChildrenData(PATH_PREFIX + serviceName))
        {
            urls.add(URL.buildURLFromString(string));
        }
        return urls;
    }

    private String getSubPath(URL url)
    {
        return url.getHost()+":" + url.getPort();
    }
}
