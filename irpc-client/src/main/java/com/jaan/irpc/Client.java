package com.jaan.irpc;

import com.jaan.irpc.common.Constant;
import com.jaan.irpc.common.RpcDecode;
import com.jaan.irpc.common.RpcProtocol;
import com.jaan.irpc.common.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName Client
 * @Description
 * @Author jaan
 * @Date 2022/10/15 18:51
 */
public class Client {
    // rpc代理工厂

    // 请求队列
    protected static final BlockingQueue<RpcProtocol> REQ_QUEUE = new LinkedBlockingQueue();

    // 返回
    protected static final Map<String, RpcRequest> RET_MAP = new HashMap<String, RpcRequest>();


    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap()
                .channel(NioSctpServerChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcDecode());
                        socketChannel.pipeline().addLast(new ClientHandler());
                        socketChannel.pipeline().addLast(new RpcDecode());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", Constant.SERVER_PORT).sync();
        startChannel(channelFuture);
    }

    private void startChannel(final ChannelFuture channelFuture) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    channelFuture.wait();
                    RpcProtocol rpcProtocol = REQ_QUEUE.take();
                    channelFuture.channel().writeAndFlush(rpcProtocol);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
