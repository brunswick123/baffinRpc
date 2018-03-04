package com.my.baffinrpc.core.message;

import java.io.Serializable;
import java.util.UUID;

public abstract class AbstractMessage implements Message,Serializable {
    private final UUID messageId;
    private final byte serializeType;
    private final boolean heartBeat;

    protected AbstractMessage(UUID messageId, byte serializeType, boolean heartBeat) {
        this.messageId = messageId;
        this.serializeType = serializeType;
        this.heartBeat = heartBeat;
    }


    @Override
    public UUID getMessageId() {
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
