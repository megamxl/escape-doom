package com.escapedoom.gamesession.dataaccess.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}