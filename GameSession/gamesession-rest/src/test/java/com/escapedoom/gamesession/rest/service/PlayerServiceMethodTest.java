package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.EscapeRoomRepository;
import com.escapedoom.gamesession.dataaccess.OpenLobbyRepository;
import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PlayerServiceMethodTest {

    @Mock
    private SessionManagementRepository sessionManagementRepository;

    @Mock
    private OpenLobbyRepository openLobbyRepository;

    @Mock
    private EscapeRoomRepository escapeRoomRepo;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPlayerStage_PlayerFound_LobbyPlaying() {
        // Arrange
        String httpSession = "session123";
        Player player = new Player();
        player.setEscaperoomSession(1L);
        player.setEscampeRoom_room_id(100L);
        player.setEscaperoomStageId(1L);

        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobbyId(1L);
        lobby.setState(EscapeRoomState.PLAYING);

        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSession)).thenReturn(Optional.of(player));
        when(openLobbyRepository.findByLobbyId(1L)).thenReturn(Optional.of(lobby));
        when(escapeRoomRepo.getEscapeRoomStageByEscaperoomIDAndStageNumber(100L, 1L)).thenReturn(new ArrayList<>(List.of("Stage Details")));

        // Act
        StageResponse response = playerService.getPlayerStage(httpSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getRoomID()).isEqualTo(1L);
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.PLAYING);
                    assertThat(res.getStage()).containsExactly("Stage Details");
                });

        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSession);
        verify(openLobbyRepository, times(1)).findByLobbyId(1L);
        verify(escapeRoomRepo, times(1)).getEscapeRoomStageByEscaperoomIDAndStageNumber(100L, 1L);
    }

    @Test
    void testGetPlayerStage_PlayerFound_LobbyJoinable() {
        // Arrange
        String httpSession = "session123";
        Player player = new Player();
        player.setEscaperoomSession(1L);

        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobbyId(1L);
        lobby.setState(EscapeRoomState.JOINABLE);

        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSession)).thenReturn(Optional.of(player));
        when(openLobbyRepository.findByLobbyId(1L)).thenReturn(Optional.of(lobby));

        // Act
        StageResponse response = playerService.getPlayerStage(httpSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getRoomID()).isEqualTo(1L);
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.JOINABLE);
                    assertThat(res.getStage()).isEmpty();
                });

        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSession);
        verify(openLobbyRepository, times(1)).findByLobbyId(1L);
        verifyNoInteractions(escapeRoomRepo);
    }

    @Test
    void testGetPlayerStage_PlayerFound_LobbyNotFound() {
        // Arrange
        String httpSession = "session123";
        Player player = new Player();
        player.setEscaperoomSession(1L);

        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSession)).thenReturn(Optional.of(player));
        when(openLobbyRepository.findByLobbyId(1L)).thenReturn(Optional.empty());

        // Act
        StageResponse response = playerService.getPlayerStage(httpSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.STOPPED);
                    assertThat(res.getRoomID()).isNull();
                    assertThat(res.getStage()).isEmpty();
                });

        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSession);
        verify(openLobbyRepository, times(1)).findByLobbyId(1L);
        verifyNoInteractions(escapeRoomRepo);
    }

    @Test
    void testGetPlayerStage_PlayerNotFound() {
        // Arrange
        String httpSession = "session123";

        when(sessionManagementRepository.findPlayerByHttpSessionID(httpSession)).thenReturn(Optional.empty());

        // Act
        StageResponse response = playerService.getPlayerStage(httpSession);

        // Assert
        assertThat(response)
                .isNotNull()
                .satisfies(res -> {
                    assertThat(res.getState()).isEqualTo(EscapeRoomState.STOPPED);
                    assertThat(res.getRoomID()).isNull();
                    assertThat(res.getStage()).isEmpty();
                });

        verify(sessionManagementRepository, times(1)).findPlayerByHttpSessionID(httpSession);
        verifyNoInteractions(openLobbyRepository);
        verifyNoInteractions(escapeRoomRepo);
    }
}
