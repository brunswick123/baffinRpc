package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.message.*;

import java.util.UUID;

@ExtensionImpl(name = "base",extension = MessageFactory.class)
public class BaseMessageFactory extends AbstractMessageFactory{

    public BaseMessageFactory() {
        super(new BaseMessageCodec());
    }

    @Override
    public Request newRequest(Invocation invocation) {
        return new BaseRequest(invocation, UUID.randomUUID());
    }

    @Override
    public Request newHeartBeatRequest() {
        return new BaseRequest((byte)0);
    }

    @Override
    public Response newResponse(Result result, Request request) {
        return new BaseResponse(result,request.getMessageId(),request.getSerializeType());
    }
}
