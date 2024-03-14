package org.izdevs.acidium.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server{
    public final int port;
    public Server(int port){
        this.port = port;
    }
    public void start() throws Throwable{
        ServerHandler handler = new ServerHandler();
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(master,worker);
            bootstrap.handler(handler);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.debug("socket connected:" + ch.toString());
                }
            });
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_REUSEADDR, true);
            bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childOption(ChannelOption.SO_LINGER, 0);
            bootstrap.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            bootstrap.childOption(ChannelOption.SO_RCVBUF, 1048576);
            bootstrap.childOption(ChannelOption.SO_SNDBUF, 1048576);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();


        }finally{
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
