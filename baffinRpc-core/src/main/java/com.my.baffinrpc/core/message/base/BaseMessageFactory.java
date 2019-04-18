package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.annotation.ExtensionImpl;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.message.*;

import java.util.UUID;

@ExtensionImpl(name = "base",extension = MessageFactory.class)
public class BaseMessageFactory extends AbstractMessageFactory<UUID>{

    public BaseMessageFactory() {
        super(new BaseMessageCodec());
    }

    @Override
    public Request<UUID> newRequest(Invocation invocation) {
        return new BaseRequest(invocation, UUID.randomUUID());
    }

    @Override
    public Request<UUID> newHeartBeatRequest() {
        return new BaseRequest((byte)0);
    }



}
