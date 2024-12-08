package com.escapedoom.lector.portal.rest.model;

import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EscaperoomResponse {

    private Long UserId;

    private Long escaproom_id;

    private String name;

    private String topic;

    private Integer time;

    private EscapeRoomState escapeRoomState;

    public EscaperoomResponse(Escaperoom escaperoom, EscapeRoomState escapeRoomState) {
        this.escaproom_id = escaperoom.getEscapeRoomId();
        this.UserId = escaperoom.getUser().getUserId();
        this.time = escaperoom.getTime();
        this.topic = escaperoom.getTopic();
        this.name = escaperoom.getName();
        this.escapeRoomState = escapeRoomState;
    }
}