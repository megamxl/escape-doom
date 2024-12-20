package com.escapedoom.lector.portal.rest.controller;


import com.escapedoom.lector.portal.rest.model.EscaperoomResponse;
import com.escapedoom.lector.portal.rest.service.EscaperoomService;
import com.escapedoom.lector.portal.dataaccess.model.EscapeRoomDto;
import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/v1/portal-escape-room")
@RequiredArgsConstructor
public class EscapeRoomPortalController {

    private final EscaperoomService service;

    
    @GetMapping("/getAll")
    
    ResponseEntity<List<EscaperoomResponse>> getAllEscapeRoom() {
        return ResponseEntity.ok(service.getAllRoomsByAnUser());
    }

    @PostMapping("/saveEscaperoom")
    
    EscapeRoomDto saveEscapeRoom() {
        return  service.createADummyRoom();
    }

    
    @PostMapping(value = "openEscapeRoom/{escapeRoomId}")
    
    public ResponseEntity<String> openEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(service.openEscapeRoom(lobbyId));
    }

    
    @PostMapping(value = "startEscapeRoom/{escapeRoomId}/{minutes}")
    
    public ResponseEntity<String> startEscapeRoom(
            @PathVariable("escapeRoomId") Long lobbyId,
            @PathVariable("minutes") Long minutes
    ) {
        return ResponseEntity.ok(service.changeEscapeRoomState(lobbyId, EscapeRoomState.PLAYING ,minutes));
    }

    
    @PostMapping(value = "stopEscapeRoom/{escapeRoomId}")
    
    public ResponseEntity<String> stopEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(service.changeEscapeRoomState(lobbyId, EscapeRoomState.STOPPED,null));
    }

}
