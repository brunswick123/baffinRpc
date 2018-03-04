package com.my.baffinrpc.core.protocol.rmi;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;

public class RmiProtocol extends AbstractProxyProtocol {
    public RmiProtocol(ProxyFactory proxyFactory) {
        super(proxyFactory);
    }

    @Override
    protected <T> void doExport(T instance, Class<?> interfaceClz, Invoker invoker) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setRegistryPort(port);
        rmiServiceExporter.setServiceName(interfaceClz.getSimpleName());
        rmiServiceExporter.setServiceInterface(interfaceClz);
        rmiServiceExporter.setService(instance);
        try
        {
            rmiServiceExporter.afterPropertiesSet();
        } catch (RemoteException e) {
            throw new RPCFrameworkException(e);
        }
    }

    @Override
    protected <T> T doRefer(URL url, Class<?> interfaceClz) {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://"+ url.getHost() + ":" + port + "/" + interfaceClz.getSimpleName());
        rmiProxyFactoryBean.setServiceInterface(interfaceClz);
        rmiProxyFactoryBean.setCacheStub(true);
        rmiProxyFactoryBean.setRefreshStubOnConnectFailure(true);
        rmiProxyFactoryBean.afterPropertiesSet();
        return (T)rmiProxyFactoryBean.getObject();
    }

    @Override
    public void destroy() {

    }
}
