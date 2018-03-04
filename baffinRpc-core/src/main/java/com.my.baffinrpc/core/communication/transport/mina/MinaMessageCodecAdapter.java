package com.my.baffinrpc.core.communication.transport.mina;

import com.my.baffinrpc.core.communication.transport.ByteBuffer;
import com.my.baffinrpc.core.message.*;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;

public class MinaMessageCodecAdapter implements ProtocolCodecFactory {

    private ProtocolEncoder protocolEncoder = new Encoder();
    private CumulativeProtocolDecoder cumulativeProtocolDecoder = new Decoder();
    private Codec<Message> codec;

    MinaMessageCodecAdapter(Codec<Message> codec)
    {
        this.codec = codec;
    }


    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return protocolEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return cumulativeProtocolDecoder;
    }

    private class Encoder extends ProtocolEncoderAdapter
    {
        @Override
        public void encode(IoSession session, Object object, ProtocolEncoderOutput out) throws Exception {
            IoBuffer buffer = IoBuffer.allocate(1024,false);
            buffer.setAutoExpand(true);
            ByteBuffer<IoBuffer> byteBuffer = new MinaByteBuffer(buffer);
            Message message = (Message)object;
            codec.encode(message,byteBuffer);
            buffer.flip();
            out.write(buffer);
        }
    }


    private class Decoder extends CumulativeProtocolDecoder
    {
        @Override
        protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            ByteBuffer<IoBuffer> byteBuffer = new MinaByteBuffer(in);
            Message message = codec.decode(byteBuffer);
            if(message == null)
                return false;
            else
            {
                out.write(message);
                return true;
            }
        }
    }
}
