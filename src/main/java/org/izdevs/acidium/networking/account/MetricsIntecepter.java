package org.izdevs.acidium.networking.account;

import com.google.gson.Gson;
import org.izdevs.acidium.Metrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class MetricsIntecepter implements HandshakeInterceptor {
    Logger logger = LoggerFactory.getLogger("AccountServiceMetricsLogger");
    Gson gson = new Gson();
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Metrics.apiRequests.increment();
        logger.info("Handshake received");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        logger.info("HandShake Complete");
    }
}
