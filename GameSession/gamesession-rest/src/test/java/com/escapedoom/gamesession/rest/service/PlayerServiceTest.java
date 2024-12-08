package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private SessionManagementRepository sessionManagementRepository;
    @Mock
    private OpenLobbyRepository openLobbyRepository;


    @InjectMocks
    private PlayerService playerService;

    private static final String HTTP_SESSION_ID = "testSessionId";
    private static final Long ESCAPE_ROOM_ID = 1L;


    @Test
    void testGetPlayerStatus_WhenLobbyIsPlaying() {
        Player player = new Player();
        player.setEscaperoomSession(ESCAPE_ROOM_ID);
        when(sessionManagementRepository.findPlayerByHttpSessionID(HTTP_SESSION_ID)).thenReturn(Optional.of(player));

        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobbyId(ESCAPE_ROOM_ID);
        lobby.setState(EscapeRoomState.PLAYING);
        when(openLobbyRepository.findByLobbyId(ESCAPE_ROOM_ID)).thenReturn(Optional.of(lobby));

        var response = playerService.getPlayerStatus(HTTP_SESSION_ID);

        assertThat(response).isNotNull();
        assertThat(response.getState()).isEqualTo(EscapeRoomState.PLAYING);
        assertThat(response.getRoomID()).isEqualTo(ESCAPE_ROOM_ID);
    }
}
