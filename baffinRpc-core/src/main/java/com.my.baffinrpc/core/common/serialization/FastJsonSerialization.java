package com.my.baffinrpc.core.common.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;


public class FastJsonSerialization implements Serialization{
    public <T> byte[] serialize(T object) throws IOException {
        SerializeWriter writer = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(writer);
        //serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        //serializer.config(SerializerFeature.);
        serializer.config(SerializerFeature.WriteClassName, true);
        serializer.write(object);
        return writer.toBytes("UTF-8");

    }

    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException {
        return JSON.parseObject(new String(bytes),clz);
    }
}
