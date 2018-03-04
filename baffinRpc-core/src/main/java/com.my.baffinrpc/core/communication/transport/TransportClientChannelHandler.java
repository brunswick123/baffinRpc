package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.communication.AbstractChannelHandler;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.MessageFactory;
import com.my.baffinrpc.core.message.Response;
import com.my.baffinrpc.core.message.base.BaseMessageFactory;

public class TransportClientChannelHandler extends AbstractChannelHandler<Response> {

    private final ChannelHandler<Response> exchangeChannelHandler;
    private MessageFactory messageFactory = new BaseMessageFactory();

    public TransportClientChannelHandler(ChannelHandler<Response> exchangeChannelHandler) {
        this.exchangeChannelHandler = exchangeChannelHandler;
    }

    @Override
    public void received(Channel channel, Response msg) throws Exception {
        exchangeChannelHandler.received(channel, msg);
    }

    @Override
    public void channelIdle(Channel channel) {
        logger.info("client channel idle, send heart beat");
        channel.send(messageFactory.newHeartBeatRequest());
    }
}
