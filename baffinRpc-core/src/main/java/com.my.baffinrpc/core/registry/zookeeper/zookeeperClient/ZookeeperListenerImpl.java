package com.my.baffinrpc.core.registry.zookeeper.zookeeperClient;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.registry.NotifyListener;

import java.util.ArrayList;
import java.util.List;

public class ZookeeperListenerImpl implements ZookeeperListener {

    private final NotifyListener notifyListener;

    public ZookeeperListenerImpl(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    @Override
    public void notify(List<String> newDataStringList) {
        List<URL> urls = new ArrayList<>();
        for (String string : newDataStringList)
        {
            urls.add(URL.buildURLFromString(string));
        }
        notifyListener.notify(urls);
    }
}
