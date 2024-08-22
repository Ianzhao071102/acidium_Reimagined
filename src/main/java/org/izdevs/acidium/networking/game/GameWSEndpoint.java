package org.izdevs.acidium.networking.game;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.networking.account.JoinedPlayer;
import org.izdevs.acidium.networking.account.OnlinePlayerRepository;
import org.izdevs.acidium.networking.game.payload.AuthenticationPayload;
import org.izdevs.acidium.networking.game.payload.CombatPayload;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.security.SessionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("connection is established:" + session.getRemoteAddress());

        //authentication happens here
        session.getAttributes().put("authenticated", 0);
        session.getAttributes().put("username","");
        session.getAttributes().put("passwordHash","");
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
        }
        assert po != null;
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

                        if(player.getPasswordHash().equals(password) && uuid.toString().equals(player.getUuid())){
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
}
