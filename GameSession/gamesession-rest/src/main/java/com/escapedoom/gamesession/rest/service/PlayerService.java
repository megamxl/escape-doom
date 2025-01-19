package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.EscapeRoomRepository;
import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.rest.model.response.StatusReturn;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

    private final SessionManagementRepository sessionManagementRepository;
    private final OpenLobbyRepository openLobbyRepository;
    private final EscapeRoomRepository escapeRoomRepo;

    public StageResponse getPlayerStage(String httpSession) {
        log.debug("Fetching player stage for HttpSession: {}", httpSession);

        var curr = sessionManagementRepository.findPlayerByHttpSessionID(httpSession);
        if (curr.isPresent()) {
            log.debug("Player found for HttpSession: {}", httpSession);
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                log.debug("Lobby found for player session: {} with state: {}", httpSession, lobbyId.get().getState());

                if (lobbyId.get().getState() == EscapeRoomState.PLAYING) {
                    log.debug("Lobby is in PLAYING state for HttpSession: {}", httpSession);
                    return StageResponse.builder()
                            .stage(escapeRoomRepo.getEscapeRoomStageByEscaperoomIDAndStageNumber(
                                    curr.get().getEscampeRoom_room_id(), curr.get().getEscaperoomStageId()))
                            .roomID(lobbyId.get().getLobbyId())
                            .state(lobbyId.get().getState()).build();
                } else if (lobbyId.get().getState() == EscapeRoomState.JOINABLE) {
                    log.warn("Lobby is in JOINABLE state for HttpSession: {}. Player might not have started the game.", httpSession);
                    return StageResponse.builder()
                            .stage(new ArrayList<>())
                            .state(lobbyId.get().getState())
                            .roomID(lobbyId.get().getLobbyId())
                            .build();
                } else {
                    log.warn("Unexpected state ({}) for Lobby in HttpSession: {}", lobbyId.get().getState(), httpSession);
                }
            } else {
                log.warn("No lobby found for player session: {}", httpSession);
            }
        } else {
            log.warn("No player found for HttpSession: {}", httpSession);
        }

        return StageResponse.builder()
                .stage(new ArrayList<>())
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }

    public StatusReturn getPlayerStatus(String playerID) {
        log.debug("Fetching player status for PlayerID: {}", playerID);

        var curr = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
        if (curr.isPresent()) {
            log.debug("Player found for PlayerID: {}", playerID);
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                log.debug("Lobby found for PlayerID: {} with state: {}", playerID, lobbyId.get().getState());
                return StatusReturn.builder()
                        .state(lobbyId.get().getState())
                        .roomID(lobbyId.get().getLobbyId())
                        .build();
            } else {
                log.warn("No lobby found for PlayerID: {}", playerID);
            }
        } else {
            log.warn("No player found for PlayerID: {}", playerID);
        }

        return StatusReturn.builder()
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }

    public String sessionToRoomPin(String httpSession) {
        return sessionManagementRepository.findEscapeRoomSessionByHttpSessionID(httpSession).orElseThrow(() -> new NoSuchElementException("No session found for HttpSession: " + httpSession));
    }
}
