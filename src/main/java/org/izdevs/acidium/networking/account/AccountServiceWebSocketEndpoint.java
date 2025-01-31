package org.izdevs.acidium.networking.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.mapping.Join;
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

@Service
@Slf4j
@ServerEndpoint("/ws/account")
public class AccountServiceWebSocketEndpoint implements WebSocketHandler {
    @Autowired
    UserRepository repository;
    @Autowired
    SessionGenerator generator;
    @Autowired
    OnlinePlayerRepository opr;
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
        if (exception instanceof IOException) {
            if (!session.isOpen()) {
                session.sendMessage(new TextMessage("connection error"));
                session.close(CloseStatus.PROTOCOL_ERROR);
            }
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
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password_hash = encoder.encode(password);
            User user = new User(username, password_hash);
            switch (schema.type) {
                case REGISTER -> {
                    if (repository.findByUsername(username) != null) {
                        logger.debug("session is closed due to failed register attempt");
                        session.sendMessage(new TextMessage("username exists"));
                        session.close(CloseStatus.BAD_DATA);
                        return;
                    }
                    repository.save(user);
                }
                case LOGIN -> {
                    if (repository.findByUsername(username) == null) {
                        session.sendMessage(new TextMessage("user does not exist"));
                        session.close(CloseStatus.BAD_DATA);
                    }
                    String hash = repository.findByUsername(username).getPasswordHash();
                    logger.debug(new Gson().toJson(user));
                    if (!encoder.matches(password, hash)) {
                        session.sendMessage(new TextMessage("incorrect password"));
                        session.close(CloseStatus.BAD_DATA);
                    } else {
                        UUID uuid = generator.use();
                        opr.save(new JoinedPlayer(username,password_hash,uuid.toString()));
                        session.sendMessage(new TextMessage(uuid.toString()));
                    }
                }
            }

            //below normal login more handling
            //todo finish game protocol handling
        } else {
            session.sendMessage(new TextMessage("illegal argument passed in 3"));
            session.close(CloseStatus.BAD_DATA);
        }
    }
}
