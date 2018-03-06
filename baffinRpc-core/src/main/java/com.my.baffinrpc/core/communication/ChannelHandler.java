package com.my.baffinrpc.core.communication;


public interface ChannelHandler<T> {

    /***
     * 收到消息
     * @param channel
     * @param msg
     * @throws Exception
     */
    void received(Channel channel, T msg) throws Exception;
    void exceptionCaught(Channel channel, Throwable cause) throws Exception;
    void channelIdle(Channel channel);
}

