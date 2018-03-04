package com.my.baffinrpc.core.message;

public abstract class AbstractMessageFactory implements MessageFactory {

    private final Codec<Message> codec;

    public AbstractMessageFactory(Codec<Message> codec) {
        this.codec = codec;
    }

    @Override
    public Codec<Message> getMessageCodec() {
        return codec;
    }
}
