package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.message.AbstractMessage;
import com.my.baffinrpc.core.message.Request;

import java.util.UUID;

public class BaseRequest extends AbstractMessage implements Request {

    private final Invocation invocation;

    public BaseRequest(Invocation invocation, UUID messageId) {
        super(messageId, invocation.getSerializationType(), false);
        this.invocation = invocation;
    }

    public BaseRequest(byte serializationType)
    {
        super(null,serializationType,true);
        this.invocation = null;
    }

    @Override
    public Invocation getInvocation() {
        return invocation;
    }

}
