package com.my.baffinrpc.core.protocol.invoker;


import com.my.baffinrpc.core.common.RpcContext;
import com.my.baffinrpc.core.common.future.FutureAdapter;
import com.my.baffinrpc.core.common.future.ResponseFuture;
import com.my.baffinrpc.core.common.model.*;
import com.my.baffinrpc.core.communication.exchange.ExchangeClient;
import com.my.baffinrpc.core.communication.exchange.ExchangeClientChannelHandler;
import com.my.baffinrpc.core.communication.exchange.ExchangeClientImpl;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.communication.transport.mina.MinaTransportClient;
import com.my.baffinrpc.core.communication.transport.netty.NettyTransportClient;
import com.my.baffinrpc.core.communication.transport.netty.NettyTransportFactory;
import com.my.baffinrpc.core.filter.ExceptionFilter;
import com.my.baffinrpc.core.message.MessageFactory;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.base.BaseMessageCodec;
import com.my.baffinrpc.core.message.base.BaseMessageFactory;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.Protocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class RemoteInvoker extends AbstractInvoker {

    private final ExchangeClient exchangeClient;

    private ProxyFactory proxyFactory = new CglibProxyFactory();
    private MessageFactory messageFactory = new BaseMessageFactory();

    private final ConcurrentHashMap<URL,Exporter> exportCallbackMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RemoteInvoker.class);
    private static final int DEFAULT_CALLBACK_PORT = 9800;


    public RemoteInvoker(URL url, Class<?> interfaceClz,TransportFactory transportFactory) {
        super(url,interfaceClz);
        exchangeClient = new ExchangeClientImpl(transportFactory.newTransportClient(url.getHost(),url.getPort(),
                (new ExchangeClientChannelHandler()),new BaseMessageCodec()));
    }

    @Override
    public Result invoke(Invocation invocation) throws Exception {
        if (invocation.getUrl() == null)
        {
            invocation.setUrl(url);
            invocation.setFromURL(url);
        }
        Request request = messageFactory.newRequest(invocation);
        if (invocation.isCallback())
        {
            CallbackInfo callbackInfo = invocation.getCallbackInfo();
            //synchronized和双重非null检测 保证只export一次
            if (exportCallbackMap.get(invocation.getUrl()) == null) {
                synchronized (exportCallbackMap) {
                    if (exportCallbackMap.get(invocation.getUrl()) == null) {
                        Object callbackInstance = invocation.getArgs()[callbackInfo.getMethodArgsIndex()];
                        Class<?> callbackInterface = callbackInfo.getCallbackInterface();
                        URL url = new URL(callbackInterface.getName(),"127.0.0.1",DEFAULT_CALLBACK_PORT);
                        Invoker invoker = proxyFactory.getInvoker(callbackInstance, callbackInterface,url);
                        FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(new ProtocolImpl());
                        filterWrapProtocol.addFilter(new ExceptionFilter());
                        Exporter exporter = filterWrapProtocol.export(invoker);
                        if (exporter != null) {
                            exportCallbackMap.put(invocation.getUrl(), exporter);
                            logger.info(callbackInterface.getName() + " callback service exported successfully with url:" +url.buildString());
                        }
                    }
                }
            }
            //回调参数不需要传送,设置成null,服务端会根据url中callbackInterface和callbackIndex生成代理对象作为回调参数
            int callbackIndex = callbackInfo.getMethodArgsIndex();
            invocation.getArgs()[callbackIndex] = null;
        }
        ResponseFuture<Result> responseFuture = exchangeClient.request(request);
        if (invocation.isOneWay())
        {
            RpcContext.getContext().setFuture(null);
            return ResultFactory.newVoidResult();

        }else if (invocation.isAsync())
        {
            //todo atomic
            RpcContext.getContext().setAsyncCall(true);
            RpcContext.getContext().setFuture(new FutureAdapter(responseFuture));
            return ResultFactory.newVoidResult();
        }else
        {
            RpcContext.getContext().setAsyncCall(false);
            RpcContext.getContext().setFuture(null);
            return responseFuture.get();
        }
    }

    @Override
    public boolean isAvailable() {
        return exchangeClient.isActive();
    }

    @Override
    public void destroy() {
        exchangeClient.close();
    }


}
