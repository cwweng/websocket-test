package net.cwweng.websocket.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public Set<WebSocketSession> wsSessions() {
        return new HashSet<>();
    }

    public SimpleWebSocketHandler simpleWebSocketHandler() {
        return new SimpleWebSocketHandler(wsSessions());
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(simpleWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
