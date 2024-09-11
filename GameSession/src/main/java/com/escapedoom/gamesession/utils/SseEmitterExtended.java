package com.escapedoom.gamesession.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseEmitterExtended extends SseEmitter {

    private String httpID;

    private Long lobby_id;

    private String name;

    public String getHttpID() {
        return httpID;
    }

    public void setHttpID(String httpID) {
        this.httpID = httpID;
    }

    public Long getLobby_id() {
        return lobby_id;
    }

    public void setLobby_id(Long lobby_id) {
        this.lobby_id = lobby_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
