package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class NotificationServiceMethodTest {

    @Mock
    private OpenLobbyRepository openLobbyRepository;

    @Mock
    private SessionManagementRepository sessionManagementRepository;

    @InjectMocks
    private NotificationService escapeRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNotifyEscapeRoomStart_WithPlayers() {
        // Arrange
        Long lobbyId = 1L;
        OpenLobbys lobby = new OpenLobbys();
        lobby.setState(EscapeRoomState.PLAYING);

        Player player1 = new Player(); // Set up fields as needed
        when(openLobbyRepository.findByLobbyId(lobbyId)).thenReturn(Optional.of(lobby));
        when(sessionManagementRepository.findAllByEscaperoomSession(lobbyId))
                .thenReturn(Optional.of(List.of(player1)));

        // Mock notifyAllPlayersInSession method if it's external or private
        NotificationService spyService = spy(escapeRoomService);
        doNothing().when(spyService).notifyAllPlayersInSession(player1, true);

        // Act
        spyService.notifyEscapeRoomStart(lobbyId);

        // Assert
        verify(openLobbyRepository, times(1)).findByLobbyId(lobbyId);
        verify(sessionManagementRepository, times(1)).findAllByEscaperoomSession(lobbyId);
        verify(spyService, times(1)).notifyAllPlayersInSession(player1, true);
    }

    @Test
    void testNotifyEscapeRoomStart_NoPlayers() {
        // Arrange
        Long lobbyId = 1L;
        OpenLobbys lobby = new OpenLobbys();
        lobby.setState(EscapeRoomState.PLAYING);

        when(openLobbyRepository.findByLobbyId(lobbyId)).thenReturn(Optional.of(lobby));
        when(sessionManagementRepository.findAllByEscaperoomSession(lobbyId))
                .thenReturn(Optional.of(List.of())); // Empty list of players

        // Act
        escapeRoomService.notifyEscapeRoomStart(lobbyId);

        // Assert
        verify(openLobbyRepository, times(1)).findByLobbyId(lobbyId);
        verify(sessionManagementRepository, times(1)).findAllByEscaperoomSession(lobbyId);
        verifyNoMoreInteractions(sessionManagementRepository); // No further interactions beyond the query
    }

    @Test
    void testNotifyEscapeRoomStart_NotPlayingState() {
        // Arrange
        Long lobbyId = 1L;
        OpenLobbys lobby = new OpenLobbys();
        lobby.setState(EscapeRoomState.JOINABLE);

        when(openLobbyRepository.findByLobbyId(lobbyId)).thenReturn(Optional.of(lobby));

        // Act
        escapeRoomService.notifyEscapeRoomStart(lobbyId);

        // Assert
        verify(openLobbyRepository, times(1)).findByLobbyId(lobbyId);
        verifyNoInteractions(sessionManagementRepository);
    }

    @Test
    void testNotifyEscapeRoomStart_LobbyNotFound() {
        // Arrange
        Long lobbyId = 1L;

        when(openLobbyRepository.findByLobbyId(lobbyId)).thenReturn(Optional.empty());

        // Act
        escapeRoomService.notifyEscapeRoomStart(lobbyId);

        // Assert
        verify(openLobbyRepository, times(1)).findByLobbyId(lobbyId);
        verifyNoInteractions(sessionManagementRepository);
    }


    @Test
    void testNotifyAllPlayersInSession_PlayingTrue() {
        // Arrange
        Player player = new Player();
        player.setEscaperoomSession(1L);

        NotificationService spyService = spy(escapeRoomService);

        doNothing().when(spyService).notifyClients(eq(1L), eq("started"), any(JSONObject.class));

        // Act
        spyService.notifyAllPlayersInSession(player, true);

        // Assert
        verify(spyService, times(1)).notifyClients(eq(1L), eq("started"), any(JSONObject.class));
        verifyNoInteractions(sessionManagementRepository);
    }

    @Test
    void testNotifyAllPlayersInSession_PlayingFalse_WithPlayers() {
        // Arrange
        Player player = new Player();
        player.setEscaperoomSession(1L);

        Player player1 = new Player();
        player1.setName("Alice");
        Player player2 = new Player();
        player2.setName("Bob");

        when(sessionManagementRepository.findAllByEscaperoomSession(1L))
                .thenReturn(Optional.of(List.of(player1, player2)));

        NotificationService spyService = spy(escapeRoomService);

        doNothing().when(spyService).notifyClients(eq(1L), eq("all_names"), anyString());

        // Act
        spyService.notifyAllPlayersInSession(player, false);

        // Assert
        verify(sessionManagementRepository, times(1)).findAllByEscaperoomSession(1L);
    }

    @Test
    void testNotifyAllPlayersInSession_PlayingFalse_NoPlayers() {
        // Arrange
        Player player = new Player();
        player.setEscaperoomSession(1L);

        when(sessionManagementRepository.findAllByEscaperoomSession(1L))
                .thenReturn(Optional.empty());

        NotificationService spyService = spy(escapeRoomService);

        doNothing().when(spyService).notifyClients(eq(1L), eq("all_names"), anyString());

        // Act
        spyService.notifyAllPlayersInSession(player, false);

        // Assert
        verify(sessionManagementRepository, times(1)).findAllByEscaperoomSession(1L);
    }

}
