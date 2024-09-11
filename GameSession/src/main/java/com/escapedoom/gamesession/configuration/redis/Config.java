package com.escapedoom.gamesession.configuration.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.FlushMode;
import org.springframework.session.SaveMode;
import org.springframework.session.data.redis.config.annotation.web.http.AbstractRedisHttpSessionConfiguration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

import java.time.Duration;

@Configuration
public class Config extends RedisHttpSessionConfiguration {

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    @Value("${spring.session.timeout}")
    private Duration inactiveInterval;

    @Override
    protected Duration getMaxInactiveInterval() {
        return inactiveInterval;
    }

    @Override
    protected String getRedisNamespace() {
        return namespace;
    }

    // you can remove it if you accept the defaults below
    @Override
    protected FlushMode getFlushMode() {
        return FlushMode.ON_SAVE;
    }

    @Override
    protected SaveMode getSaveMode() {
        return SaveMode.ON_SET_ATTRIBUTE;
    }

}
