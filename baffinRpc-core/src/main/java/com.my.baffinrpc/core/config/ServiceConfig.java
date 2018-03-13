package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.common.serialization.SerializeType;
import com.my.baffinrpc.core.filter.ExceptionFilter;
import com.my.baffinrpc.core.filter.ResultSerializableCheckFilter;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.JavaassistProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.JdkProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.registry.RegistryService;
import com.my.baffinrpc.core.spi.ExtensionLoader;
import com.my.baffinrpc.core.util.NetworkUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

import static com.my.baffinrpc.core.common.constant.DefaultConfig.PROTOCOL_PORT_PLACE_HOLDER;

public class ServiceConfig<T> implements InitializingBean{

    private Class<?> interfaceClz;
    private T serviceRef;
    private List<MethodConfig> methodConfigs;
    private ProtocolConfig protocolConfig;
    private RegistryConfig registryConfig;
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
        Protocol protocol = null;
        if (protocolConfig.getPort() != PROTOCOL_PORT_PLACE_HOLDER)
            protocol = new ProtocolImpl(protocolConfig.getPort(),protocolConfig.getTransport(),protocolConfig.getMessage());
        else
            protocol = new ProtocolImpl(protocolConfig.getTransport(),protocolConfig.getMessage());
        FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(protocol);
        filterWrapProtocol.addFilter(new ExceptionFilter());
        filterWrapProtocol.addFilter(new ResultSerializableCheckFilter());
        URL url = URL.buildURL(interfaceClz.getName(), NetworkUtil.getLocalHostAddress(),protocol.getPort(),
                protocolConfig.getTransport(),protocolConfig.getSerialization(),protocolConfig.getMessage(),methodConfigs);
        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class,protocolConfig.getProxy());
        Exporter exporter = filterWrapProtocol.export(proxyFactory.getInvoker(serviceRef,interfaceClz, url));

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
                logger.info("Run shutdown hook...");
                if (protocolConfig!= null)
                {
                    /*Protocol protocol = protocolConfig.getProtocol();
                    if (protocol != null)
                        protocol.destroy();*/
                }
            }
        });
    }
}
