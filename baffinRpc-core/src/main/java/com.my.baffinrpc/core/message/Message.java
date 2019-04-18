package com.my.baffinrpc.core.message;

import java.io.Serializable;
import java.util.UUID;

public interface Message<MessageId> extends Serializable {

    MessageId getMessageId();
    byte getSerializeType();
    boolean isHeartBeat();
}
