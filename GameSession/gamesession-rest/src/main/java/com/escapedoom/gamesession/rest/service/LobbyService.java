package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.util.NameGeneratorUtil;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LobbyService {

    private final SessionManagementRepository sessionManagementRepository;
    private final OpenLobbyRepository openLobbyRepository;
    private final NotificationService notificationService;

    // used in notfication service as well
    boolean update = false;

    public JoinResponse manageLobbyJoin(String httpSessionID, Long escaperoomSession) {

        Optional<OpenLobbys> lobbyOpt = openLobbyRepository.findByLobbyId(escaperoomSession);
        OpenLobbys lobby = null;
        if (lobbyOpt.isEmpty()) {
            JoinResponse.builder()
                    .state(EscapeRoomState.STOPPED)
                    .name("")
                    .sessionId("")
                    .build();
        } else {
            lobby = lobbyOpt.get();
        }

        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        Player player;
        if (optplayer.isPresent()) {
            player = optplayer.get();
            if(lobby == null) lobby = new OpenLobbys();
            lobby.setState(EscapeRoomState.STOPPED);
            switch (lobby.getState()) {
                case JOINABLE -> {
                    notificationService.getSseEmitters().removeIf(sseEmitterExtended -> sseEmitterExtended.getHttpID().equals(httpSessionID));
                    return JoinResponse.builder()
                            .state(lobby.getState())
                            .sessionId(httpSessionID)
                            .name(player.getName())
                            .build();
                }
                case PLAYING -> {
                    return JoinResponse.builder()
                            .state(lobby.getState())
                            .sessionId(httpSessionID)
                            .name(player.getName())
                            .build();
                }
                case STOPPED -> {
                    JoinResponse.builder()
                            .state(lobby.getState())
                            .name("")
                            .sessionId("")
                            .build();
                }
            }
        } else {
            if (lobby != null) {
                player = Player.builder()
                        .name(NameGeneratorUtil.generate())
                        .escampeRoom_room_id(lobby.getEscaperoom_escaperoom_id())
                        .httpSessionID(httpSessionID)
                        .escaperoomSession(escaperoomSession)
                        .escaperoomStageId(1L)
                        .score(0L)
                        .lastStageSolved(null)
                        .build();
                sessionManagementRepository.save(player);
                update = true;
                return JoinResponse.builder()
                        .state(lobby.getState())
                        .sessionId(httpSessionID)
                        .name(player.getName())
                        .build();
            }
        }
        return JoinResponse.builder()
                .state(EscapeRoomState.STOPPED)
                .name("")
                .sessionId("")
                .build();
    }

    public List<Player> getAllPlayersInLobby(Long id) {
        return sessionManagementRepository.findAllByEscaperoomSession(id).orElseThrow();
    }

    @Transactional
    public String deleteAllPlayersInLobby(Long id) {
        sessionManagementRepository.deleteAllByEscaperoomSession(id);
        return "done";
    }
}
