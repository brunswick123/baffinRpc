package com.my.baffinrpc.core.registry.zookeeper;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.util.StringUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.*;


public class CuratorClient implements ZookeeperClient {

    private final CuratorFramework client;
    private static final String ENCODING = "UTF-8";
    private static final Logger logger = Logger.getLogger(CuratorClient.class);

    public CuratorClient(String zookeeperAddress)
    {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
        client.start();
    }


    @Override
    public void createPersistence(String path, String nodeData, boolean sequence) {
        byte[] bytes = StringUtil.encodeString(nodeData,ENCODING);
        try {
            if (sequence)
                client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, bytes);
            else
                client.create().withMode(CreateMode.PERSISTENT).forPath(path, bytes);
        }catch (Exception e)
        {
            logger.error("create zkNode " + path + " failed due to " + e);
        }
    }

    @Override
    public void createEphemeral(String path, String nodeData, boolean sequence) {
        byte[] bytes = StringUtil.encodeString(nodeData,ENCODING);
        try {
            if (sequence)
                client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, bytes);
            else
                client.create().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
        }catch (KeeperException.NodeExistsException e)
        {
            logger.warn("zkNode " + path + " already existed, delete it and create again");
            deleteAndRecreateEphemeral(path,bytes,sequence);
        }catch (Exception e)
        {
            logger.error("create zkNode " + path + " failed due to " + e);
        }
    }

    private void deleteAndRecreateEphemeral(String path, byte[] bytes, boolean sequence)
    {
        try {
            client.delete().forPath(path);
            if (sequence)
                client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, bytes);
            else
                client.create().withMode(CreateMode.EPHEMERAL).forPath(path, bytes);
        }catch (Exception e)
        {
            logger.error("recreate zkNode " + path + " failed due to " + e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            client.delete().forPath(path);
        } catch (Exception e) {
            logger.error("delete zkNode " + path + " failed due to " + e);
        }
    }

    @Override
    public boolean exists(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            logger.error("check exist of zkNode " + path + " failed due to " + e);
            return false;
        }
    }


    @Override
    public List<String> getChildrenData(String parentPath) {
        try {
            List<String> dataList = new ArrayList<>();
            List<String> childrenPaths = client.getChildren().forPath(parentPath);
            for (String path : childrenPaths)
            {
                dataList.add(StringUtil.decodeString(client.getData().forPath(parentPath + "/" + path),ENCODING));
            }
            return dataList;
        } catch (Exception e) {
            throw new RPCFrameworkException("get children data for zkNode " + parentPath + " failed due to " + e);
        }
    }

    @Override
    public void addListener(final String path, final ZookeeperListener zookeeperListener) {
        logger.debug("add listener for path " + path);
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,path,true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            private boolean initialized = false;
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData childData = event.getData();
                if (event.getType() == INITIALIZED)
                {
                    initialized = true;
                }
                if (initialized && childData != null)
                {
                    if(event.getType() == CHILD_ADDED || event.getType() == CHILD_UPDATED || event.getType() == CHILD_REMOVED) {
                        zookeeperListener.notify(getChildrenData(path));
                    }
                }
            }
        });
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            logger.error("add listener to ZkNode " + path + " failed due to " + e);
        }
    }



    @Override
    public void connect() throws InterruptedException {
        client.blockUntilConnected(10, TimeUnit.SECONDS);
    }


}
