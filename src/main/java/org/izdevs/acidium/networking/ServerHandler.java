package org.izdevs.acidium.networking;

import com.google.gson.Gson;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.security.PlayerManager;
import org.izdevs.acidium.security.SessionGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.izdevs.acidium.AcidiumApplication.*;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(ServerHandler.class);
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

                    Statement statement = SQLConnection.createStatement();
                    Pattern pattern = Pattern.compile("^([a-z]|[A-Z]|[0-9]|[\\-]|[\\_]){5,20}$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
                    Matcher matcher = pattern.matcher(username);

                    if(!matcher.matches()){
                        ctx.writeAndFlush("invalid username,must match: ^([a-z]|[A-Z]|[0-9]|[\\-]|[\\_]){5,20}$");
                        ctx.close();
                    }

                    String sql = "SELECT * FROM ONLY users WHERE username = " + username;
                    //SQL QUERY UP
                    ResultSet result = statement.executeQuery(sql);
                    String resultPass = result.getString("passwordHash"); //get password
                    int length = 0;
                    result.last();
                    length = result.getRow();

                    //fucking problems
                    if(length == 0){
                        ctx.writeAndFlush("invalid credentials");
                        ctx.close();
                    }
                    else if(length !=1){
                        logger.warn("there are two users with same name, query is below");
                        logger.error(result.toString());
                    }
                    
                    if(resultPass.equals(encrypted)){
                        //give the client session id
                        UUID uuid = SessionGenerator.use();

                        Payload payload = new Payload();
                        Gson gson = new Gson();
                        payload.setPayload(gson.toJson(uuid));
                        ctx.writeAndFlush(payload);
                        ctx.channel().attr(AttributeKey.valueOf("uuid")).set(uuid);

                        Player associated = new Player(new User(username,uuid));
                        PlayerManager.add(associated);
                        ctx.channel().attr(AttributeKey.valueOf("player")).set(associated);
                    }
                }else{
                    ctx.writeAndFlush("-105 invalid argument");
                    ctx.close();
                }
            }
            //ANTI_GROSS: UN-NESTED CODE, YOU ARE IN A SWITCH RIGHT HERE IN THIS SCOPE(CASE DEFINITIONS)
            case "move" -> {
                double tangent = 0;
                try {
                    tangent = Double.parseDouble(split[1]);
                }catch(Exception e){
                    ctx.writeAndFlush("invalid angle...");
                    ctx.close();
                }
                if(!ctx.channel().hasAttr(AttributeKey.valueOf("uuid"))){
                    //unauthorized
                    ctx.writeAndFlush("Please login before this.....");
                    ctx.writeAndFlush("Thanks a lot, do not attack this service.... plz");
                    ctx.close();
                }

                //uuid present
                String a = (String) ctx.channel().attr(AttributeKey.valueOf("uuid")).get();
                UUID uuid = null;
                try{
                    uuid = UUID.fromString(a);
                }catch(IllegalArgumentException e){
                    ctx.writeAndFlush("session invalid");
                    ctx.writeAndFlush("please, do not attack our services, it is a project made in free time....");
                    ctx.close();
                }

                if(uuid == null){
                    ctx.writeAndFlush("session invalid");
                    ctx.writeAndFlush("please, do not attack our services, it is a project made in free time....");
                    ctx.close();
                }

                //uuid valid first step
                if(SessionGenerator.validate(uuid)){
                    //valid uuid
                    if(ctx.channel().attr(AttributeKey.valueOf("player")).get() instanceof Player player){
                        if(!PlayerManager.players.contains(player)){
                            //fuck it ass shit hell
                            ctx.writeAndFlush("session invalid");
                            ctx.writeAndFlush("please, do not attack our services, it is a project made in free time....");
                            ctx.close();
                        }
                        //finally....


                        double x = player.getEntity().getX();
                        double y = player.getEntity().getY();

                        //把 tangent 变量 与 movementSpeed 构成的极坐标系转换为直角坐标系并作为向量与 x,y 相加
                        //ADD TANGENT AND MOVEMENT SPEED AS THE VECTOR
                        double prevX = Math.cos(tangent)*player.getEntity().getMovementSpeed();
                        double prevY = Math.sin(tangent)*player.getEntity().getMovementSpeed();

                        x+=prevX;
                        y+=prevY;

                        player.getEntity().setX(x);
                        player.getEntity().setY(y);
                    }else{
                        ctx.writeAndFlush("session invalid");
                        ctx.writeAndFlush("please, do not attack our services, it is a project made in free time....");
                        ctx.close();
                    }
                }else{
                    ctx.writeAndFlush("session invalid");
                    ctx.writeAndFlush("please, do not attack our services, it is a project made in free time....");
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
