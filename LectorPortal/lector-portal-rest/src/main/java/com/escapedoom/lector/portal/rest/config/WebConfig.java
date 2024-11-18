package com.escapedoom.lector.portal.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {
        "com.escapedoom.lector.portal.dataaccess",
        "com.escapedoom.lector.portal.rest",
        "com.escapedoom.lector.portal.shared"
})
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate6Module());
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/auth/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("POST", "GET", "OPTIONS")
                .allowCredentials(true);
    }
}
