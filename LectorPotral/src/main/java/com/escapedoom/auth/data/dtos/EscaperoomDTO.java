package com.escapedoom.auth.data.dtos;

import com.escapedoom.auth.Service.EscaperoomService;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EscaperoomDTO {

    private Long UserId;

    private Long escaproom_id;

    private String name;

    private String topic;

    private Integer time;

    private EscapeRoomState escapeRoomState;

    public EscaperoomDTO(Escaperoom escaperoom, EscapeRoomState escapeRoomState) {
        this.escaproom_id = escaperoom.getEscaperoom_id();
        this.UserId = escaperoom.getUser().getUser_id();
        this.time = escaperoom.getTime();
        this.topic = escaperoom.getTopic();
        this.name = escaperoom.getName();
        this.escapeRoomState = escapeRoomState;
    }
}
