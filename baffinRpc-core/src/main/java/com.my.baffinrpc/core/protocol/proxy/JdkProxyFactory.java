package com.my.baffinrpc.core.protocol.proxy;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.invoker.WrapInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyFactory extends AbstractProxyFactory {
    @Override
    public <T> T getProxy(final Invoker invoker) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return proxyInvoke(invoker,method,args);
            }
        };
        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{invoker.getInterface()},invocationHandler);
    }

    @Override
    public <T> Invoker getInvoker(final T instance, final Class<?> interfaceClz, URL url) {
        return new WrapInvoker<>(url,interfaceClz, instance);
    }
}
