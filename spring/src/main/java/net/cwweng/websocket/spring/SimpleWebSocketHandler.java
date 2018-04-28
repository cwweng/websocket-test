package net.cwweng.websocket.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

public class SimpleWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(SimpleWebSocketHandler.class);

    private Set<WebSocketSession> wsSessions;

    public SimpleWebSocketHandler(Set<WebSocketSession> wsSessions) {

        this.wsSessions = wsSessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        wsSessions.add(session);

        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String content = message.getPayload();

        logger.info("{} received {}", session.getId(), content);

        wsSessions.forEach(ws -> {
            try {
                ws.sendMessage(new TextMessage(content));
            } catch (IOException e) {
                logger.error("{} exception : {}", ws.getId(), e.toString());
            }
        });
    }
}
