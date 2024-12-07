package com.escapedoom.gamesession.rest.model.response;

import com.escapedoom.gamesession.shared.EscapeRoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JoinResponse {

    private EscapeRoomState state;

    private String sessionId;

    private String name;

}
