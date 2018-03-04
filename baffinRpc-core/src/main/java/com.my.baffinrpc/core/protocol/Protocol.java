package com.my.baffinrpc.core.protocol;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.invoker.Invoker;


public interface Protocol {
    /***
     * 发布服务
     * @param invoker
     * @return
     * @throws RPCFrameworkException
     */
    Exporter export(Invoker invoker) throws RPCFrameworkException;
    Invoker refer(URL url, Class<?> interfaceClz);

    /***
     * 获取协议默认端口
     * @return 协议默认端口
     */
    int getPort();

    /***
     *
     */
    void destroy();
}
