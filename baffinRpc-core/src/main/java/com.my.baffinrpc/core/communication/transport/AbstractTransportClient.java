package com.my.baffinrpc.core.communication.transport;

import com.my.baffinrpc.core.common.exception.RPCNetworkException;
import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Response;
import org.apache.log4j.Logger;

public abstract class AbstractTransportClient implements TransportClient {

    protected final String host;
    protected final int port;
    protected TransportChannel transportChannel;
    private final ChannelHandler<Response> channelHandler;
    private final Codec<Message> codec;
    protected static final int WRITE_IDLE_INTERVAL_SECONDS = 25;
    private static final Logger logger = Logger.getLogger(AbstractTransportClient.class);

    public AbstractTransportClient(String host, int port, ChannelHandler<Response> channelHandler, Codec<Message> codec) {
        this.host = host;
        this.port = port;
        this.channelHandler = channelHandler;
        this.codec = codec;
    }

    @Override
    public void send(Object object) {
        transportChannel.send(object);
    }

    @Override
    public boolean isActive() {
        if (transportChannel != null)
            return transportChannel.isActive();
        else
        {
            connect();
            if (transportChannel != null)
                return transportChannel.isActive();
            else
                return false;
        }
    }

    @Override
    public void close() {
        if (transportChannel != null)
            transportChannel.close();
        shutdownGracefully();
    }

    @Override
    public void connect() {
        if (transportChannel == null || !transportChannel.isActive()) {
            init(channelHandler, codec);
            TransportChannel temp = doConnect();
            if (temp != null) {
                transportChannel = temp;
                transportChannel.setCloseChannelListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete() {
                        shutdownGracefully();
                    }
                });
                logger.info("connect to [" + host + ":" + port + "] successfully");
            } else {
                shutdownGracefully();
                throw new RPCNetworkException("connect to [" + host + ":" + port + "] failed");
            }
        }
    }

    /*@Override
    public void connect() throws InterruptedException {
        if (transportChannel == null || !transportChannel.isActive()) {
            init(channelHandler, codec);
            int attemptCount = 0;
            boolean connectSuccessFlag = false;
            while (attemptCount < initialConnectRetryTime) {
                TransportChannel temp = doConnect();
                if (temp != null) {
                    transportChannel = temp;
                    transportChannel.setCloseChannelListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete() {
                            shutdownGracefully();
                        }
                    });
                    connectSuccessFlag = true;
                    logger.info("connect to [" + host + ":" + port + "] successfully");
                    break;
                } else {
                    attemptCount++;
                    logger.error("try to connect to [" + host + ":" + port + "] failed for " +
                            attemptCount + " attempt, try to reconnect...");
                    Thread.sleep(attemptCount * 1000);
                }
            }
            if (!connectSuccessFlag) {
                shutdownGracefully();
                throw new RPCNetworkException("connect to [" + host + ":" + port + "] failed");
            }
        }
    }*/

    protected abstract void shutdownGracefully();

    protected abstract TransportChannel doConnect();

    protected abstract void init(final ChannelHandler<Response> channelHandler, final Codec<Message> codec);

    @Override
    public void setCloseChannelListener(ChannelFutureListener closeChannelListener) {
        transportChannel.setCloseChannelListener(closeChannelListener);
    }
}
