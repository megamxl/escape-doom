package com.escapedoom.auth;

import com.escapedoom.auth.Service.EscaperoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LectorPortalBackend implements CommandLineRunner {

    @Autowired
    EscaperoomService escaperoomService;

    public static void main(String[] args)
    {
        SpringApplication.run(LectorPortalBackend.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate6Module());
        return objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
//        escaperoomService.createADummyRoom();
    }
}
