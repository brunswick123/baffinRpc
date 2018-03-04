package com.my.baffinrpc.core.protocol.rmi;

import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.protocol.AbstractProtocol;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.export.ExporterImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.invoker.WrapInvoker;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;

public abstract class AbstractProxyProtocol extends AbstractProtocol {

    private final ProxyFactory proxyFactory;

    private static final int DEFAULT_PORT_NO = 9998;

    protected AbstractProxyProtocol(ProxyFactory proxyFactory) {
        super(DEFAULT_PORT_NO);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Exporter export(Invoker invoker) throws RPCFrameworkException {
        if (!(invoker instanceof WrapInvoker))
            throw new RPCFrameworkException("exported non wrap invoker");
        doExport(proxyFactory.getProxy(invoker),invoker.getInterface(),invoker);
        return new ExporterImpl(invoker);
    }

    @Override
    public Invoker refer(URL url, Class<?> interfaceClz) {
        if (interfaceClz == null)
            throw new RPCFrameworkException("interfaceClz == null");
        return proxyFactory.getInvoker(doRefer(url,interfaceClz),interfaceClz,url);
    }

    protected abstract <T> void doExport(T instance, Class<?> interfaceClz, Invoker invoker);

    protected abstract <T> T doRefer(URL url, Class<?> interfaceClz);
}
