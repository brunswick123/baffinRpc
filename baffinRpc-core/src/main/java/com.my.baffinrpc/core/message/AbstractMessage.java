package com.my.baffinrpc.core.message;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractMessage<MessageId> implements Message<MessageId>,Serializable {
    private final MessageId messageId;
    private final byte serializeType;
    private final boolean heartBeat;

    protected AbstractMessage(MessageId messageId, byte serializeType, boolean heartBeat) {
        this.messageId = messageId;
        this.serializeType = serializeType;
        this.heartBeat = heartBeat;
    }


    @Override
    public MessageId getMessageId() {
        return messageId;
    }

    @Override
    public byte getSerializeType() {
        return serializeType;
    }

    @Override
    public boolean isHeartBeat() {
        return heartBeat;
    }

}
