package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Result;

public interface Response<MessageId> extends Message<MessageId> {
    Result getResult();
}
