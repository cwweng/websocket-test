package net.cwweng.websocket.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Set;

public class TextWebSocketHandler implements WebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(TextWebSocketHandler.class);

    private final Set<WebSocketSession> sessions;

    public TextWebSocketHandler(Set<WebSocketSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(session.receive()
                .map(message -> {
                    switch (message.getType()) {
                        case TEXT:
                            return message.getPayloadAsText();
                        default:
                            return "unsupported message";
                    }
                }).doOnNext(content -> logger.info("{} received {}", session.getId(), content))
                .map(session::textMessage));
    }
}
