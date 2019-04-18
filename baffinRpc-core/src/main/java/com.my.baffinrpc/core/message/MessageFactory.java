package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.annotation.Extension;
import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;

import java.io.Serializable;

@Extension
public interface MessageFactory<MessageId> {
    Request<MessageId> newRequest(Invocation invocation);
    Request<MessageId> newHeartBeatRequest();
    Response<MessageId> newResponse(Result result, Request<MessageId> request);
    Codec<Message<MessageId>> getMessageCodec();
}
