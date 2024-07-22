package org.izdevs.acidium.networking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class ServerEndpointConfigurator {
    @Bean
    public ServerEndpointExporter exporter(){
        return new ServerEndpointExporter();
    }
}
