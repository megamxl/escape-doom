package com.escapedoom.lector.portal.rest.util;


import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import com.escapedoom.lector.portal.dataaccess.LobbyRepository;
import com.escapedoom.lector.portal.dataaccess.entity.OpenLobbys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomStopper {

    private final LobbyRepository lobbyRepository;

    @Scheduled(fixedRate = 60000)
    public void HouseKeeper() {

        Optional<List<OpenLobbys>> allByStatePlaying = lobbyRepository.findAllByStatePlaying();
        if (allByStatePlaying.isEmpty()) {
            log.debug("No room to Stop");
            return;
        }
        for (OpenLobbys openLobby : allByStatePlaying.get()) {
            if (LocalDateTime.now().isAfter(openLobby.getEndTime())) {
                openLobby.setState(EscapeRoomState.STOPPED);
                lobbyRepository.save(openLobby);
                log.info("Stopped Room {}", openLobby.getLobby_Id());
            }
        }
    }
}