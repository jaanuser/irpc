package com.jaan.irpc.common;

import com.jaan.irpc.common.Constant;
import com.jaan.irpc.common.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecode extends ByteToMessageDecoder {


    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() <= Constant.PROTOCAL_HEADER_LENGTH) {
            return;
        }
        if (Constant.MAGIC_NUMBER != byteBuf.readShort()) {
            return;
        }
        int bodyLength = byteBuf.readInt();
        if(bodyLength < byteBuf.readableBytes()){
            return;
        }
        RpcProtocol protocol =  new RpcProtocol();
        protocol.setContentLength(bodyLength);
        byte[] bytes = new byte[bodyLength];
        byteBuf.readBytes(bytes);
        protocol.setContent(bytes);
    }
}
