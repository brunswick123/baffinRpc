package com.my.baffinrpc.core.message;

import java.io.Serializable;
import java.util.UUID;

public interface Message extends Serializable {
    UUID getMessageId();
    byte getSerializeType();
    boolean isHeartBeat();
}
