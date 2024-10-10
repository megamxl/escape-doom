package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.escapedoom.gamesession.controller.UrlConstants.IPC_INFO_URL;
import static com.escapedoom.gamesession.controller.UrlConstants.START_GAME_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(IPC_INFO_URL)
public class IPCController {

    private final PlayerStateManagementService playerStateManagementService;

    @GetMapping(START_GAME_URL)
    public void informAboutStartedGame(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        playerStateManagementService.informAboutStart(escaperoom_id);
    }
}
