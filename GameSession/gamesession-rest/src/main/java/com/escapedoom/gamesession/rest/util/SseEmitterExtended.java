package com.escapedoom.gamesession.rest.util;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class SseEmitterExtended extends SseEmitter {

    private String httpID;

    private Long lobby_id;

    private String name;

    public void setHttpID(String httpID) {
        this.httpID = httpID;
    }

    public void setLobby_id(Long lobby_id) {
        this.lobby_id = lobby_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
