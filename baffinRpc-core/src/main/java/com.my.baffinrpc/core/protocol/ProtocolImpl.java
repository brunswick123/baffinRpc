package com.my.baffinrpc.core.protocol;

import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.*;
import com.my.baffinrpc.core.common.threadpool.FixedSizeThreadPoolFactory;
import com.my.baffinrpc.core.common.threadpool.NamedThreadFactory;
import com.my.baffinrpc.core.common.threadpool.ThreadPoolFactory;
import com.my.baffinrpc.core.communication.AbstractChannelHandler;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.Server;
import com.my.baffinrpc.core.communication.exchange.ExchangeServerImpl;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.communication.transport.netty.NettyTransportFactory;
import com.my.baffinrpc.core.config.ProtocolConfig;
import com.my.baffinrpc.core.message.MessageFactory;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.base.BaseMessageFactory;
import com.my.baffinrpc.core.protocol.export.Exporter;
import com.my.baffinrpc.core.protocol.export.ExporterImpl;
import com.my.baffinrpc.core.protocol.invoker.Invoker;
import com.my.baffinrpc.core.protocol.invoker.InvokerTask;
import com.my.baffinrpc.core.protocol.invoker.RemoteInvoker;
import com.my.baffinrpc.core.protocol.proxy.CglibProxyFactory;
import com.my.baffinrpc.core.protocol.proxy.ProxyFactory;
import com.my.baffinrpc.core.spi.ExtensionLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class ProtocolImpl extends AbstractProtocol {

    private static final int DEFAULT_PORT_NO = 9999;
    private final Map<URL,Exporter> exporterMap = new ConcurrentHashMap<>();
    private final MessageFactory messageFactory;
    private final TransportFactory transportFactory;
    private ExchangeServerChannelHandler channelHandler = null; //延迟初始化 对于client并不需要ExchangeServerChannelHandler
    private ThreadPoolFactory threadPoolFactory = new FixedSizeThreadPoolFactory();

    public ProtocolImpl(String transport, String message) {
        this(DEFAULT_PORT_NO,transport, message);
    }

    public ProtocolImpl(int port, String transport, String message)
    {
        super(port);
        transportFactory = ExtensionLoader.getExtension(TransportFactory.class,transport);
        messageFactory = ExtensionLoader.getExtension(MessageFactory.class,message);
    }


    private ChannelHandler<Request> getExchangeServerChannelHandler()
    {
        if (channelHandler == null)
            synchronized (this) {
                if (channelHandler == null)
                    channelHandler = new ExchangeServerChannelHandler();
            }
        return channelHandler;
    }

    @Override
    public Exporter export(Invoker invoker) throws RPCFrameworkException {
        Server server = new ExchangeServerImpl(invoker.getUrl(),getExchangeServerChannelHandler(),transportFactory,messageFactory.getMessageCodec());
        if (server.bind())
        {
            Exporter exporter = new ExporterImpl(invoker);
            exporterMap.put(invoker.getUrl(),exporter);
            return new ExporterImpl(invoker);
        }
        else
            throw new RPCFrameworkException("Start server with port " + invoker.getUrl().getPort() + " failed");
    }

    @Override
    public Invoker refer(URL url, Class<?> interfaceClz) {
        return new RemoteInvoker(url, interfaceClz,transportFactory);
    }

    @Override
    public void destroy() {
        if (exporterMap.size() > 0) {
            for (Exporter exporter : exporterMap.values()) {
                exporter.unExport();
            }
            exporterMap.clear();
        }
        if (channelHandler != null)
            channelHandler.stop();

    }

    class ExchangeServerChannelHandler extends AbstractChannelHandler<Request>
    {
        private ProxyFactory proxyFactory = new CglibProxyFactory();
        private final ConcurrentHashMap<URL, Invoker> callbackInvokerMap = new ConcurrentHashMap<>();
        private final ExecutorService threadPool = threadPoolFactory.newThreadPool(DefaultConfig.MAX_POOL_SIZE,DefaultConfig.MAX_POOL_SIZE,
                new NamedThreadFactory("biz"));
        @Override
        public void received(Channel channel, Request msg) throws Exception {
            Invocation invocation = msg.getInvocation();
            URL url = invocation.getUrl();
            Exporter exporter = exporterMap.get(url);
            Result result;
            if (exporter != null) {
                Invoker invoker = exporter.getInvoker();
                try {
                    if (invocation.getCallbackInfo() != null) {
                        setCallbackArgToProxy(invocation);
                    }
                    InvokerTask invokerTask = new InvokerTask(invoker, invocation, channel, messageFactory,msg);
                    threadPool.submit(invokerTask);
                } catch (Throwable throwable) {
                    result = ResultFactory.newExceptionResult(throwable);
                    channel.send(messageFactory.newResponse(result, msg));
                }
            } else {
                result = ResultFactory.newExceptionResult(new RPCFrameworkException( "no exporter is found for " + url.buildString()));
                channel.send(messageFactory.newResponse(result, msg));
            }
        }

        private void setCallbackArgToProxy(Invocation invocation)
        {
            CallbackInfo callbackInfo = invocation.getCallbackInfo();
            URL callbackUrl = callbackInfo.getCallbackURL();
            Invoker callbackInvoker = getCallbackInvoker(callbackUrl, callbackInfo);
            invocation.getArgs()[callbackInfo.getMethodArgsIndex()] = proxyFactory.getProxy(callbackInvoker);
        }

        private Invoker getCallbackInvoker(URL callbackUrl, CallbackInfo callbackInfo) {
            Invoker callbackInvoker = callbackInvokerMap.get(callbackUrl);
            if (callbackInvoker == null) {
                Invoker newCallbackInvoker = refer(callbackUrl, callbackInfo.getCallbackInterface());
                callbackInvoker = callbackInvokerMap.putIfAbsent(callbackUrl, newCallbackInvoker);
                if (callbackInvoker == null)
                    callbackInvoker = newCallbackInvoker;
            }
            return callbackInvoker;
        }

        private void stop()
        {
            threadPool.shutdown();
        }
    }


}
