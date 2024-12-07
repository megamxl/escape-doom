package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.*;
import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.service.CompilationService;
import com.escapedoom.gamesession.rest.service.LobbyService;
import com.escapedoom.gamesession.rest.service.NotificationService;
import com.escapedoom.gamesession.rest.service.PlayerService;
import com.escapedoom.gamesession.rest.util.SseEmitterExtended;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private CompilationService compilationService;

    @MockBean
    private EntityManager entityManager;

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
    void lobby_ShouldReturnSseEmitter() throws Exception {
        SseEmitterExtended mockEmitter = new SseEmitterExtended();

        Mockito.when(notificationService.establishLobbyEmitters("mock-session-id"))
                .thenReturn(mockEmitter);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.LOBBY_URL.replace("{id}", "mock-session-id"))
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk());
    }

}
