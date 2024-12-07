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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService {

    private final SessionManagementRepository sessionManagementRepository;
    private final OpenLobbyRepository openLobbyRepository;
    private final EscapeRoomRepository escapeRoomRepo;

    public StageResponse getPlayerStage(String httpSession) {

        var curr = sessionManagementRepository.findPlayerByHttpSessionID(httpSession);
        if (curr.isPresent()) {
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                if (lobbyId.get().getState() == EscapeRoomState.PLAYING) {
                    return StageResponse.builder()
                            .stage(escapeRoomRepo.getEscapeRoomStageByEscaperoomIDAndStageNumber(curr.get().getEscampeRoom_room_id(), curr.get().getEscaperoomStageId()))
                            .roomID(lobbyId.get().getLobbyId())
                            .state(lobbyId.get().getState()).build();
                } else if (lobbyId.get().getState() == EscapeRoomState.JOINABLE) {
                    return StageResponse.builder()
                            .stage(new ArrayList<>())
                            .state(lobbyId.get().getState())
                            .roomID(lobbyId.get().getLobbyId())
                            .build();
                }
            }
        }
        return StageResponse.builder()
                .stage(new ArrayList<>())
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }

    public StatusReturn getPlayerStatus(String playerID) {
        var curr = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
        if (curr.isPresent()) {
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                return StatusReturn.builder()
                        .state(lobbyId.get().getState())
                        .roomID(lobbyId.get().getLobbyId())
                        .build();
            }
        }
        return StatusReturn.builder()
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }


}
