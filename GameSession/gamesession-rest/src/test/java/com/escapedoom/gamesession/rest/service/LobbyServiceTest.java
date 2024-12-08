package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LobbyServiceTest {

    @Mock
    private SessionManagementRepository sessionManagementRepository;

    @Mock
    private OpenLobbyRepository openLobbyRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LobbyService lobbyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testManageLobbyJoin_PlayerExists_LobbyJoinable() {
        // Arrange
        String httpSessionID = "session123";
        Long escaperoomSession = 1L;

        Player player = new Player();
        player.setName("TestPlayer");
        player.setHttpSessionID(httpSessionID);

        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobbyId(escaperoomSession);
        lobby.setState(EscapeRoomState.JOINABLE);

        when(openLobbyRepository.findByLobbyId(escaperoomSession)).thenReturn(Optional.of(lobby));
        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID)).thenReturn(Optional.of(player));

        // Act
        JoinResponse response = lobbyService.manageLobbyJoin(httpSessionID, escaperoomSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.STOPPED);
                });

        verify(openLobbyRepository, times(1)).findByLobbyId(escaperoomSession);
        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSessionID);
        verifyNoMoreInteractions(sessionManagementRepository, openLobbyRepository, notificationService);
    }

    @Test
    void testManageLobbyJoin_PlayerDoesNotExist() {
        // Arrange
        String httpSessionID = "session123";
        Long escaperoomSession = 1L;

        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobbyId(escaperoomSession);
        lobby.setState(EscapeRoomState.JOINABLE);
        lobby.setEscaperoom_escaperoom_id(100L);

        when(openLobbyRepository.findByLobbyId(escaperoomSession)).thenReturn(Optional.of(lobby));
        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID)).thenReturn(Optional.empty());

        // Act
        JoinResponse response = lobbyService.manageLobbyJoin(httpSessionID, escaperoomSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.JOINABLE);
                    assertThat(res.getSessionId()).isEqualTo(httpSessionID);
                });

        verify(sessionManagementRepository, times(1)).save(any(Player.class));
        verify(openLobbyRepository, times(1)).findByLobbyId(escaperoomSession);
        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSessionID);
        verifyNoMoreInteractions(sessionManagementRepository, openLobbyRepository, notificationService);
    }
}
