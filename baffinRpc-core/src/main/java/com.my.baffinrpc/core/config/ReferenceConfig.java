package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.cluster.*;
import com.my.baffinrpc.core.cluster.highavailable.FirstAvailableHighAvailable;
import com.my.baffinrpc.core.cluster.loadbalance.RoundRobinLoadBalance;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.filter.ArgsSerializableCheckFilter;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.registry.RegistryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

public class ReferenceConfig<T> implements FactoryBean<T> {
    private Class<T> interfaceClz;
    private ProxyFactory proxyFactory = new CglibProxyFactory();
    private RegistryConfig registryConfig;
    private Cluster cluster = new ClusterImpl();
    private Protocol protocol = new ProtocolImpl();
    private volatile Invoker invoker;
    private static final Logger logger = Logger.getLogger(ReferenceConfig.class);

    public ReferenceConfig()
    {
        //todo
        FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(new ProtocolImpl());
        filterWrapProtocol.addFilter(new ArgsSerializableCheckFilter());
        protocol = filterWrapProtocol;
    }


    @Override
    public T getObject() throws Exception {
        RegistryService registryService = registryConfig.getRegistryService();
        List<URL> urls = registryService.find(interfaceClz.getName());
        logger.info("service urls initialized for " + interfaceClz.getName() + ", urls are " + urls.toString());
        Directory<T> directory = new DirectoryImpl<>(protocol,urls,interfaceClz);
        registryService.subscribe(interfaceClz.getName(),directory);
        invoker = cluster.createVirtualInvoker(directory,new FirstAvailableHighAvailable(), new RoundRobinLoadBalance());
        registerShutdownHook();
        return proxyFactory.getProxy(invoker);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceClz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<?> getInterfaceClz() {
        return interfaceClz;
    }

    public void setInterfaceClz(Class<T> interfaceClz) {
        this.interfaceClz = interfaceClz;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    private void registerShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                if (invoker != null)
                {
                    logger.info("run client shut down ....");
                    invoker.destroy();
                }
            }
        });
    }
}
