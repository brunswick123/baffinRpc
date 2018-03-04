package com.my.baffinrpc.core.communication.transport.mina;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.TransportClient;
import com.my.baffinrpc.core.communication.transport.TransportFactory;
import com.my.baffinrpc.core.communication.transport.TransportServer;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.Response;

public class MinaTransportFactory implements TransportFactory {
    @Override
    public TransportServer newTransportServer(String host, int port, ChannelHandler<Request> channelHandler, Codec<Message> codec) {
        return new MinaTransportServer(host, port, channelHandler, codec);
    }

    @Override
    public TransportClient newTransportClient(String host, int port, ChannelHandler<Response> channelHandler, Codec<Message> codec) {
        return new MinaTransportClient(host, port, channelHandler, codec);
    }
}
