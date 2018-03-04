package com.my.baffinrpc.core.message.base;

import com.my.baffinrpc.core.common.serialization.Serialization;
import com.my.baffinrpc.core.common.serialization.SerializeType;
import com.my.baffinrpc.core.communication.transport.ByteBuffer;
import com.my.baffinrpc.core.message.AbstractMessage;
import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;

import java.io.IOException;

public class BaseMessageCodec implements Codec<Message> {

    @Override
    public Message decode(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
        if (byteBuffer.readableBytes() <= 1)
            return null;
        else
        {
            byteBuffer.markReaderIndex();
            byte serializeTypeByte = byteBuffer.readByte();
            Serialization serialization = SerializeType.getSerialization(serializeTypeByte);
            if (byteBuffer.readableBytes() <= 4) {
                byteBuffer.resetReaderIndex();
                return null;
            }

            int bodyLength = byteBuffer.readInt();
            if (byteBuffer.readableBytes() < bodyLength) {
                byteBuffer.resetReaderIndex();
                return null;
            }
            byte[] payload = new byte[bodyLength];
            byteBuffer.readBytes(payload);
            return serialization.deserialize(payload,AbstractMessage.class);
        }
    }

    @Override
    public void encode(Message message, ByteBuffer byteBuffer) throws IOException {
        byteBuffer.writeByte(message.getSerializeType());
        Serialization serialization = SerializeType.getSerialization(message.getSerializeType());
        byte[] payload =serialization.serialize(message);
        byteBuffer.writeInt(payload.length);
        byteBuffer.writeBytes(payload);

    }
}
