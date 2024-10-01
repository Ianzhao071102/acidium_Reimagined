package org.izdevs.acidium.networking.game;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import org.izdevs.acidium.game.player.PlayerHolder;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.account.OnlinePlayerRepository;
import org.izdevs.acidium.networking.game.payload.AuthenticationPayload;
import org.izdevs.acidium.networking.game.payload.CombatPayload;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.networking.game.payload.WarpTeleportation;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.SessionGenerator;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.WarpingPoint;
import org.izdevs.acidium.world.WarpingPointHolder;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

@Service
@Slf4j
@ServerEndpoint("/ws/game")
public class GameWSEndpoint implements WebSocketHandler {
    @Autowired
    SessionGenerator generator;

    @Autowired
    LoopManager loopManager;

    @Autowired
    OnlinePlayerRepository opr;
    @Autowired
    PlayerConnectionService connectionService;

    @Autowired
    WorldController controller;

    @Autowired
    WarpingPointHolder wpholder;
    @Autowired
    PlayerHolder playerHolder;

    public long getPlayersOnline(){
        return opr.count();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("connection is established:" + session.getRemoteAddress());

        session.getAttributes().put("authenticated", 0);
        session.getAttributes().put("username","");
        session.getAttributes().put("passwordHash","");
        session.getAttributes().put("world_name", "__UNSET__");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("endpoint message received: " + session.getId() + ": " + message.getPayload());
        this.handle(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.trace(session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("connection is closed:" + session.getId() + " reason:" + closeStatus.getReason());
        if(session.getAttributes().get("authenticated") instanceof Integer a && a > 0){
            String un = (String) session.getAttributes().get("username");
            if(un != null && !un.isEmpty()){
                if(opr.findJoinedPlayerByUsername(un) != null){
                    opr.deleteByUsername(un);
                }
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handle(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String pld = message.getPayload().toString();
        Gson gson = new Gson();

        ProtocolOperation<Object> po = null;
        try {
            Type type = new TypeToken<ProtocolOperation<Object>>() {
            }.getType();
            po = gson.fromJson(pld, type);
        } catch (JsonSyntaxException e) {
            this.close(session, "invalid payload, required json");
        }

        if (po == null) {
            this.close(session, "invalid payload, required json");
            return;
        }
        UUID uuid = UUID.fromString(po.uuid);

        //initially revision id is set to 0, so it is impossible for it to be less than 0
        if (po.revision < 0) {
            close(session, "invalid revision number");
        }
        if (po.revision == 0) {
            session.getAttributes().put("revision", 0);
        }

        switch (po.type) {
            case COMBAT -> {
                Type type = new TypeToken<ProtocolOperation<CombatPayload>>() {
                }.getType();
                ProtocolOperation<CombatPayload> payload = null;

                try {
                    payload = new Gson().fromJson(pld, type);
                } catch (JsonSyntaxException e) {
                    close(session, "invalid json data passed in");
                }

                if (payload == null) {
                    close(session, "invalid data: payload is invalid");
                    return;
                }
                CombatPositionType position = payload.additionalPayload.getPosition_type();
                if(position == null){
                    close(session,"invalid position data");
                }
                if(session.getAttributes().get("world_name").equals("__UNSET__")){
                    session.sendMessage(new TextMessage("failed to switch combat state: world is not specified"));
                    return;
                }
                JoinedPlayer player = new JoinedPlayer(session.getAttributes().getOrDefault("username", "__UNSET__").toString(),
                session.getAttributes().getOrDefault("passwordHash", "__UNSET__").toString(),
                uuid.toString());
                switch(position){
                    case TIGHT -> {
                        connectionService.attackState(player, CombatPositionType.TIGHT);
                        setToNormal5TicksLater(player);                        
                    }
                    case NORMAL -> {
                        setToNormal5TicksLater(player);   
                    }
                    case EXTENDED -> {
                        connectionService.attackState(player, CombatPositionType.EXTENDED);
                        setToNormal5TicksLater(player);   
                    }
                }                    
                
            }
            case LOGIN -> {
                if (session.getAttributes().get("authenticated") instanceof Integer a && a > 0) {
                    //user already authenticated
                    session.sendMessage(new TextMessage("already authenticated"));
                    break;
                }
                Type type = new TypeToken<ProtocolOperation<AuthenticationPayload>>() {
                }.getType();

                ProtocolOperation<AuthenticationPayload> payload = null;
                try {
                    payload = new Gson().fromJson(pld, type);
                } catch (JsonSyntaxException e) {
                    close(session, "invalid authentication payload sent");
                }

                if (payload != null && payload.additionalPayload != null) {
                    AuthenticationPayload auth = payload.additionalPayload;
                    String username = auth.getUsername();
                    String password = auth.getPasswordHash();

                    if(opr.findJoinedPlayerByUsername(username) == null){
                        close(session,"this user is not yet authenticated");
                    }
                    else{
                        JoinedPlayer player = opr.findJoinedPlayerByUsername(username);

                        if(player.getPasswordHash().equals(new BCryptPasswordEncoder().encode(password)) && uuid.toString().equals(player.getUuid())){
                            //authenticated ur in bro
                            session.getAttributes().put("authenticated",1);
                            session.getAttributes().put("username",username);
                            session.getAttributes().put("password",password);
                        }else{
                            close(session,"failed login attempt: edge case, please contact support: corrupted database row");
                        }
                    }
                }else{
                    close(session,"invalid authentication payload send check failed");
                }
            }
            case WARP -> {
                if(!(session.getAttributes().get("authenticated") instanceof Integer a && a >= 0)){
                    close(session,"please authorize before warping");
                    return;
                }

                Type type = new TypeToken<ProtocolOperation<WarpTeleportation>>() {
                }.getType();

                ProtocolOperation<WarpTeleportation> payload = null;
                try {
                    payload = new Gson().fromJson(pld, type);
                } catch (JsonSyntaxException e) {
                    close(session, "invalid authentication payload sent");
                }

                if(payload == null){
                    close(session,"invalid warp location specified");
                    return;
                }
                if(payload.additionalPayload == null){
                    close(session, "illegally formatted teleportation payload JSON");
                    return;
                }
                WarpTeleportation teleportation = payload.additionalPayload;
                
                if(teleportation.getPoint() == null){
                    close(session, "please provide a valid warp point");
                    return;
                }
                WarpingPoint point = teleportation.getPoint();
                double x = point.getX(),y = point.getY(), facing = point.getFacing();
                String world_name = point.getWorld_name();

                if(wpholder.getPoints().contains(point)){
                    //found it
                    controller.worlds.forEach((world) -> {
                        if(world.getName().equals(world_name)){
                            session.getAttributes().put("world_name", point.getWorld_name());
                        }
                    });
                    
                }

                if(session.getAttributes().get("world_name").equals("__UNSET__")){
                    close(session, "the world specified for warping is NOT found");
                }else{
                    JoinedPlayer jp = opr.findJoinedPlayerByUsername(session.getAttributes().get("username").toString());
                    if(jp == null){
                        close(session, "authentication for this player is NOT found");
                        return;
                    }
                    jp.setWorld_name(world_name);
                    opr.save(jp);
                    
                    connectionService.teleport(jp, new Location((int) x , (int) y));
                }
            }
            default -> {
                close(session, "invalid op type");
            }
        }

        //revision++
        session.getAttributes().put("revision", (int) session.getAttributes().getOrDefault("revision", 0) + 1);
    }

    public void close(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
        session.close();
    }
    private void setToNormal5TicksLater(JoinedPlayer player){
        loopManager.scheduleAsyncDelayedTask(new DelayedTask(() -> {
            connectionService.attackState(player,CombatPositionType.NORMAL);
        }, 5));
    }
}
