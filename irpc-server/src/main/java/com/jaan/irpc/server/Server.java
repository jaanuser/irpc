package com.jaan.irpc.server;

import com.jaan.irpc.common.RpcDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {

    // 服务map
    private HashMap<String, Object> serviceMap;

    public void start(Map<Class, Object> map) {
        initServiceMap(map);
        start();
    }

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .channel(NioSctpServerChannel.class)
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcDecode());
                        socketChannel.pipeline().addLast(new ServerHandler(Server.this));
                        socketChannel.pipeline().addLast(new RpcDecode());
                    }
                });
    }

    private void initServiceMap(Map<Class, Object> map) {
        Set<Class> classSet = map.keySet();
        for (Class clazz : classSet) {
            if (!clazz.getClass().isInterface()) {
                throw new RuntimeException("");
            }
            Object service = map.get(clazz);
            if (null == service) {

            }
            if (!service.getClass().isInstance(clazz)) {

            }
            serviceMap.put(service.getClass().getName(), service);
        }
    }

    public HashMap<String, Object> getServiceMap() {
        return serviceMap;
    }
}
