package com.escapedoom.gamesession.rest.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketStartedHandler extends AbstractWebSocketHandler {
    private final Set<WebSocketSession> sessions =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    private HashMap<String,WebSocketSession> sessionsMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (query != null && !query.isEmpty()) {
            query = query.replace("sessionID=","");
            sessionsMap.put(query, session);
        }
        log.debug("Client connected: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        for (Map.Entry<String, WebSocketSession> entry : sessionsMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                sessionsMap.remove(entry.getKey());
                log.debug("Removed session with key: " + entry.getKey());
                break;
            }
        }

        log.debug("Client disconnected: " + session.getId());
    }

    public void broadcast(String message, List<String> allIds) {

        for (String sessionId : allIds) {
            WebSocketSession session = sessionsMap.get(sessionId);
            if (session != null) {
                try {
                    session.sendMessage(new TextMessage(message));
                    log.debug("Sent message: " + message + " to session: " + session.getId());
                } catch (IOException e) {
                    log.error("Error sending message to client: " + session.getId(), e);
                }
            }
        }
    }
}
