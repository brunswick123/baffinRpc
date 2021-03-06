package com.my.baffinrpc.core.common.serialization;

import com.my.baffinrpc.core.annotation.ExtensionImpl;

import java.io.*;

@ExtensionImpl(name = "jdk",extension = Serialization.class)
public class JdkSerialization implements Serialization {
    public <T> byte[] serialize(T object) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        ObjectOutputStream oo = null;
        try {
            bo = new ByteArrayOutputStream();
            oo = new ObjectOutputStream(bo);

            oo.writeObject(object);
            bytes = bo.toByteArray();

        } finally {
            try {
                if(bo!=null){
                    bo.close();
                }
                if(oo!=null){
                    oo.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            bi =new ByteArrayInputStream(bytes);
            oi =new ObjectInputStream(bi);
            obj = oi.readObject();

        } catch (Exception e)
        {
            throw e;
        } finally {
            try {
                if(bi!=null){
                    bi.close();
                }
                if(oi!=null){
                    oi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return (T)obj;
    }

    @Override
    public byte getSerializationId() {
        return 0;
    }
}
