package com.my.baffinrpc.core.protocol.invoker;

import com.my.baffinrpc.core.common.exception.RPCBizException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.ResultFactory;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.registry.RegistryService;
import com.my.baffinrpc.core.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/***
 * 将实际的service包装成Invoker
 * 对该Invoker.invoke的调用会被转化成对被包装service方法的调用
 *
 * @param <T> 被包装service接口类
 */
public class WrapInvoker<T> extends AbstractInvoker {

    private final T wrappedServiceInstance;

    public WrapInvoker(URL url, Class<?> interfaceClz, T wrappedServiceInstance) {
        super(url,interfaceClz);
        this.wrappedServiceInstance = wrappedServiceInstance;
    }


    @Override
    public Result invoke(Invocation invocation) throws Exception {
        Object invokeResult = doInvoke(invocation);
        return ResultFactory.newNormalResult(invokeResult);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void destroy() {

    }

    private Object doInvoke(Invocation invocation) throws RPCBizException, InvocationTargetException, IllegalAccessException {
        Method method = ReflectUtil.findMethod(wrappedServiceInstance.getClass(),invocation.getMethodName());
        if (method == null)
            throw new RPCFrameworkException(invocation.getMethodName() + " method not found for " + wrappedServiceInstance.getClass());
        try
        {
            //todo 回调匿名内部类 会有IllegalArgumentException
            return method.invoke(wrappedServiceInstance,invocation.getArgs());
        }
        catch (InvocationTargetException e)
        {
            //属于业务异常,包装成RPCBizException后抛出
            throw new RPCBizException(e.getTargetException());
        }
    }
}
