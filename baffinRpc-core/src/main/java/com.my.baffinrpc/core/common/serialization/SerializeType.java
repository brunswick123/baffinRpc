package com.my.baffinrpc.core.common.serialization;

public enum SerializeType {
    Jdk((byte)0),
    FastJson((byte)1),
    FST((byte)2),
    Kryo((byte)3);

    private final byte value;

     SerializeType(byte value)
    {
        this.value =value;
    }

    public byte getValue() {
        return value;
    }

    public static Serialization getSerialization(byte serializeTypeValue)
    {
        switch (serializeTypeValue)
        {
            case (byte)1:return new FastJsonSerialization();
            case (byte)2:return new FSTSerialization();
            default:
                return new JdkSerialization();
        }
    }
}
