package com.my.baffinrpc.core.message;

import com.my.baffinrpc.core.communication.transport.ByteBuffer;

import java.io.IOException;

public interface Codec<T extends Message> {
    /***
     * 解码
     * @param byteBuffer
     * @return 当数据不足一个完整的解析单元 需要ByteBuffer继续读取时 返回null, 否则返回成功解析的对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    T decode(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException;
    void encode(T message, ByteBuffer byteBuffer) throws IOException;
}
