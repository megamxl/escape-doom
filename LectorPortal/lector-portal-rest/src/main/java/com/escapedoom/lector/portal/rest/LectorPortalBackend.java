package com.escapedoom.lector.portal.rest;

import com.escapedoom.lector.portal.rest.service.EscaperoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LectorPortalBackend implements CommandLineRunner {

    @Autowired
    private EscaperoomService escaperoomService;

    public static void main(String[] args) {
        SpringApplication.run(LectorPortalBackend.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        escaperoomService.createADummyRoom();
    }
}
