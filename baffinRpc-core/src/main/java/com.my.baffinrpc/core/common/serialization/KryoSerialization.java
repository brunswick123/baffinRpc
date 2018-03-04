package com.my.baffinrpc.core.common.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KryoSerialization implements Serialization {

    private Kryo kryo = new Kryo();

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bo = null;
        Output output = null;
        try {
            bo = new ByteArrayOutputStream();
            output = new Output(bo);
            kryo.writeObject(output,object);
            output.flush();
            output.close();
            bytes = bo.toByteArray();

        } finally {
            try {
                if(bo!=null){
                    bo.close();
                }
                if(output!=null){
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException {
        T obj = null;
        ByteArrayInputStream bi = null;
        Input input = null;
        try {
            bi =new ByteArrayInputStream(bytes);
            input =new Input(bi);
            input.close();
            obj = kryo.readObject(input,clz);

        } catch (Exception e)
        {
            throw e;
        } finally {
            try {
                if(bi!=null){
                    bi.close();
                }
                if(input!=null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }
}
