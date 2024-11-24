package com.escapedoom.gamesession.dataaccess.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.escapedoom.gamesession.dataaccess")
@EntityScan(basePackages = "com.escapedoom.gamesession.dataaccess.entity")
public class JpaConfig {
}
