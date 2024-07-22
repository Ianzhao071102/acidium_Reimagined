package org.izdevs.acidium.networking.account;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class AccountServiceWebsocketConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new AccountServiceWebSocketEndpoint(),"/ws/account")
                .addInterceptors(new MetricsIntecepter())
                .setAllowedOrigins("*");
    }
}
