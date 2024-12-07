package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.*;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.config.MockTestConfig;
import com.escapedoom.gamesession.rest.model.leaderboard.LeaderboardEntry;
import com.escapedoom.gamesession.rest.service.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockTestConfig.class)
@WebMvcTest(controllers = LeaderboardController.class)
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

    @MockBean
    private CompilationService compilationService;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private SessionManagementRepository sessionManagementRepository;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private OpenLobbyRepository openLobbyRepository;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private EscapeRoomRepository escapeRoomRepository;

    @MockBean
    private RedisConnectionFactory redisConnectionFactory;

    @MockBean
    private RedisTemplate<?, ?> redisTemplate;

    @MockBean
    private RedisIndexedSessionRepository redisIndexedSessionRepository;

    @MockBean
    private SessionRepositoryFilter<?> sessionRepositoryFilter;

    @MockBean
    private CompilationRepository compilationRepository;

    @MockBean
    private CodeRiddleRepository codeRiddleRepository;

    @Test
    void leaderboardAsJson_ShouldReturnLeaderboard() throws Exception {
        Player player1 = new Player();
        player1.setName("Player1");
        player1.setScore(100L);
        player1.setLastStageSolved(3600L);

        Player player2 = new Player();
        player2.setName("Player2");
        player2.setScore(90L);
        player2.setLastStageSolved(4000L);

        List<LeaderboardEntry> mockLeaderboard = List.of(
                new LeaderboardEntry(player1),
                new LeaderboardEntry(player2)
        );

        Mockito.when(leaderboardService.getScoreBoard(anyLong())).thenReturn(mockLeaderboard);

        mockMvc.perform(get("/api/leaderboard/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
