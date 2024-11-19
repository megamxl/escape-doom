package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.services.LobbyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.API_JOIN_PATH)
@RequiredArgsConstructor
@CrossOrigin
public class LobbyController {

    private final LobbyService lobbyService;

    // method for joining / subscribing
    @CrossOrigin
    @GetMapping(value = Constants.ESCAPE_ROOM_URL, consumes = MediaType.ALL_VALUE)
    public JoinResponse joinLobby(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return lobbyService.manageLobbyJoin(httpSession.getSession().getId(), escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = Constants.DELETE_URL)
    public String clearLobby(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return lobbyService.deleteAllPlayersInLobby(escaperoom_id);
    }

    //TODO REMOVE IF THIS SERVICE KNOWS WHICH TO DELETE AND WHEN
    @GetMapping(value = Constants.GET_ALL_URL)
    public List<Player> lobbyPlayers(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        return lobbyService.getAllPlayersInLobby(escaperoom_id);
    }

}
