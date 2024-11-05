package com.escapedoom.gamesession.rest.data.response;

import com.escapedoom.gamesession.rest.data.EscapeRoomState;
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

    private ArrayList<Object> stage;

    private EscapeRoomState state;

    private Long roomID;

}
