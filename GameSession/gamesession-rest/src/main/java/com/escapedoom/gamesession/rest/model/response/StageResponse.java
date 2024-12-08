package com.escapedoom.gamesession.rest.model.response;

import com.escapedoom.gamesession.shared.EscapeRoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StageResponse {

    private EscapeRoomState state;

    private ArrayList<Object> stage;

    private Long roomID;

}
