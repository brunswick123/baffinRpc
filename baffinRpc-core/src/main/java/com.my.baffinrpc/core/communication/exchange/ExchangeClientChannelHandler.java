package com.my.baffinrpc.core.communication.exchange;

import com.my.baffinrpc.core.common.future.ResponseFutureImpl;
import com.my.baffinrpc.core.communication.AbstractChannelHandler;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.message.Response;

public class ExchangeClientChannelHandler extends AbstractChannelHandler<Response> {

    @Override
    public void received(Channel channel, Response msg) throws Exception {
        ResponseFutureImpl.received(msg);
    }
}
