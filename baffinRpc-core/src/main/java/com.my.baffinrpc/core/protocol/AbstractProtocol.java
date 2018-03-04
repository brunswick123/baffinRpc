package com.my.baffinrpc.core.protocol;

public abstract class AbstractProtocol implements Protocol {
    protected int port;

    protected AbstractProtocol(int port) {
        this.port = port;
    }


    public int getPort() {
        return port;
    }
}
