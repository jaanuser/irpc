package com.jaan.irpc;

import com.jaan.irpc.common.RpcProtocol;
import com.jaan.irpc.common.RpcRequest;
import com.jaan.irpc.common.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.jaan.irpc.Client.RET_MAP;

/**
 * @ClassName ClientHandler
 * @Description
 * @Author jaan
 * @Date 2022/10/16 1:23
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        RpcRequest rpcRequest = JsonUtil.fromJson(rpcProtocol.getContent().toString(), RpcRequest.class);
        RET_MAP.put(rpcRequest.getUuid(),rpcRequest);

        super.channelRead(ctx, msg);
    }
}
