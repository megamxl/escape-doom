package com.escapedoom.gamesession.rest.config.redis;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "escapedoom.codecompiler")
@Configuration
@Data
@NoArgsConstructor
public class KafkaConfigProperties {

    private String codeCompilerTopic;

}
