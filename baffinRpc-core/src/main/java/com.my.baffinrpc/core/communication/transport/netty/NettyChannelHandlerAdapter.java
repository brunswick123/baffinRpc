package com.my.baffinrpc.core.communication.transport.netty;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyChannelHandlerAdapter<T extends Message> extends SimpleChannelInboundHandler<Message> {

    private final ChannelHandler<T> channelHandler;

    public NettyChannelHandlerAdapter(ChannelHandler<T> channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    @SuppressWarnings("unchecked") //T type must implement Message
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        channelHandler.received(new NettyChannel(ctx.channel()),(T)msg);
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        if (evt instanceof IdleStateEvent)
            channelHandler.channelIdle(new NettyChannel(ctx.channel()));
        else
            super.userEventTriggered(ctx,evt);
    }

    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) throws Exception {
        channelHandler.exceptionCaught(new NettyChannel(ctx.channel()), cause);
    }
}
