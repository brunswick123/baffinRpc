package com.my.baffinrpc.core.protocol.proxy;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.invoker.Invoker;

public interface ProxyFactory {

    /***
     * 生成代理对象
     * 该代理对象会将对 被代理对象 的方法调用 委托给传入的invoker的invoke方法
     * @param invoker 被代理对象的方法调用将被封装成Invocation, 然后调用invoker的invoke(invocation)
     * @param <T> 被代理对象接口
     * @return 代理对象
     */
    <T> T getProxy(Invoker invoker);

    /***
     * 将真实服务包装成invoker
     * 对这个invoker的invoke(invocation)调用 将根据invocation中的方法信息
     * 去调用真实中对应的方法
     * @param instance 被包装的实际被调用者
     * @param interfaceClz 被包装服务的接口
     * @param url
     * @param <T> 被包装服务instance
     * @return 包装invoker
     */
    <T> Invoker getInvoker(T instance, Class<?> interfaceClz, URL url);
}
