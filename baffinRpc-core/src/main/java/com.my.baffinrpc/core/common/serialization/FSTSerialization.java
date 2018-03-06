package com.my.baffinrpc.core.common.serialization;

import com.my.baffinrpc.core.message.Message;
import com.my.baffinrpc.core.message.Request;
import com.my.baffinrpc.core.message.Response;
import org.nustaq.serialization.FSTConfiguration;

import java.io.IOException;

public class FSTSerialization implements Serialization {

    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    static
    {
        conf.registerClass(Message.class);
        conf.registerClass(Request.class);
        conf.registerClass(Response.class);
    }

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return conf.asByteArray(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException {
        return (T)conf.asObject(bytes);
    }

    @Override
    public byte getSerializationId() {
        return 1;
    }
}
