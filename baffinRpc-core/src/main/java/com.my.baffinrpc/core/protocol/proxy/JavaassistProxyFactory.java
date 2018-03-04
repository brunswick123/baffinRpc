package com.my.baffinrpc.core.protocol.proxy;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;


public class JavaassistProxyFactory extends AbstractProxyFactory{
    @Override
    public <T> T getProxy(final Invoker invoker) {
        try {
            javassist.util.proxy.ProxyFactory proxyFactory = new javassist.util.proxy.ProxyFactory();
            proxyFactory.setSuperclass(invoker.getInterface());
            Class<T> clz = proxyFactory.createClass();
            final T newInstance = clz.newInstance();
            MethodHandler methodHandler = new MethodHandler() {
                @Override
                public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                    return proxyInvoke(invoker,thisMethod,args);
                }
            };
            ((ProxyObject) newInstance).setHandler(methodHandler);
            return newInstance;
        }catch (Exception e)
        {
            throw new RPCFrameworkException("failed to create proxy for " + invoker.getInterface().getName() + " due to " + e);
        }
    }
}
