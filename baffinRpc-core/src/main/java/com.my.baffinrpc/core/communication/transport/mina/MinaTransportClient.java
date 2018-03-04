package com.my.baffinrpc.core.communication.transport.mina;


import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.AbstractTransportClient;
import com.my.baffinrpc.core.communication.transport.TransportChannel;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Response;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MinaTransportClient extends AbstractTransportClient {
    private static final Logger logger = Logger.getLogger(MinaTransportClient.class);

    private NioSocketConnector connector;

    public MinaTransportClient(String host, int port, ChannelHandler<Response> channelHandler, Codec<Message> codec) {
        super(host,port, channelHandler,codec);
    }


    @Override
    protected TransportChannel doConnect() {
        ConnectFuture future = connector.connect(new InetSocketAddress(host,port));
        future.awaitUninterruptibly();
        if (future.isConnected()) {
            return new MinaChannel(future.getSession());
        }else
        {
            return null;
        }
    }

    @Override
    protected void init(ChannelHandler<Response> channelHandler, Codec<Message> codec) {
        connector = new NioSocketConnector();
        connector.getSessionConfig().setWriterIdleTime(WRITE_IDLE_INTERVAL_SECONDS);
        connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MinaMessageCodecAdapter(codec)));
        connector.setHandler(new MinaIoHandlerAdapter<>(channelHandler));
    }

    @Override
    protected void shutdownGracefully() {
        if(connector != null)
            connector.dispose();
    }


}
