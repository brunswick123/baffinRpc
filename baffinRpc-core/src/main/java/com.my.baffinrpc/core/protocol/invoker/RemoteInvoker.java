package com.my.baffinrpc.core.protocol.invoker;


import com.my.baffinrpc.core.common.RpcContext;
import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.future.FutureAdapter;
import com.my.baffinrpc.core.common.future.ResponseFuture;
import com.my.baffinrpc.core.common.model.*;
import com.my.baffinrpc.core.communication.exchange.ExchangeClient;
import com.my.baffinrpc.core.communication.exchange.ExchangeClientChannelHandler;
import com.my.baffinrpc.core.communication.exchange.ExchangeClientImpl;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.filter.ExceptionFilter;
import com.my.baffinrpc.core.message.MessageFactory;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.base.BaseMessageCodec;
import com.my.baffinrpc.core.message.base.BaseMessageFactory;
import com.my.baffinrpc.core.protocol.FilterWrapProtocol;
import com.my.baffinrpc.core.protocol.ProtocolImpl;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.spi.ExtensionLoader;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class RemoteInvoker extends AbstractInvoker {

    private final ExchangeClient exchangeClient;

    private ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class, DefaultConfig.PROXY);
    private final MessageFactory messageFactory;
    private final ConcurrentHashMap<URL,Exporter> exportCallbackMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RemoteInvoker.class);


    public RemoteInvoker(URL url, Class<?> interfaceClz,TransportFactory transportFactory, MessageFactory messageFactory) {
        super(url,interfaceClz);
        this.messageFactory = messageFactory;
        exchangeClient = new ExchangeClientImpl(transportFactory.newTransportClient(url.getHost(),url.getPort(),
                (new ExchangeClientChannelHandler()),this.messageFactory.getMessageCodec()));
    }

    private void exportCallbackExporter(CallbackInfo callbackInfo,Invocation invocation)
    {
        //synchronized和双重非null检测 保证只export一次
        if (exportCallbackMap.get(invocation.getUrl()) == null) {
            synchronized (exportCallbackMap) {
                if (exportCallbackMap.get(invocation.getUrl()) == null) {
                    Object callbackInstance = invocation.getArgs()[callbackInfo.getMethodArgsIndex()];
                    Class<?> callbackInterface = callbackInfo.getCallbackInterface();
                    URL url = callbackInfo.getCallbackURL();
                    Invoker invoker = proxyFactory.getInvoker(callbackInstance, callbackInterface,url);
                    FilterWrapProtocol filterWrapProtocol = new FilterWrapProtocol(new ProtocolImpl(url.getPort(),url.getTransport(),url.getMessage()));
                    filterWrapProtocol.addFilter(new ExceptionFilter());
                    Exporter exporter = filterWrapProtocol.export(invoker);
                    if (exporter != null) {
                        exportCallbackMap.put(invocation.getUrl(), exporter);
                        logger.info(callbackInterface.getName() + " callback service exported successfully with url:" +url.buildString());
                    }
                }
            }
        }
    }

    @Override
    public Result invoke(Invocation invocation) throws Exception {
        //向invocation设置url
        if (invocation.getUrl() == null)
            invocation.setUrl(url);
        Request request = messageFactory.newRequest(invocation);
        if (invocation.getCallbackInfo() != null)
        {
            CallbackInfo callbackInfo = invocation.getCallbackInfo();
            exportCallbackExporter(callbackInfo,invocation);
            //回调参数不需要传送,设置成null,服务端会根据url中callbackInterface和callbackIndex生成代理对象作为回调参数
            int callbackIndex = callbackInfo.getMethodArgsIndex();
            invocation.getArgs()[callbackIndex] = null;
        }
        ResponseFuture<Result> responseFuture = exchangeClient.request(request);
        if (invocation.isOneWay())
        {
            RpcContext.getContext().setAsyncCall(false);
            RpcContext.getContext().setFuture(null);
            return ResultFactory.newVoidResult();

        }else if (invocation.isAsync())
        {
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
