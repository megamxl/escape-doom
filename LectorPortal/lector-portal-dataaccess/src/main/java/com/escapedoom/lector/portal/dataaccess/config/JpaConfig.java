package com.escapedoom.lector.portal.dataaccess.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.escapedoom.lector.portal.dataaccess")
@EntityScan(basePackages = "com.escapedoom.lector.portal.dataaccess.entity")
public class JpaConfig {
}
