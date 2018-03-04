package com.my.baffinrpc.core.communication.transport.mina;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.communication.transport.AbstractTransportServer;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MinaTransportServer extends AbstractTransportServer {

    private IoAcceptor acceptor;
    private static final Logger logger = Logger.getLogger(MinaTransportServer.class);

    public MinaTransportServer(String host, int port, ChannelHandler<Request> channelHandler, Codec<Message> codec) {
        super(host, port, channelHandler,codec);
    }

    public void init(ChannelHandler<Request> channelHandler, Codec<Message> codec)
    {
        acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setReaderIdleTime(READ_IDLE_INTERVAL_SECONDS);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaMessageCodecAdapter(codec)));
        acceptor.setHandler(new MinaIoHandlerAdapter<>(channelHandler));
        acceptor.getSessionConfig().setReadBufferSize(2048);
    }


    @Override
    protected boolean doBind() {
        try {
            acceptor.bind(new InetSocketAddress(host, port));
            return true;
        }catch (IOException e)
        {
            logger.error("bind server to [" + host + ":" + port + "] failed due to " +  e.getMessage());
            return false;
        }
    }

    @Override
    protected void shutdownGracefully() {
        acceptor.unbind();
        acceptor.dispose();
    }
}
