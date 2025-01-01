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
import java.util.NoSuchElementException;
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
        try {
            Optional<OpenLobbys> lobbyOpt = openLobbyRepository.findByLobbyId(escaperoomSession);
            OpenLobbys lobby = null;
            if (lobbyOpt.isEmpty()) {
                log.info("User with session {} tried to join an invalid escaperoomSession {}", httpSessionID, escaperoomSession);
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
                if (lobby == null) lobby = new OpenLobbys();
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
                    log.trace("New Player created for httpSessionId: {} in escaperoomSession {}", httpSessionID, escaperoomSession);
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
        } catch (Exception ex) {
            log.error("Error occurred while managing lobby join for session {} and escaperoomSession {}: {}", httpSessionID, escaperoomSession, ex.getMessage(), ex);
            throw new RuntimeException("Failed to manage lobby join for escaperoomSession: " + escaperoomSession, ex);
        }
    }

    public List<Player> getAllPlayersInLobby(Long id) {
        try {
            return sessionManagementRepository.findAllByEscaperoomSession(id)
                    .orElseThrow(() -> new NoSuchElementException("No players found for EscapeRoom session ID: " + id));
        } catch (NoSuchElementException ex) {
            log.error("Error fetching players for EscapeRoom session ID: {}", id, ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error while fetching players for EscapeRoom session ID: {}", id, ex);
            throw new RuntimeException("Failed to fetch players for EscapeRoom session ID: " + id, ex);
        }
    }

    @Transactional
    public String deleteAllPlayersInLobby(Long id) {
        try {
            sessionManagementRepository.deleteAllByEscaperoomSession(id);
            log.info("Successfully deleted all players in EscapeRoom session ID: {}", id);
            return "done";
        } catch (Exception ex) {
            log.error("Error deleting players for EscapeRoom session ID: {}", id, ex);
            throw new RuntimeException("Failed to delete players for EscapeRoom session ID: " + id, ex);
        }
    }
}
