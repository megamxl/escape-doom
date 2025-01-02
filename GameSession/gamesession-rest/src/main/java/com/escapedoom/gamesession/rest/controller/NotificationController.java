package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.service.NotificationService;
import com.escapedoom.gamesession.rest.service.NotificationWsService;
import com.escapedoom.gamesession.rest.util.SseEmitterExtended;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_JOIN_PATH)
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    //private final NotificationService notificationService;
    private final NotificationWsService notificationWsService;

    @GetMapping(value = Constants.LOBBY_URL)
    public void connectToLobby(@PathVariable("id") String session) {
        notificationWsService.establishLobbyEmitters(session);
    }

}
