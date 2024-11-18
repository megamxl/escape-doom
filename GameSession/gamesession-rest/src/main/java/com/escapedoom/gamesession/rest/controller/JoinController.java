package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.rest.model.response.StatusReturn;
import com.escapedoom.gamesession.rest.utils.SseEmitterExtended;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.model.code.CodeCompilingRequestEvent;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping(Constants.API_JOIN_PATH)
public class JoinController {

    private final PlayerStateManagementService playerStateManagementService;

    // method for joining / subscribing
    @CrossOrigin
    @GetMapping(value = Constants.ESCAPE_ROOM_URL, consumes = MediaType.ALL_VALUE)
    public JoinResponse sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
    }

    @GetMapping(value = Constants.LOBBY_URL)
    public SseEmitterExtended lobby(@PathVariable("id") String session) {
        return playerStateManagementService.lobbyConnection(session);
    }

    @GetMapping(value = Constants.GET_STAGE_URL)
    public StageResponse currentStage(@PathVariable String httpSession) {
        return playerStateManagementService.returnStageToPlayer(httpSession);
    }

    // method to dispatch data to the rigth lobbys

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = Constants.DELETE_URL)
    public String deletAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.deleteAllPlayersByEscaperoomID(escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = Constants.GET_ALL_URL)
    public List<Player> getAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.getAllPlayersByEscapeRoomID(escaperoom_id);
    }

    @PostMapping(value = Constants.SUBMIT_CODE_URL)
    public void submitCode(@RequestBody CodeCompilingRequestEvent codeCompilingRequestEvent) {
        playerStateManagementService.startCompiling(codeCompilingRequestEvent);
    }

    @GetMapping(value = Constants.GET_CODE_URL)
    public CodeStatus submitCode(@PathVariable String playerID) {
        return playerStateManagementService.getResult(playerID);
    }

    @GetMapping(value = Constants.STATUS_URL)
    public StatusReturn getCurrentStatusByPlayerID(@PathVariable String playerID) {
        return playerStateManagementService.getCurrentStatus(playerID);
    }
}
