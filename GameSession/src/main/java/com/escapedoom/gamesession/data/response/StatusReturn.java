package com.escapedoom.gamesession.data.response;

import com.escapedoom.gamesession.data.EscapeRoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusReturn {

    private EscapeRoomState state;

    private Long roomID;

}
