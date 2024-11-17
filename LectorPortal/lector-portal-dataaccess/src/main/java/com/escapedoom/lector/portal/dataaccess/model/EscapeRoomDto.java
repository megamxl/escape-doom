package com.escapedoom.lector.portal.dataaccess.model;

import com.escapedoom.lector.portal.dataaccess.entity.EscapeRoomStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class EscapeRoomDto {

    private Long escaperoom_id;

    private String name;

    private String topic;

    private Integer time;

    private List<EscapeRoomStage> escapeRoomStages;

}
