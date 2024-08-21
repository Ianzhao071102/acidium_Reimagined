package org.izdevs.acidium.networking.game;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.networking.game.payload.CombatPayload;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("connection is established:" + session.getRemoteAddress());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("endpoint message received: " + session.getId() + ": " + message.getPayload());
        this.handle(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.trace(session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("connection is closed:" + session.getId() + " reason:" + closeStatus.getReason());
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
        if (generator.validate(uuid)) {
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
                    CombatPayload payload = null;


                    try {
                        payload = new Gson().fromJson(pld, type);
                    } catch (JsonSyntaxException e) {
                        close(session, "invalid json data passed in");
                    }

                    if(payload == null){
                        close(session,"invalid data: payload is invalid");
                    }


                }
                default -> {
                    close(session, "invalid op type");
                }
            }

            //revision++
            session.getAttributes().put("revision", (int) session.getAttributes().getOrDefault("revision", 0) + 1);
        } else {
            close(session, "account session expired");
        }
    }

    public void close(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
        session.close();
    }
}
