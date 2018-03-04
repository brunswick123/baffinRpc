package com.my.baffinrpc.core.communication.transport.netty;


import com.my.baffinrpc.core.communication.transport.ChannelFutureListener;
import com.my.baffinrpc.core.communication.transport.TransportChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.log4j.Logger;

public class NettyChannel implements TransportChannel {

    private final Channel channel;
    private static final Logger logger = Logger.getLogger(NettyChannel.class);

   NettyChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean isActive() {
        return channel != null && channel.isActive();
    }

    @Override
    public void close() {
       if (isActive()) {
           channel.close().awaitUninterruptibly();
           logger.info("channel closed for remote address " + channel.remoteAddress());
       }
    }

    @Override
    public void send(Object object) {
        channel.writeAndFlush(object);
    }

    @Override
    public void setCloseChannelListener(final ChannelFutureListener channelFutureListener) {
        channel.closeFuture().addListener(new io.netty.channel.ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (channelFutureListener != null && future.isSuccess())
                    channelFutureListener.operationComplete();
            }
        });
    }
}
