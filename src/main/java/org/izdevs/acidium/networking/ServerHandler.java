package org.izdevs.acidium.networking;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.springframework.data.jdbc.repository.query.Query;

import static org.izdevs.acidium.AcidiumApplication.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("version: " + version);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String message = in.toString();
        String[] split = message.split("-");
        ctx.writeAndFlush("rcv");
        switch(split[0]){
            case "login" -> {
                if(ctx.channel().hasAttr(AttributeKey.valueOf("connected"))){
                    ctx.writeAndFlush("already connected to this relay...");
                    ctx.close();
                }
                if(split.length == 3){
                    String username = split[1];
                    String password = split[2];
                    ctx.channel().attr(AttributeKey.valueOf("connected")).set(true);
                    String encrypted = bcrypt(password);

                }else{
                    ctx.writeAndFlush("-105 invalid argument");
                    ctx.close();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(cause);
    }
}
