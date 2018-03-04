package com.my.baffinrpc.core.common.serialization;

import java.io.IOException;

public interface Serialization {
    <T> byte[] serialize(T object) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException;
}
