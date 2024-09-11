package com.escapedoom.gamesession.data.response;

import com.escapedoom.gamesession.data.EscapeRoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JoinResponse {

    private String name;

    private String sessionId;

    private EscapeRoomState state;

}
