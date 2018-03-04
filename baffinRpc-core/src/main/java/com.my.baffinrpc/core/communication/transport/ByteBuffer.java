package com.my.baffinrpc.core.communication.transport;


public interface ByteBuffer<T> {

    void writeInt(int value);
    void writeLong(long value);
    void writeShort(short value);
    void writeByte(byte value);
    void writeBytes(byte[] src);

    int readInt();
    long readLong();
    short readShort();
    byte readByte();
    int readableBytes();
    void readBytes(byte[] dst);
    void markReaderIndex();
    void resetReaderIndex();

    T wrappedByteBuffer();



}
