package com.my.baffinrpc.core.registry;

import com.my.baffinrpc.core.common.model.URL;

import java.util.List;

public interface RegistryService {
    void register(String serviceName, URL url);
    void unregister(String serviceName);
    void subscribe(String serviceName, NotifyListener listener);
    List<URL> find(String serviceName);
}
