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

public class JdkProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(final Invoker invoker) {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                boolean isOneWay = false;
                if (method.getReturnType() == Void.class)
                    isOneWay = true;
                Invocation invocation = new Invocation(invoker.getInterface().getName(),method.getName(),args,isOneWay);
                Result result = invoker.invoke(invocation);
                if (result == null)
                    throw new RPCFrameworkException("result == null");
                return result.recreateInvokeResult();
            }
        };
        return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{invoker.getInterface()},invocationHandler);
    }

    @Override
    public <T> Invoker getInvoker(final T instance, final Class<?> interfaceClz, URL url) {
        return new WrapInvoker<>(url,interfaceClz, instance);
    }
}
