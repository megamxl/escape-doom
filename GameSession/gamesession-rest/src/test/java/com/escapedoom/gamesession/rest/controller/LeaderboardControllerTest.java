package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.escaperoom.LeaderboardDao;
import com.escapedoom.gamesession.rest.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@WebMvcTest(controllers = LeaderboardController.class)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class
})
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

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

        List<LeaderboardDao> mockLeaderboard = List.of(
                new LeaderboardDao(player1),
                new LeaderboardDao(player2)
        );

        Mockito.when(leaderboardService.getScoreBoard(anyLong())).thenReturn(mockLeaderboard);

        mockMvc.perform(get("/api/leaderboard/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Configuration
    static class TestRedisConfig {
        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return Mockito.mock(RedisConnectionFactory.class);
        }

        @Bean
        public RedisTemplate<?, ?> redisTemplate() {
            RedisTemplate<?, ?> redisTemplate = Mockito.mock(RedisTemplate.class);
            return redisTemplate;
        }

        @Bean
        public RedisIndexedSessionRepository redisIndexedSessionRepository(RedisConnectionFactory redisConnectionFactory) {
            return Mockito.mock(RedisIndexedSessionRepository.class);
        }

        @Bean
        public SessionRepositoryFilter<?> sessionRepositoryFilter(RedisIndexedSessionRepository redisIndexedSessionRepository) {
            return Mockito.mock(SessionRepositoryFilter.class);
        }
    }
}
