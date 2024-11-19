package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.rest.model.response.StatusReturn;
import com.escapedoom.gamesession.rest.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_JOIN_PATH)
@RequiredArgsConstructor
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping(value = Constants.GET_STAGE_URL)
    public StageResponse playerStage(@PathVariable String httpSession) {
        return playerService.getPlayerStage(httpSession);
    }

    @GetMapping(value = Constants.STATUS_URL)
    public StatusReturn playerStatus(@PathVariable String playerID) {
        return playerService.getPlayerStatus(playerID);
    }
}

