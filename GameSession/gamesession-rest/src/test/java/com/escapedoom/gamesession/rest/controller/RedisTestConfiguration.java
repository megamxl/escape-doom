package com.escapedoom.gamesession.rest.controller;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

@TestConfiguration
public class RedisTestConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return Mockito.mock(RedisConnectionFactory.class);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        return Mockito.mock(RedisTemplate.class);
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
