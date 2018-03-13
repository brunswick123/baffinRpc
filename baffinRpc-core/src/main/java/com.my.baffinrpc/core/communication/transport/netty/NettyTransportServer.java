package com.my.baffinrpc.core.communication.transport.netty;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.AbstractTransportServer;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;


public class NettyTransportServer extends AbstractTransportServer {

    private static final Logger logger = Logger.getLogger(NettyTransportServer.class);

    private ServerBootstrap serverBootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    public NettyTransportServer(String host, int port, ChannelHandler<Request> channelHandler, Codec<Message> codec) {
        super(host, port, channelHandler,codec);
    }


    public void init(final ChannelHandler<Request> channelHandler, final Codec<Message> codec)
    {
        bossGroup = new NioEventLoopGroup(2);
        workerGroup = new NioEventLoopGroup(10);
        serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(READ_IDLE_INTERVAL_SECONDS,0,0, TimeUnit.SECONDS))
                        .addLast(new NettyMessageCodecAdapter(codec)).addLast(new NettyChannelHandlerAdapter<>(channelHandler));
            }
        }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        logger.info("Netty server init successfully");
    }

    protected void shutdownGracefully() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    @Override
    protected boolean doBind() {
        ChannelFuture channelFuture = serverBootstrap.bind(port);
        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                shutdownGracefully();
            }
        });
        channelFuture.awaitUninterruptibly();
        if (channelFuture.isSuccess()) {
            logger.info("Open Netty server successfully with port " + port);
            return true;
        }
        else {
            logger.error("Bind server to [" + host + ":" + port + "] failed due to " +  channelFuture.cause().getMessage());
            return false;
        }
    }
}
