package com.escapedoom.gamesession.configuration.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

@Configuration
public class RedisConfig {
    @Bean
    @Primary
    public SessionRepository redisIndexedSessionRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisIndexedSessionRepository(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, Object> redisDefaultTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new JdkSerializationRedisSerializer());
        return template;
    }
}