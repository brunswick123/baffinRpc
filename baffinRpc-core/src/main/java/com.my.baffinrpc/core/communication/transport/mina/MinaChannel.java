package com.my.baffinrpc.core.communication.transport.mina;


import com.my.baffinrpc.core.communication.transport.ChannelFutureListener;
import com.my.baffinrpc.core.communication.transport.TransportChannel;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;

public class MinaChannel implements TransportChannel {

    private final IoSession ioSession;
    private static final Logger logger = Logger.getLogger(MinaChannel.class);

    MinaChannel(IoSession ioSession)
    {
        this.ioSession = ioSession;
    }

    @Override
    public void send(Object object) {
        ioSession.write(object);
    }

    @Override
    public void close() {
        if (isActive()) {
            ioSession.closeOnFlush();
            logger.info("channel closed for remote address " + ioSession.getRemoteAddress());
        }
    }

    @Override
    public boolean isActive() {
        return ioSession != null && ioSession.isActive();
    }

    @Override
    public void setCloseChannelListener(final ChannelFutureListener channelFutureListener) {
        ioSession.getCloseFuture().addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                if (channelFutureListener != null)
                    channelFutureListener.operationComplete();
            }
        });
    }
}
