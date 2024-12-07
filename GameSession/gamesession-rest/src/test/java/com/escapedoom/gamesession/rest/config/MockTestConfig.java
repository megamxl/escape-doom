package com.escapedoom.gamesession.rest.config;

import com.escapedoom.gamesession.dataaccess.*;
import com.escapedoom.gamesession.rest.service.PlayerService;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

@TestConfiguration
public class MockTestConfig {

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private SessionManagementRepository sessionManagementRepository;

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
}
