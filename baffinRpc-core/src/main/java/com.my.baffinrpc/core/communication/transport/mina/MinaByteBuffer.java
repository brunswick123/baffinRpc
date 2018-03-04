package com.my.baffinrpc.core.communication.transport.mina;

import com.my.baffinrpc.core.communication.transport.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;

public class MinaByteBuffer implements ByteBuffer<IoBuffer> {

    private final IoBuffer ioBuffer;

    MinaByteBuffer(IoBuffer ioBuffer) {
        this.ioBuffer = ioBuffer;
    }

    @Override
    public void writeInt(int value) {
        ioBuffer.putInt(value);
    }

    @Override
    public void writeLong(long value) {
        ioBuffer.putLong(value);
    }

    @Override
    public void writeShort(short value) {
        ioBuffer.putShort(value);
    }

    @Override
    public void writeByte(byte value) {
        ioBuffer.put(value);
    }

    @Override
    public void writeBytes(byte[] src) {
        ioBuffer.put(src);
    }

    @Override
    public int readInt() {
        return ioBuffer.getInt();
    }

    @Override
    public long readLong() {
        return ioBuffer.getLong();
    }

    @Override
    public short readShort() {
        return ioBuffer.getShort();
    }

    @Override
    public byte readByte() {
        return ioBuffer.get();
    }

    @Override
    public int readableBytes() {
        return ioBuffer.remaining();
    }

    @Override
    public void readBytes(byte[] dst) {
        ioBuffer.get(dst);
    }

    @Override
    public void markReaderIndex() {
        ioBuffer.mark();
    }

    @Override
    public void resetReaderIndex() {
        ioBuffer.reset();
    }

    @Override
    public IoBuffer wrappedByteBuffer() {
        return ioBuffer;
    }


}
