package com.my.baffinrpc.core.protocol.proxy;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@ExtensionImpl(name = "cglib",extension = ProxyFactory.class)
public class CglibProxyFactory extends AbstractProxyFactory{
    @Override
    @SuppressWarnings("unchecked") //动态代理产生的对象是T类型
    public <T> T getProxy(final Invoker invoker) {
        return (T) Enhancer.create(invoker.getInterface(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return proxyInvoke(invoker,method,objects);
            }
        });
    }
}
