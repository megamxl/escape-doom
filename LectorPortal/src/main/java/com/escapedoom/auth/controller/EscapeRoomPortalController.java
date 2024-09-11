package com.escapedoom.auth.controller;

import com.escapedoom.auth.Service.EscaperoomService;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.EscapeRoomState;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.EscapeRoomDto;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/portal-escape-room")
@RequiredArgsConstructor
public class EscapeRoomPortalController {

    private final EscaperoomService escaperoomService;

    @CrossOrigin
    @GetMapping("/getAll")
    ResponseEntity<List<EscaperoomDTO>> getAllEscapeRoom() {
        return ResponseEntity.ok(escaperoomService.getAllRoomsByAnUser());
    }

    @PostMapping("/saveEscaperoom")
    EscapeRoomDto saveEscapeRoom() {
        return  escaperoomService.createADummyRoom();
    }

    @CrossOrigin
    @PostMapping(value = "openEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> openEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(escaperoomService.openEscapeRoom(lobbyId));
    }

    @CrossOrigin
    @PostMapping(value = "startEscapeRoom/{escapeRoomId}/{minutes}")
    public ResponseEntity<String> startEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId, @PathVariable("minutes") Long minutes) {
        return ResponseEntity.ok(escaperoomService.changeEscapeRoomState(lobbyId, EscapeRoomState.PLAYING ,minutes));
    }

    @CrossOrigin
    @PostMapping(value = "stopEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> stopEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(escaperoomService.changeEscapeRoomState(lobbyId, EscapeRoomState.STOPPED,null));
    }

}
