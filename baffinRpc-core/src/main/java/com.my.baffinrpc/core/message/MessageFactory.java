package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Invocation;
import com.my.baffinrpc.core.common.model.Result;

public interface MessageFactory {
    Request newRequest(Invocation invocation);
    Request newHeartBeatRequest();
    Response newResponse(Result result, Request request);
    Codec getMessageCodec();
}
