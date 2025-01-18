package com.escapedoom.gamesession.rest.config.websocket;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.service.NotificationWsService;
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
public class WebSocketYourNameHandler extends AbstractWebSocketHandler {
    private final Set<WebSocketSession> sessions =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    private HashMap<String,WebSocketSession> sessionsMap = new HashMap<>();


    private final NotificationWsService notificationWsService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Client connected: " + session.getId());
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        if (query != null && !query.isEmpty()) {
            query = query.replace("sessionID=","");
            sessionsMap.put(query, session);
        }
        Player myName = notificationWsService.getYourName(query);
        if(myName != null) {
            unicast(sessionsMap.get(query).getId(), myName.getName());
            log.info("Your name is: " + myName.getName());
            String allNames = notificationWsService.getAllNames(myName);
            System.out.println(allNames);
            //broadcast(allNames);
        } else {
            log.info("Your name is null");
        }
        //broadcast("welcome - " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);

        for (Map.Entry<String, WebSocketSession> entry : sessionsMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                sessionsMap.remove(entry.getKey());
                log.info("Removed session with key: " + entry.getKey());
                break;
            }
        }
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
        System.out.println("message: " + message);
        System.out.println("sessionID: " + sessionId);
        System.out.println("sessionToSend: " + sessionToSend);
        System.out.println("alle::");
        sessions.forEach(session -> {
            System.out.println(session.getId());
        });
      if (sessionToSend.isPresent()) {
          try {
              System.out.println("ill send");
              sessionToSend.get().sendMessage(new TextMessage(message));
          } catch (IOException e) {
              log.error("Error sending message to client: " + sessionId);
          }
      }
    }
}
