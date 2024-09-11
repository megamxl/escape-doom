package com.escapedoom.gamesession.controller;

import com.escapedoom.gamesession.data.Player;
import com.escapedoom.gamesession.services.PlayerStateManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/info")
public class IPCController {

    private final PlayerStateManagementService playerStateManagementService;

    @GetMapping("started/{escaperoom_id}")
    public void informAboutStartedGame(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        playerStateManagementService.informAboutStart(escaperoom_id);
    }


}
