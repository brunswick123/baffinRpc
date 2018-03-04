package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.communication.AbstractChannelHandler;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Request;

import java.util.concurrent.atomic.AtomicInteger;

public class TransportServerChannelHandler extends AbstractChannelHandler<Request> {

    private final ChannelHandler<Request> exchangeChannelHandler;
    private final AtomicInteger channelIdleCount = new AtomicInteger(0);
    private static final int MAX_IDLE_OCCUR_ALLOW = 3;

    public TransportServerChannelHandler(ChannelHandler<Request> exchangeChannelHandler) {
        this.exchangeChannelHandler = exchangeChannelHandler;
    }


    @Override
    public void received(Channel channel, Request msg) throws Exception {
        if (msg.isHeartBeat())
        {
            logger.info("heart beat from client");
            channelIdleCount.set(0);
        }
        else
            exchangeChannelHandler.received(channel, msg);
    }


    @Override
    public void channelIdle(Channel channel) {
        logger.info("server channel idle");
        int countSnap = channelIdleCount.incrementAndGet();
        if (countSnap > MAX_IDLE_OCCUR_ALLOW)
            channel.close();
    }
}
