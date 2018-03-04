package com.my.baffinrpc.core.protocol.invoker;


import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.URL;

public interface Invoker {
    /***
     * 调用
     * @param invocation 调用的相关信息
     * @return 封装的调用结果
     * @throws Throwable
     */
    Result invoke(Invocation invocation) throws Exception;
    URL getUrl();
    Class<?> getInterface();
    boolean isAvailable();
    void destroy();
}
