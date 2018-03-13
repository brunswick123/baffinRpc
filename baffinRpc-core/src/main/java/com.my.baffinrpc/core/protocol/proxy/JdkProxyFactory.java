package com.my.baffinrpc.core.protocol.proxy;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.invoker.WrapInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@ExtensionImpl(name = "jdk",extension = ProxyFactory.class)
public class JdkProxyFactory extends AbstractProxyFactory {
    @Override
    @SuppressWarnings("unchecked") //代理生成的一定是T类型
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
