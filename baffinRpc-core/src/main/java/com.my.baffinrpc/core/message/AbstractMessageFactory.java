package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.common.model.Result;


public abstract class AbstractMessageFactory<MessageId> implements MessageFactory<MessageId> {

    private final Codec<Message<MessageId>> codec;

    public AbstractMessageFactory(Codec<Message<MessageId>> codec) {
        this.codec = codec;
    }

    @Override
    public Codec<Message<MessageId>> getMessageCodec() {
        return codec;
    }

    @Override
    public Response<MessageId> newResponse(final Result result, final Request<MessageId> request) {
        return new DefaultResponse<>(result,request.getMessageId(),request.getSerializeType());
    }




}
