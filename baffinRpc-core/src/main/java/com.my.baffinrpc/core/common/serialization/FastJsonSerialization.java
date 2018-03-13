package com.my.baffinrpc.core.common.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.my.baffinrpc.core.annotation.ExtensionImpl;

import java.io.IOException;

@ExtensionImpl(name = "fastJson",extension = Serialization.class)
public class FastJsonSerialization implements Serialization{
    public <T> byte[] serialize(T object) throws IOException {
        SerializeWriter writer = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(writer);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(object);
        return writer.toBytes("UTF-8");

    }

    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return JSON.parseObject(new String(bytes),clz);
    }

    @Override
    public byte getSerializationId() {
        return 2;
    }
}
