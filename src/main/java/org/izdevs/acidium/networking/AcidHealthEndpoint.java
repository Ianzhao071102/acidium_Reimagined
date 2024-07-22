package org.izdevs.acidium.networking;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
@Component
@ServerEndpoint("/ws/utils/health")
public class AcidHealthEndpoint{
    Logger logger = LoggerFactory.getLogger(AcidHealthEndpoint.class);

    Session session;
    enum opType{
        READ,
        RESPOND,
        OPEN,
        DEAD,
        EXCEPTION
    }
    static String format(String payload,opType type){
        return String.format("[WebSocket-%s] %s",type.name(),payload);
    }

    @OnMessage
    public void OnMessage(String message){
        String session = this.session.getId();
        String payload = String.format("session-id = %s, message = %s",session,message);
        logger.info(format(payload,opType.READ));

        String response = String.format("[%s] OK", Instant.now());
        logger.info(format(response,opType.RESPOND));
        this.session.getAsyncRemote().sendText(response);
    }
    @OnOpen
    public void open(Session session) throws IOException {
        if(session != null){
            session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY,"Another Client is connecting..."));
        }
        this.session = session;
    }
}
