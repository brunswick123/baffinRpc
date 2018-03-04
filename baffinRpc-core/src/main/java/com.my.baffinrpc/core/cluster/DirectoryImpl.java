package com.my.baffinrpc.core.cluster;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryImpl<T> implements Directory<T> {

    private Protocol protocol;
    private List<URL> urls;
    private volatile Map<URL,Invoker> invokerMap;
    private Class<T> interfaceClz;
    private static final Logger logger = Logger.getLogger(DirectoryImpl.class);

    public DirectoryImpl(Protocol protocol, List<URL> urls, Class<T> interfaceClz) {
        this.protocol = protocol;
        this.interfaceClz = interfaceClz;
        this.invokerMap = new HashMap<>();
        this.urls = urls;
        if (urls != null && urls.size() > 0)
        {
            for (URL url : urls)
            {
                Invoker invoker = protocol.refer(url,interfaceClz);
                invokerMap.put(url,invoker);
            }
        }
    }


    @Override
    public void notify(List<URL> newUrls) {
        Map<URL,Invoker> newInvokerMap = new HashMap<>();
        if (newUrls != null) {
            urls = newUrls;
        }else
            urls = new ArrayList<>();
        if (urls.size() > 0)
        {
            for (URL url : urls)
            {
                Invoker existedInvoker = invokerMap.get(url);
                if (existedInvoker == null)
                {
                    Invoker invoker = protocol.refer(url,interfaceClz);
                    newInvokerMap.put(url,invoker);
                }
                else
                    newInvokerMap.put(url,existedInvoker);
            }
        }
        invokerMap = newInvokerMap;
        logger.info("service urls changed for " + interfaceClz.getName() + ", new urls are " + urls.toString());
    }

    @Override
    public List<Invoker> getInvokers() {
        return new ArrayList<>(invokerMap.values());
    }

    @Override
    public Class<T> getInterface() {
        return interfaceClz;
    }
}
