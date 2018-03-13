package com.my.baffinrpc.core.communication.exchange;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.communication.transport.TransportServer;
import com.my.baffinrpc.core.communication.transport.mina.MinaTransportServer;
import com.my.baffinrpc.core.communication.transport.netty.NettyTransportServer;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.base.BaseMessageCodec;

public class ExchangeServerImpl implements ExchangeServer {

    private final TransportServer transportServer;

    public ExchangeServerImpl(URL url, ChannelHandler<Request> channelHandler, TransportFactory transportFactory, Codec<Message> codec) {
        this.transportServer
                = transportFactory.newTransportServer(url.getHost(),url.getPort(), channelHandler, codec);
    }

    @Override
    public boolean bind() {
        return transportServer.bind();
    }

}
