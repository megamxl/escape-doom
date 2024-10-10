package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.codeCompiling.CodeStatus;
import com.escapedoom.gamesession.data.response.StageResponse;
import com.escapedoom.gamesession.data.response.StatusReturn;
import com.escapedoom.gamesession.utils.SseEmitterExtended;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.data.codeCompiling.CodeCompilingRequestEvent;
import com.escapedoom.gamesession.data.response.JoinResponse;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping(UrlConstants.API_JOIN_PATH)
public class JoinController {

    private final PlayerStateManagementService playerStateManagementService;

    // method for joining / subscribing
    @CrossOrigin
    @GetMapping(value = UrlConstants.ESCAPE_ROOM_URL, consumes = MediaType.ALL_VALUE)
    public JoinResponse sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
    }

    @GetMapping(value = UrlConstants.LOBBY_URL)
    public SseEmitterExtended lobby(@PathVariable("id") String session) {
        return playerStateManagementService.lobbyConnection(session);
    }

    @GetMapping(value = UrlConstants.GET_STAGE_URL)
    public StageResponse currentStage(@PathVariable String httpSession) {
        return playerStateManagementService.returnStageToPlayer(httpSession);
    }

    // method to dispatch data to the rigth lobbys

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = UrlConstants.DELETE_URL)
    public String deletAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.deleteAllPlayersByEscaperoomID(escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = UrlConstants.GET_ALL_URL)
    public List<Player> getAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.getAllPlayersByEscapeRoomID(escaperoom_id);
    }

    @PostMapping(value = UrlConstants.SUBMIT_CODE_URL)
    public void submitCode(@RequestBody CodeCompilingRequestEvent codeCompilingRequestEvent) {
        playerStateManagementService.startCompiling(codeCompilingRequestEvent);
    }

    @GetMapping(value = UrlConstants.GET_CODE_URL)
    public CodeStatus submitCode(@PathVariable String playerID) {
        return playerStateManagementService.getResult(playerID);
    }

    @GetMapping(value = UrlConstants.STATUS_URL)
    public StatusReturn getCurrentStatusByPlayerID(@PathVariable String playerID) {
        return playerStateManagementService.getCurrentStatus(playerID);
    }
}
