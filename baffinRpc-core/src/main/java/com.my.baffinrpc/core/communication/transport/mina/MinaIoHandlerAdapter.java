package com.my.baffinrpc.core.communication.transport.mina;

import com.my.baffinrpc.core.communication.ChannelHandler;
import com.my.baffinrpc.core.message.Message;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MinaIoHandlerAdapter<T extends Message> extends IoHandlerAdapter {
    private final ChannelHandler<T> channelHandler;

    public MinaIoHandlerAdapter(ChannelHandler<T> channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void messageReceived(IoSession session, Object message) throws Exception {
        channelHandler.received(new MinaChannel(session),(T)message);
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        channelHandler.channelIdle(new MinaChannel(session));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        channelHandler.exceptionCaught(new MinaChannel(session),cause);
    }
}
