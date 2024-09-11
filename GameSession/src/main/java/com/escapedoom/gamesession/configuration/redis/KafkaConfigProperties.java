package com.escapedoom.gamesession.configuration.redis;


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
@AllArgsConstructor
@Builder
public class KafkaConfigProperties {

    private String codeCompilerTopic;

}
