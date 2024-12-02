package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.service.PlayerStateManagementService;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JoinController.class)
@Import(JoinControllerTest.TestRedisConfig.class)
@ImportAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class
})
class JoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerStateManagementService playerStateManagementService;

    @MockBean
    private HttpSession httpSession;

    @Test
    void sessionId_ShouldReturnJoinResponse() throws Exception {
        // Arrange
        JoinResponse mockResponse = JoinResponse.builder()
                .sessionId("mock-session-id")
                .name("Mock Escape Room")
                .state(EscapeRoomState.JOINABLE)
                .build();

        Mockito.when(playerStateManagementService.mangeStateBySessionID("mock-session-id", 1L))
                .thenReturn(mockResponse);

        MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(get(Constants.API_JOIN_PATH + "/1")
                        .session(mockHttpSession)
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
