package com.my.baffinrpc.core.communication.exchange;

import com.my.baffinrpc.core.common.future.ResponseFuture;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.communication.Channel;
import com.my.baffinrpc.core.communication.Client;
import com.my.baffinrpc.core.message.Request;

public interface ExchangeClient extends Client,Channel{
    ResponseFuture<Result> request(Request request) throws InterruptedException;
}
