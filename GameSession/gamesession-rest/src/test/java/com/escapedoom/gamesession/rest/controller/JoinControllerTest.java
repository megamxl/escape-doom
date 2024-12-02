package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.code.CState;
import com.escapedoom.gamesession.rest.model.code.CodeCompilingRequestEvent;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.rest.model.response.StatusReturn;
import com.escapedoom.gamesession.rest.service.PlayerStateManagementService;
import com.escapedoom.gamesession.rest.util.SseEmitterExtended;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import jakarta.persistence.EntityManagerFactory;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void getAll_ShouldReturnListOfPlayers() throws Exception {
        List<Player> mockPlayers = List.of(
                new Player(1, "Player1", "mock-session-id", null, null, null, 100L, 1L),
                new Player(2, "Player2", "mock-session-id2", null, null, null, 90L, 2L)
        );

        Mockito.when(playerStateManagementService.getAllPlayersByEscapeRoomID(1L))
                .thenReturn(mockPlayers);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_ALL_URL.replace("{escaperoom_id}", "1"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sessionId_ShouldReturnJoinResponse() throws Exception {
        JoinResponse mockResponse = JoinResponse.builder()
                .sessionId("mock-session-id")
                .name("Mock Escape Room")
                .state(EscapeRoomState.JOINABLE)
                .build();

        Mockito.when(playerStateManagementService.mangeStateBySessionID("mock-session-id", 1L))
                .thenReturn(mockResponse);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("SESSION", "mock-session-id");

        mockMvc.perform(get(Constants.API_JOIN_PATH + "/1")
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void lobby_ShouldReturnSseEmitter() throws Exception {
        SseEmitterExtended mockEmitter = new SseEmitterExtended();

        Mockito.when(playerStateManagementService.lobbyConnection("mock-session-id"))
                .thenReturn(mockEmitter);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.LOBBY_URL.replace("{id}", "mock-session-id"))
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void currentStage_ShouldReturnStageResponse() throws Exception {
        StageResponse mockStageResponse = StageResponse.builder()
                .roomID(1L)
                .build();

        Mockito.when(playerStateManagementService.returnStageToPlayer("mock-session-id"))
                .thenReturn(mockStageResponse);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_STAGE_URL.replace("{httpSession}", "mock-session-id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAll_ShouldReturnSuccessMessage() throws Exception {
        Mockito.when(playerStateManagementService.deleteAllPlayersByEscaperoomID(1L))
                .thenReturn("Deleted all players for escaperoom_id 1");

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.DELETE_URL.replace("{escaperoom_id}", "1"))
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }


    @Test
    void submitCode_ShouldStartCompiling() throws Exception {
        CodeCompilingRequestEvent mockEvent = CodeCompilingRequestEvent.builder()
                .codeRiddleID(1L)
                .code("print('Hello World')")
                .build();

        mockMvc.perform(post(Constants.API_JOIN_PATH + Constants.SUBMIT_CODE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "playerId": "mock-player-id",
                                    "code": "print('Hello World')"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void getCode_ShouldReturnCodeStatus() throws Exception {
        CodeStatus mockStatus = CodeStatus.builder()
                .output("mock-player-id")
                .status(CState.valueOf("SUCCESS"))
                .output("Hello World")
                .build();

        Mockito.when(playerStateManagementService.getResult("mock-player-id"))
                .thenReturn(mockStatus);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_CODE_URL.replace("{playerID}", "mock-player-id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCurrentStatus_ShouldReturnStatusReturn() throws Exception {
        StatusReturn mockStatusReturn = StatusReturn.builder()
                .roomID(1L)
                .build();

        Mockito.when(playerStateManagementService.getCurrentStatus("mock-player-id"))
                .thenReturn(mockStatusReturn);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.STATUS_URL.replace("{playerID}", "mock-player-id"))
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
