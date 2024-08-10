package org.izdevs.acidium.networking;

import jakarta.websocket.server.ServerEndpoint;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Set;

@Configuration
@EnableWebSocket
public class ServerEndpointConfigurator implements WebSocketConfigurer {
    @Autowired
    Set<WebSocketHandler> handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        for(int i=0;i<=handler.size()-1;i++){
            WebSocketHandler _this = handler.iterator().next();

            registry.addHandler(_this,_this.getClass().getAnnotation(ServerEndpoint.class).value());
        }
    }
}
