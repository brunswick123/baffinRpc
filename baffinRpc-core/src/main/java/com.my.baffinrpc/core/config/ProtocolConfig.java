package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.common.constant.DefaultConfig;
import com.my.baffinrpc.core.protocol.Protocol;

import static com.my.baffinrpc.core.common.constant.DefaultConfig.PROTOCOL_PORT_PLACE_HOLDER;

public class ProtocolConfig {
    private int port;
    private final String transport;
    private final String serialization;
    private final String proxy;
    private final String message;


    public ProtocolConfig(Builder builder)
    {
        this.port = builder.port;
        this.transport = builder.transport;
        this.serialization = builder.serialization;
        this.proxy = builder.proxy;
        this.message = builder.message;
    }


    public static class Builder
    {
        private int port = PROTOCOL_PORT_PLACE_HOLDER;
        private String transport = DefaultConfig.TRANSPORT;
        private String serialization = DefaultConfig.SERIALIZATION;
        private String proxy = DefaultConfig.PROXY;
        private String message = DefaultConfig.MESSAGE;

        public Builder port(int port)
        {
            this.port = port;
            return this;
        }

        public Builder transport(String transport)
        {
            this.transport = transport;
            return this;
        }

        public Builder serialization(String serialization)
        {
            this.serialization = serialization;
            return this;
        }

        public Builder proxy(String proxy)
        {
            this.proxy = proxy;
            return this;
        }

        public Builder message(String message)
        {
            this.message = message;
            return this;
        }

        public ProtocolConfig build()
        {
            return new ProtocolConfig(this);
        }

    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTransport() {
        return transport;
    }



    public String getSerialization() {
        return serialization;
    }


    public String getProxy() {
        return proxy;
    }

    public String getMessage() {
        return message;
    }
}
