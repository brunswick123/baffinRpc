package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.communication.Channel;

/***
 * transport通道
 */
public interface TransportChannel extends Channel {
    /***
     * 设置通道关闭监听
     * @param channelFutureListener 通道监听器
     */
    void setCloseChannelListener(ChannelFutureListener channelFutureListener);
}
