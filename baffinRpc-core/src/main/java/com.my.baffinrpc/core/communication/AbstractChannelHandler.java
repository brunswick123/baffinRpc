package com.my.baffinrpc.core.communication;

import org.apache.log4j.Logger;

public abstract class AbstractChannelHandler<T> implements ChannelHandler<T> {

    protected static final Logger logger = Logger.getLogger(AbstractChannelHandler.class);

    @Override
    public void exceptionCaught(Channel channel, Throwable cause) throws Exception {
        logger.error("Channel exception occurred " + cause + ",try to channelClosed channel",cause);
        channel.close();
    }

    @Override
    public void channelIdle(Channel channel) {

    }
}
