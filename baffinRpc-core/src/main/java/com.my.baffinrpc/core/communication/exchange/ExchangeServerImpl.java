package com.my.baffinrpc.core.communication.exchange;

import com.my.baffinrpc.core.common.model.URL;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.communication.transport.TransportServer;
import com.my.baffinrpc.core.communication.transport.mina.MinaTransportServer;
import com.my.baffinrpc.core.communication.transport.netty.NettyTransportServer;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.base.BaseMessageCodec;

public class ExchangeServerImpl implements ExchangeServer {

    private final TransportServer transportServer;

    public ExchangeServerImpl(URL url, ChannelHandler<Request> channelHandler, TransportFactory transportFactory) {
        this.transportServer
                = transportFactory.newTransportServer(url.getHost(),url.getPort(), channelHandler,
                new BaseMessageCodec());
    }

    @Override
    public boolean bind() {
        return transportServer.bind();
    }

}
