package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.leaderboard.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LeaderboardServiceTest {

    @Mock
    private SessionManagementRepository repository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getScoreBoard_ShouldReturnLeaderboard() {
        // Arrange
        Long escaperoomID = 1L;
        List<Player> mockPlayers = List.of(
                new Player(1, "Player1", "session1", escaperoomID, null, null, 100L, 1L),
                new Player(2, "Player2", "session2", escaperoomID, null, null, 90L, 2L)
        );
        when(repository.findAllByEscaperoomSession(escaperoomID)).thenReturn(Optional.of(mockPlayers));

        // Act
        List<LeaderboardEntry> leaderboard = leaderboardService.getScoreBoard(escaperoomID);

        // Assert
        assertThat(leaderboard)
                .hasSize(2)
                .extracting("playerName", "score")
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple("Player1", 100L),
                        org.assertj.core.groups.Tuple.tuple("Player2", 90L)
                );
    }

    @Test
    void getScoreBoard_ShouldReturnEmptyList_WhenNoPlayersFound() {
        // Arrange
        Long escaperoomID = 1L;
        when(repository.findAllByEscaperoomSession(escaperoomID)).thenReturn(Optional.empty());

        // Act
        List<LeaderboardEntry> leaderboard = leaderboardService.getScoreBoard(escaperoomID);

        // Assert
        assertThat(leaderboard).isEmpty();
    }

    @Test
    void testGetScoreBoard_WithPlayers() {
        // Arrange
        Long escaperoomID = 1L;
        Player player1 = new Player(1, "Player1", "session1", escaperoomID, null, null, 80L, 1L);
        Player player2 = new Player(2, "Player2", "session2", escaperoomID, null, null, 75L, 2L);

        when(repository.findAllByEscaperoomSession(escaperoomID))
                .thenReturn(Optional.of(List.of(player1, player2)));

        // Act
        List<LeaderboardEntry> result = leaderboardService.getScoreBoard(escaperoomID);

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .extracting("playerName", "score")
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple("Player1", 80L),
                        org.assertj.core.groups.Tuple.tuple("Player2", 75L)
                );

        verify(repository, times(1)).findAllByEscaperoomSession(escaperoomID);
    }

    @Test
    void testGetScoreBoard_NoPlayers() {
        // Arrange
        Long escaperoomID = 1L;

        when(repository.findAllByEscaperoomSession(escaperoomID))
                .thenReturn(Optional.empty());

        // Act
        List<LeaderboardEntry> result = leaderboardService.getScoreBoard(escaperoomID);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();

        verify(repository, times(1)).findAllByEscaperoomSession(escaperoomID);
        verifyNoMoreInteractions(repository);
    }
}
