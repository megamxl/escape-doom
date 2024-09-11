package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.codeCompiling.CodeStatus;
import com.escapedoom.gamesession.data.response.StageResponse;
import com.escapedoom.gamesession.data.response.StatusReturn;
import com.escapedoom.gamesession.utils.SseEmitterExtended;
import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.data.codeCompiling.CodeCompilingRequestEvent;
import com.escapedoom.gamesession.data.response.JoinResponse;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/join")
public class JoinController {

    private final PlayerStateManagementService playerStateManagementService;


    @CrossOrigin
    // method for joining / subscribing
    @GetMapping(value = "/{escaperoom_id}", consumes = MediaType.ALL_VALUE)
    public JoinResponse sessionId(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
         return playerStateManagementService.mangeStateBySessionID(httpSession.getSession().getId(), escaperoom_id);
    }

    @GetMapping(value = "lobby/{id}")
    public SseEmitterExtended lobby(@PathVariable("id")String session) {
        return playerStateManagementService.lobbyConnection(session);
    }

    @GetMapping(value = "getStage/{httpSession}")
    public StageResponse currentStage(@PathVariable String httpSession) {
        return playerStateManagementService.returnStageToPlayer(httpSession);
    }

    // method to dispatch data to the rigth lobbys

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping("delete/{escaperoom_id}")
    public String  deletAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.deleteAllPlayersByEscaperoomID(escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping("getAll/{escaperoom_id}")
    public List<Player> getAll(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return playerStateManagementService.getAllPlayersByEscapeRoomID(escaperoom_id);
    }

    @PostMapping(value = "submitCode")
    public void submitCode(@RequestBody CodeCompilingRequestEvent codeCompilingRequestEvent) {
         playerStateManagementService.startCompiling(codeCompilingRequestEvent);
    }

    @GetMapping(value = "getCode/{playerID}")
    public CodeStatus submitCode(@PathVariable String playerID) {
        return playerStateManagementService.getResult(playerID);
    }

    @GetMapping(value = "status/{playerID}")
    public StatusReturn getCurrentStatusByPlayerID(@PathVariable String playerID) {
        return playerStateManagementService.getCurrentStatus(playerID);
    }
}
