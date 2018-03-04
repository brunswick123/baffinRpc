package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.Response;

public interface TransportFactory {
    TransportServer newTransportServer(String host, int port, ChannelHandler<Request> channelHandler, Codec<Message> codec);
    TransportClient newTransportClient(String host, int port, ChannelHandler<Response> channelHandler, Codec<Message> codec);
}
