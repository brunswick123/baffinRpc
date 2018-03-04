package com.my.baffinrpc.core.communication.transport.netty;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.AbstractTransportClient;
import com.my.baffinrpc.core.communication.transport.TransportChannel;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class NettyTransportClient extends AbstractTransportClient {

    private Bootstrap bootstrap;
    private EventLoopGroup workGroup;

    public NettyTransportClient(String host, int port, ChannelHandler<Response> clientTransportChannelHandler, Codec<Message> codec) {
        super(host, port, clientTransportChannelHandler,codec);
    }

    @Override
    protected void shutdownGracefully() {
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

    @Override
    protected TransportChannel doConnect() {
        ChannelFuture channelFuture = bootstrap.connect(host,port).awaitUninterruptibly();
        if (channelFuture.isSuccess()) {
            return new NettyChannel(channelFuture.channel());
        }
        return null;
    }

    @Override
    protected void init(final ChannelHandler<Response> channelHandler, final Codec<Message> codec) {
        workGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(0,WRITE_IDLE_INTERVAL_SECONDS, 0, TimeUnit.SECONDS))
                        .addLast(new NettyMessageCodecAdapter(codec)).addLast(new NettyChannelHandlerAdapter<>(channelHandler));
            }
        });
    }

}
