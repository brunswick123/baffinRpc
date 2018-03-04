package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.common.serialization.SerializeType;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.JavaassistProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.JdkProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.registry.RegistryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class ServiceConfig<T> implements InitializingBean{

    private Class<?> interfaceClz;
    private T serviceRef;
    private ProxyFactory proxyFactory = new CglibProxyFactory();
    private List<MethodConfig> methodConfigs;
    private ProtocolConfig protocolConfig;
    private RegistryConfig registryConfig;
    private static final String DEFAULT_URL = "127.0.0.1";
    private static final Logger logger = Logger.getLogger(ServiceConfig.class);

    public ServiceConfig()
    {
        registerShutdownHook();
    }

    public Class<?> getInterfaceClz() {
        return interfaceClz;
    }

    public void setInterfaceClz(Class<?> interfaceClz) {
        if (interfaceClz == null)
            throw new RPCFrameworkException("interfaceClz == null");
        this.interfaceClz = interfaceClz;
    }

    public T getServiceRef() {
        return serviceRef;
    }

    public void setServiceRef(T serviceRef) {
        if (serviceRef == null)
            throw new RPCFrameworkException("serviceRef == null");
        this.serviceRef = serviceRef;
    }

    public List<MethodConfig> getMethodConfigs() {
        return methodConfigs;
    }

    public void setMethodConfigs(List<MethodConfig> methodConfigs) {
        this.methodConfigs = methodConfigs;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Protocol protocol = protocolConfig.getProtocol();
        URL url = URL.buildURL(interfaceClz.getName(),DEFAULT_URL,protocol.getPort(), "netty",SerializeType.Jdk,methodConfigs);
        Exporter exporter = protocolConfig.getProtocol().export(proxyFactory.getInvoker(serviceRef,interfaceClz, url));
        if (exporter != null)
        {
            RegistryService registryService = registryConfig.getRegistryService();
            registryService.register(interfaceClz.getName(),url);
            logger.info(interfaceClz.getName() + " service exported successfully with URL[" + url.buildString() + "]");
        }
    }

    private void registerShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                logger.info("run shutdown hook...");
                if (protocolConfig!= null)
                {
                    Protocol protocol = protocolConfig.getProtocol();
                    if (protocol != null)
                        protocol.destroy();
                }
            }
        });
    }



}
