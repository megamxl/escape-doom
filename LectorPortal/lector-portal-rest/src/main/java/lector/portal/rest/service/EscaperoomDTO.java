package lector.portal.rest.service;

import lector.portal.shared.model.EscapeRoomState;
import lector.portal.dataaccess.entity.Escaperoom;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
        this.escaproom_id = escaperoom.getEscapeRoomId();
        this.UserId = escaperoom.getUser().getUserId();
        this.time = escaperoom.getTime();
        this.topic = escaperoom.getTopic();
        this.name = escaperoom.getName();
        this.escapeRoomState = escapeRoomState;
    }
}