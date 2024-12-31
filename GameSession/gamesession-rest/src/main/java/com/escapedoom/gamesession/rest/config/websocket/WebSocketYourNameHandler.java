package com.escapedoom.gamesession.rest.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketYourNameHandler extends AbstractWebSocketHandler {
    private final Set<WebSocketSession> sessions =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Client connected: " + session.getId());
        //broadcast("welcome - " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("Client disconnected: " + session.getId());
    }

    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("Error sending message to client: " + session.getId());
            }
        }
    }

    public void unicast(String sessionId, String message) {
      Optional<WebSocketSession> sessionToSend  =  sessions.stream().filter(session -> session.getId().equals(sessionId)).findFirst();
        System.out.println("bin da und will: " + sessionId);
        System.out.println("es gibt:");
        sessions.forEach(session -> {
            System.out.println(session.getId());
        });
      if (sessionToSend.isPresent()) {
          try {
              sessionToSend.get().sendMessage(new TextMessage(message));
          } catch (IOException e) {
              log.error("Error sending message to client: " + sessionId);
          }
      }
    }
}
