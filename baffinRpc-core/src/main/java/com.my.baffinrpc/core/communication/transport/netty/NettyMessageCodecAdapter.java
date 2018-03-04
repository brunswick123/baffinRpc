package com.my.baffinrpc.core.communication.transport.netty;

import com.my.baffinrpc.core.message.Codec;
import com.my.baffinrpc.core.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class NettyMessageCodecAdapter extends ByteToMessageCodec<Message> {

    private static final Logger logger = Logger.getLogger(NettyMessageCodecAdapter.class);

    private final Codec<Message> codec;

    public NettyMessageCodecAdapter(Codec<Message> codec) {
        this.codec = codec;
    }

    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        codec.encode(msg,new NettyByteBuffer(out));
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception  {
        Message message = codec.decode(new NettyByteBuffer(in));
        if (message == null)
            return;
        out.add(message);
    }
}
