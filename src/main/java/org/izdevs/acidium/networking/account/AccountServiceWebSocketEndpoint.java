package org.izdevs.acidium.networking.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.websocket.server.ServerEndpoint;
import org.checkerframework.checker.units.qual.A;
import org.izdevs.acidium.api.v1.Payload;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.security.SessionGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.UUID;

@Configurable
public class AccountServiceWebSocketEndpoint implements WebSocketHandler {
    @Autowired
    UserRepository repository;
    @Autowired
    SessionGenerator generator;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("WS connection has been established...");
        if (repository == null) {
            logger.info("repository is not initialized... service might not be available at the time");
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        logger.info("msg rcv");
        this.handle(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(exception instanceof IOException){
            session.sendMessage(new TextMessage("connection error"));
            session.close(CloseStatus.PROTOCOL_ERROR);
        }
        logger.trace("exception thrown in account service", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("connection has been closed, status: " + closeStatus.getReason());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handle(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String payload = message.getPayload().toString();
        UserAccountSchema schema;
        try {
            schema = new Gson().fromJson(payload, UserAccountSchema.class);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("illegal argument passed in 1"));
            session.close(CloseStatus.BAD_DATA);
            logger.debug("error", e);
            return;
        }
        if (schema == null || schema.type == null) {
            session.sendMessage(new TextMessage("illegal argument passed in 2 "));
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        String username = schema.username;
        String password = schema.password;
        if (User.username_regex.matches(username) && User.password_regex.matches(password)) {
            String password_hash = new BCryptPasswordEncoder().encode(password);
            User user = new User(username, password_hash);
            switch (schema.type) {
                case REGISTER -> {
                    if (repository.findUserByUsername(username) != null) {
                        session.sendMessage(new TextMessage("username exists"));
                        session.close(CloseStatus.BAD_DATA);
                        return;
                    }
                    repository.save(user);
                }
                case LOGIN -> {
                    if (!repository.findUserByUsername(username).getPasswordHash().equals(password_hash)) {
                        session.sendMessage(new TextMessage("incorrect password"));
                        session.close(CloseStatus.BAD_DATA);
                        return;
                    } else {
                        UUID uuid = generator.use();
                        session.sendMessage(new TextMessage(uuid.toString()));
                        session.close(CloseStatus.NORMAL);
                    }
                }
            }

            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(user)));
            session.close(CloseStatus.NORMAL);
        } else {
            session.sendMessage(new TextMessage("illegal argument passed in 3"));
            session.close(CloseStatus.BAD_DATA);
        }
    }
}
