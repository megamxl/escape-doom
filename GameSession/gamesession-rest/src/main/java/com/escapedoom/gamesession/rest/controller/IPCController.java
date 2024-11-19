package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.escapedoom.gamesession.rest.Constants.IPC_INFO_URL;
import static com.escapedoom.gamesession.rest.Constants.START_GAME_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(IPC_INFO_URL)
public class IPCController {

    private final NotificationService notificationService;

    @GetMapping(START_GAME_URL)
    public void startEscapeRoom(@PathVariable Long escaperoom_id, HttpServletRequest httpSession){
        notificationService.notifyEscapeRoomStart(escaperoom_id);
    }
}
