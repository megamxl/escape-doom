package com.escapedoom.lector.portal.dataaccess.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.escapedoom.lector.portal",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JpaConfig.class)
)

@EntityScan(basePackages = "com.escapedoom.lector.portal.dataaccess.entity")
@EnableJpaRepositories(basePackages = "com.escapedoom.lector.portal.dataaccess")
public class TestApplication {
}


