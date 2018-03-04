package com.my.baffinrpc.core.communication.transport.netty;

import com.my.baffinrpc.core.communication.transport.ByteBuffer;
import io.netty.buffer.ByteBuf;

public class NettyByteBuffer implements ByteBuffer<ByteBuf> {
    private final ByteBuf byteBuf;

    public NettyByteBuffer(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public void writeInt(int value) {
        byteBuf.writeInt(value);
    }

    @Override
    public void writeLong(long value) {
        byteBuf.writeLong(value);
    }

    @Override
    public void writeShort(short value) {
        byteBuf.writeShort(value);
    }

    @Override
    public void writeByte(byte value) {
        byteBuf.writeByte(value);
    }

    @Override
    public void writeBytes(byte[] src) {
        byteBuf.writeBytes(src);
    }

    @Override
    public int readInt() {
        return byteBuf.readInt();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public short readShort() {
        return byteBuf.readShort();
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public void readBytes(byte[] dst) {
        byteBuf.readBytes(dst);
    }

    @Override
    public ByteBuf wrappedByteBuffer() {
        return null;
    }

    public void markReaderIndex()
    {
        byteBuf.markReaderIndex();
    }

    public void resetReaderIndex()
    {
        byteBuf.resetReaderIndex();
    }
}
