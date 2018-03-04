package com.my.baffinrpc.core.communication.exchange;

import com.my.baffinrpc.core.common.future.ResponseFuture;
import com.my.baffinrpc.core.common.future.ResponseFutureImpl;
import com.my.baffinrpc.core.common.model.Result;
import com.my.baffinrpc.core.communication.transport.TransportClient;
import com.my.baffinrpc.core.message.Request;

public class ExchangeClientImpl implements ExchangeClient {

    private final TransportClient transportClient;

    public ExchangeClientImpl(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @Override
    public ResponseFuture<Result> request(Request request) throws InterruptedException {
        if (!transportClient.isActive()) {
            connect();
        }
        ResponseFutureImpl responseFuture = new ResponseFutureImpl();
        ResponseFutureImpl.futureMap().put(request.getMessageId(),responseFuture);
        transportClient.send(request);
        return responseFuture;
    }

    @Override
    public void connect() {
        transportClient.connect();
    }


    @Override
    public boolean isActive() {
        return transportClient.isActive();
    }


    @Override
    public void close() {
        transportClient.close();
    }

    @Override
    public void send(Object object) {
        transportClient.send(object);
    }
}
