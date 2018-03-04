package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.protocol.Protocol;

public class ProtocolConfig {
    private Protocol protocol;

    public ProtocolConfig(Protocol protocol) {
        this.protocol = protocol;
    }


    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }


}
