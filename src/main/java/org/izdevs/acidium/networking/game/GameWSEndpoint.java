package org.izdevs.acidium.networking.game;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.game.player.PlayerHolder;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.account.OnlinePlayerRepository;
import org.izdevs.acidium.networking.account.PartyRepository;
import org.izdevs.acidium.networking.game.handler.ChatMessage;
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
    @Autowired
    PartyRepository partyRepository;

    public long getPlayersOnline() {
        return opr.count();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("connection is established:" + session.getRemoteAddress());

        session.getAttributes().put("authenticated", 0);
        session.getAttributes().put("username", "");
        session.getAttributes().put("passwordHash", "");
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
        log.info("connection is closed:{} reason:{}", session.getId(), closeStatus.getReason());
        if (session.getAttributes().get("authenticated") instanceof Integer a && a > 0) {
            String un = (String) session.getAttributes().get("username");
            if (un != null && !un.isEmpty()) {
                if (opr.findJoinedPlayerByUsername(un) != null) {
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
        if(message.getPayload() instanceof ProtocolOperation){

        }
        else session.sendMessage(new TextMessage("CRITICAL-1-ILLEGAL_ARGUMENT"));
        session.close(CloseStatus.BAD_DATA);
    }

    public boolean chat(User user, ChatMessage msg,WebSocketSession session) {
        if(opr.findJoinedPlayerByUsername(user.getUsername()) != null){
            if(session.getAttributes().get("username").equals(user.getUsername())){
                //todo finish chat message routing

                switch(msg.type){
                    case PARTY -> {

                    }
                }
            }
        }
        return false;
    }
    public boolean announcement(ChatMessage msg,WebSocketSession session){
        //todo finish implementing this crap
    }
}