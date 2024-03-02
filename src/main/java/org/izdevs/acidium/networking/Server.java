package org.izdevs.acidium.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public final int port;
    public Server(int port){
        this.port = port;
    }
    public void start() throws Throwable{
        ServerHandler handler = new ServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.localAddress(port);
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    })
            ;

            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();


        }finally{
            group.shutdownGracefully();
        }
    }
}
