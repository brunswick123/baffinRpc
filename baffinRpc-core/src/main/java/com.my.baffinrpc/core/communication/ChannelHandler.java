package com.my.baffinrpc.core.communication;


public interface ChannelHandler<T> {
    void received(Channel channel, T msg) throws Exception;
    void exceptionCaught(Channel channel, Throwable cause) throws Exception;
    void channelIdle(Channel channel);
}

