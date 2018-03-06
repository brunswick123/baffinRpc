package com.my.baffinrpc.core.protocol.invoker;

import com.my.baffinrpc.core.common.exception.RPCBizException;
import com.my.baffinrpc.core.common.exception.RPCFrameworkException;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.common.model.ResultFactory;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.message.MessageFactory;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.util.URLUtil;

public class InvokerTask implements Runnable {

    private final Invoker invoker;
    private final Invocation invocation;
    private final Channel channel;
    private final MessageFactory messageFactory;
    private final Request request;

    public InvokerTask(Invoker invoker, Invocation invocation, Channel channel, MessageFactory messageFactory, Request request) {
        this.invoker = invoker;
        this.invocation = invocation;
        this.channel = channel;
        this.messageFactory = messageFactory;
        this.request = request;
    }


    @Override
    public void run() {
        Result result;
        try
        {
            result = invoker.invoke(invocation);
        }catch (Exception e)
        {
            result = ResultFactory.newExceptionResult(new RPCBizException(e));
        }
        channel.send(messageFactory.newResponse(result, request));
    }
}
