package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;


public abstract class AbstractTransportServer implements TransportServer {

    protected final String host;
    protected final int port;
    private static final int MAX_BIND_ATTEMPT_TIME = 3;
    protected static final int READ_IDLE_INTERVAL_SECONDS = 30;

    protected AbstractTransportServer(String host, int port, ChannelHandler<Request> channelHandler, Codec<Message> codec)
    {
        this.host = host;
        this.port = port;
        init(channelHandler,codec);
    }

    @Override
    public boolean bind(){
        int attemptCount = 0;
        while ( attemptCount < MAX_BIND_ATTEMPT_TIME)
        {
            if (doBind())
                return true;
            else
            {
                attemptCount++;
            }
        }
        shutdownGracefully();
        return false;
    }

    protected abstract boolean doBind();
    protected abstract void shutdownGracefully();
    protected abstract void init(ChannelHandler<Request> channelHandler, Codec<Message> codec);
}
