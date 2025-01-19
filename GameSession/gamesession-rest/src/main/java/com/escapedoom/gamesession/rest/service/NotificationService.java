package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.config.websocket.WebSocketConfig;
import com.escapedoom.gamesession.rest.config.websocket.WebSocketStartedHandler;
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
public class NotificationService {

    private final SessionManagementRepository sessionManagementRepository;
    private final OpenLobbyRepository openLobbyRepository;
    //private final WebSocketConfig webSocketConfig;

    @Getter
    private final List<SseEmitterExtended> sseEmitters = new CopyOnWriteArrayList<>();

    // move to constants
    private final String ALL_NAME_EVENT = "allNames";

    boolean update = false;
    //Bei einwählen in eine Lobby
    public SseEmitterExtended establishLobbyEmitters(String httpId) {

        SseEmitterExtended sseEmitter = new SseEmitterExtended();
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmitters.remove(sseEmitter);
        });
        Player player ;
        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpId);
        if (optplayer.isPresent()) {
            player = optplayer.get();
        } else {
            return null;
        }

        sseEmitter.setHttpID(httpId);
        sseEmitter.setLobby_id(player.getEscaperoomSession());
        sseEmitter.setName(player.getName());
        sseEmitters.add(sseEmitter);

        try {
            String YOUR_NAME_EVENT = "yourName";
            sseEmitter.send(SseEmitter.event().name(YOUR_NAME_EVENT).data(sseEmitter.getName()));
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            var jsonPlayers = new JSONObject();
            if (players.isPresent()) {
                players.get().stream()
                        .filter(player1 -> Objects.equals(player1.getEscaperoomSession(), player.getEscaperoomSession()))
                        .forEach(filteredPlayer -> {
                            // Add your processing logic here
                        });
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            sseEmitter.send(SseEmitter.event().name(ALL_NAME_EVENT).data(jsonPlayers.toString()));
        } catch (Exception e) {
            log.error("Error while establishing lobby emitters. PlayerSessionId: {}, EscapeRoomSession: {}, PlayerName: {}, ErrorMessage: {}",
                    httpId,
                    player.getEscaperoomSession(),
                    player.getName(),
                    e.getMessage(),
                    e);
        }
        sseEmitter.onCompletion(() -> {
            synchronized (this.sseEmitters) {
                this.sseEmitters.remove(sseEmitter);
            }
        });
        if (update) {
            notifyAllPlayersInSession(player, false);
            update = false;
        }
        return sseEmitter;
    }

    public void notifyClients(Long esceproomId, String eventName, Object toSend) {
        if (sseEmitters.isEmpty()) {
            log.info("No active Emitters ");
            return;
        }

        ArrayList<SseEmitterExtended> failure = new ArrayList<>();
        for (SseEmitterExtended sseEmitterExtended : sseEmitters) {
            if (sseEmitterExtended != null) {
                try {
                    if (Objects.equals(sseEmitterExtended.getLobby_id(), esceproomId)) {
                        sseEmitterExtended.send(SseEmitter.event().name(eventName).data(toSend));
                        sseEmitterExtended.complete();
                    }
                } catch (Exception ignored) {
                    failure.add(sseEmitterExtended);
                }
                if (!failure.isEmpty()) {
                    sseEmitters.removeAll(failure);
                }
            }
        }
    }

    public void notifyAllPlayersInSession(Player player, Boolean playing) {
        var jsonPlayers = new JSONObject();
        if (playing) {
            String START_PLAYING_EVENT = "started";

            //webSocketConfig.getWebSocketStartedHandler().broadcast("helloouu");

            notifyClients(player.getEscaperoomSession(), START_PLAYING_EVENT, new JSONObject());
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
            notifyClients(player.getEscaperoomSession(), ALL_NAME_EVENT, jsonPlayers.toString());
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
