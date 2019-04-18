package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Invocation;

public interface Request<MessageId> extends Message<MessageId> {
    Invocation getInvocation();
}
