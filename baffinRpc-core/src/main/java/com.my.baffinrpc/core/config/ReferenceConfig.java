package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.cluster.*;
import com.my.baffinrpc.core.cluster.highavailable.FailoverHighAvailable;
import com.my.baffinrpc.core.cluster.highavailable.FirstAvailableHighAvailable;
import com.my.baffinrpc.core.cluster.highavailable.HighAvailableStrategy;
import com.my.baffinrpc.core.cluster.loadbalance.LoadBalanceStrategy;
import com.my.baffinrpc.core.cluster.loadbalance.RoundRobinLoadBalance;
import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.filter.ArgsSerializableCheckFilter;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.registry.RegistryService;
import com.my.baffinrpc.core.spi.ExtensionLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

import java.util.List;

public class ReferenceConfig<T> implements FactoryBean<T> {
    private Class<T> interfaceClz;
    //用默认proxy生成对virtualInvoker的动态代理
    private ProxyFactory proxyFactory;
    private RegistryConfig registryConfig;
    private volatile Invoker invoker;
    private static final Logger logger = Logger.getLogger(ReferenceConfig.class);
    private ClusterConfig clusterConfig;

    public ReferenceConfig()
    {
    }


    @Override
    public T getObject() throws Exception {
        RegistryService registryService = registryConfig.getRegistryService();
        List<URL> urls = registryService.find(interfaceClz.getName());
        logger.info("service urls initialized for " + interfaceClz.getName() + ", urls are " + urls.toString());
        Directory<T> directory = new DirectoryImpl<>(urls,interfaceClz);
        registryService.subscribe(interfaceClz.getName(),directory);
        HighAvailableStrategy highAvailableStrategy = ExtensionLoader.getExtension(HighAvailableStrategy.class,clusterConfig.getHighAvailableStrategy());
        LoadBalanceStrategy loadBalanceStrategy = ExtensionLoader.getExtension(LoadBalanceStrategy.class,clusterConfig.getLoadBalanceStrategy());
        invoker = new ClusterInvoker(directory,highAvailableStrategy,loadBalanceStrategy);
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

    public ClusterConfig getClusterConfig() {
        return clusterConfig;
    }

    public void setClusterConfig(ClusterConfig clusterConfig) {
        this.clusterConfig = clusterConfig;
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
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
