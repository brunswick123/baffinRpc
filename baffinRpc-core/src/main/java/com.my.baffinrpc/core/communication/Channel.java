package com.my.baffinrpc.core.communication;

public interface Channel {
    boolean isActive();
    void close();
    void send(Object object);
}
