package com.jaan.irpc.server;

import com.jaan.irpc.common.RpcProtocol;
import com.jaan.irpc.common.RpcRequest;
import com.jaan.irpc.common.utils.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * @ClassName ServerHandler
 * @Description
 * @Author jaan
 * @Date 2022/10/15 17:51
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Server server;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol protocol = (RpcProtocol) msg;
        RpcRequest rpcRequest = JsonUtil.fromJson(new String(protocol.getContent()), RpcRequest.class);

        Object service = server.getServiceMap().get(rpcRequest.getTargetServiceName());

        Class[] classes = new Class[rpcRequest.getArgs().length];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = rpcRequest.getArgs()[i].getClass();
        }

        Method method = service.getClass().getMethod(rpcRequest.getTargetMethod(), classes);

        Object result = method.invoke(service, rpcRequest.getArgs());
        rpcRequest.setResponse(result);
        RpcProtocol retProtocol = new RpcProtocol(JsonUtil.toJson(rpcRequest).getBytes());
        ctx.writeAndFlush(retProtocol);
    }

    public ServerHandler(Server server) {
        this.server = server;
    }
}
