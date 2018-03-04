package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.message.*;

import java.util.UUID;

public class BaseMessageFactory extends AbstractMessageFactory{

    public BaseMessageFactory() {
        super(new BaseMessageCodec());
    }

    @Override
    public Request newRequest(Invocation invocation) {
        BaseRequest baseRequest = new BaseRequest(invocation, UUID.randomUUID());
        return baseRequest;
    }

    @Override
    public Request newHeartBeatRequest() {
        return new BaseRequest((byte)0);
    }

    @Override
    public Response newResponse(Result result, Request request) {
        Response response = new BaseResponse(result,request.getMessageId(),request.getSerializeType());
        return response;
    }
}
