package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.config.websocket.WebSocketConfig;
import com.escapedoom.gamesession.rest.config.websocket.WebSocketStartedHandler;
import com.escapedoom.gamesession.rest.config.websocket.WebSocketYourNameHandler;
import com.escapedoom.gamesession.rest.util.SseEmitterExtended;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationWsService {
    private final SessionManagementRepository sessionManagementRepository;
    private final OpenLobbyRepository openLobbyRepository;
    private final WebSocketStartedHandler webSocketStartedHandler;



    boolean update = false;

    public Player getYourName(String httpId) {
        Player player = null;
        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpId);
        if (optplayer.isPresent()) {
            player = optplayer.get();
        }
        return player;
    }

    public String getAllNames(Player player) {
        var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
        var jsonPlayers = new JSONObject();
        if (players.isPresent()) {
            Player finalPlayer = player;
            players.get().stream()
                    .filter(player1 -> Objects.equals(player1.getEscaperoomSession(), finalPlayer.getEscaperoomSession()))
                    .forEach(filteredPlayer -> {
                    });
            jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
        }
        return jsonPlayers.toString();
    }

    //Bei einwählen in eine Lobby
    public void establishLobbyEmitters(String httpId) {
        System.out.println("Establish Lobby emitters for " + httpId );
        Player player = null;
        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpId);
        if (optplayer.isPresent()) {
            player = optplayer.get();
        }
        if(player != null)
        try {
            //zuerst den senden als myName player.getName()
            //webSocketConfig.getWebSocketYourNameHandler().broadcast(player.getName());
            //webSocketConfig.getWebSocketYourNameHandler().unicast(httpId, player.getName());
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            var jsonPlayers = new JSONObject();
            if (players.isPresent()) {
                Player finalPlayer = player;
                players.get().stream()
                        .filter(player1 -> Objects.equals(player1.getEscaperoomSession(), finalPlayer.getEscaperoomSession()))
                        .forEach(filteredPlayer -> {
                            // Add your processing logic here
                        });
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            //dann das senden als AllName jsonPlayers.toString()
            //webSocketConfig.getWebSocketAllNamesHandler().broadcast(jsonPlayers.toString());
            //sseEmitter.send(SseEmitter.event().name(ALL_NAME_EVENT).data(jsonPlayers.toString()));
        } catch (Exception e) {
            log.error("Error while establishing lobby emitters. PlayerSessionId: {}, EscapeRoomSession: {}, PlayerName: {}, ErrorMessage: {}",
                    httpId,
                    player.getEscaperoomSession(),
                    player.getName(),
                    e.getMessage(),
                    e);
        }
        if (update) {
            notifyAllPlayersInSession(player, false);
            update = false;
        }
    }

    public void notifyAllPlayersInSession(Player player, Boolean playing) {
        var jsonPlayers = new JSONObject();
        if (playing) {
            String START_PLAYING_EVENT = "started";

            webSocketStartedHandler.broadcast(String.valueOf(new JSONObject()));

            //notifyClients(player.getEscaperoomSession(), START_PLAYING_EVENT, new JSONObject());
        } else {
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            if (players.isPresent()) {
                try {
                    jsonPlayers.put(
                            "players",
                            players.get().stream()
                                    .filter(Objects::nonNull)
                                    .map(Player::getName)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList())
                    );
                } catch (org.json.JSONException e) {
                    log.error("Error adding players to JSON object", e);
                }
            }
            //notifyClients(player.getEscaperoomSession(), ALL_NAME_EVENT, jsonPlayers.toString());
        }
    }


    public void notifyEscapeRoomStart(Long id) {
        openLobbyRepository.findByLobbyId(id).ifPresent(lobby -> {
            if (lobby.getState() == EscapeRoomState.PLAYING) {

                Optional<List<Player>> allByEscaperoomSession = sessionManagementRepository.findAllByEscaperoomSession(id);
                if (allByEscaperoomSession.isPresent()) {

                    // Added this since a partial restart of the system caused no new players to join :(
                    if (!allByEscaperoomSession.get().isEmpty()) {
                        notifyAllPlayersInSession(allByEscaperoomSession.get().get(0), true);
                        log.info("informing clients");
                    } else {
                        log.info("No player found!");
                    }
                }

            }
        });
    }
}
