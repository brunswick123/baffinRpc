package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Invocation;

public interface Request extends Message {
    Invocation getInvocation();
}
