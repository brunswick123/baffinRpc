package com.my.baffinrpc.core.registry.zookeeper;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

public class ZkClient implements ZookeeperClient {

    private org.I0Itec.zkclient.ZkClient client;
    private static final Logger logger = Logger.getLogger(ZkClient.class);

    public ZkClient(String zookeeperAddress)
    {
        client = new org.I0Itec.zkclient.ZkClient(zookeeperAddress);
    }

    @Override
    public void createPersistence(String path, String nodeData, boolean sequence) {
        if (sequence)
            client.createPersistentSequential(path,nodeData);
        else
            client.createPersistent(path,nodeData);
    }

    @Override
    public void createEphemeral(String path, String nodeData, boolean sequence) {
        try {
            if (sequence)
                client.createEphemeralSequential(path, nodeData);
            else
                client.createEphemeral(path, nodeData);
        }catch (ZkNodeExistsException e)
        {
            logger.warn("zkNode " + path + " already existed, delete it and create again");
            deleteAndRecreateEphemeral(path, nodeData, sequence);
        }
    }

    private void deleteAndRecreateEphemeral(String path, String nodeData, boolean sequence)
    {
        client.delete(path);
        if (sequence)
            client.createEphemeralSequential(path, nodeData);
        else
            client.createEphemeral(path, nodeData);
    }

    @Override
    public void delete(String path) {
        client.delete(path);
    }

    @Override
    public boolean exists(String path) {
        return client.exists(path);
    }

    @Override
    public List<String> getChildrenData(String parentPath) {
        List<String> childData = new ArrayList<>();
        List<String> childPaths = client.getChildren(parentPath);
        for (String childPath : childPaths)
        {
            childData.add((String)client.readData(parentPath + "/" + childPath));
        }
        return childData;
    }

    @Override
    public void addListener(String path, final ZookeeperListener zookeeperListener) {
        client.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                zookeeperListener.notify(getChildrenData(parentPath));
            }
        });
    }

    @Override
    public void connect() throws InterruptedException {
    }

}
