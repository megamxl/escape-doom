package lector.portal.rest.controller;


import lector.portal.rest.service.EscaperoomDTO;
import lector.portal.rest.service.EscaperoomService;
import lector.portal.dataaccess.model.EscapeRoomDto;
import lector.portal.shared.model.EscapeRoomState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/portal-escape-room")
@RequiredArgsConstructor
public class EscapeRoomPortalController {

    private final EscaperoomService service;

    @CrossOrigin
    @GetMapping("/getAll")
    ResponseEntity<List<EscaperoomDTO>> getAllEscapeRoom() {
        return ResponseEntity.ok(service.getAllRoomsByAnUser());
    }

    @PostMapping("/saveEscaperoom")
    EscapeRoomDto saveEscapeRoom() {
        return  service.createADummyRoom();
    }

    @CrossOrigin
    @PostMapping(value = "openEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> openEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(service.openEscapeRoom(lobbyId));
    }

    @CrossOrigin
    @PostMapping(value = "startEscapeRoom/{escapeRoomId}/{minutes}")
    public ResponseEntity<String> startEscapeRoom(
            @PathVariable("escapeRoomId") Long lobbyId,
            @PathVariable("minutes") Long minutes
    ) {
        return ResponseEntity.ok(service.changeEscapeRoomState(lobbyId, EscapeRoomState.PLAYING ,minutes));
    }

    @CrossOrigin
    @PostMapping(value = "stopEscapeRoom/{escapeRoomId}")
    public ResponseEntity<String> stopEscapeRoom(@PathVariable("escapeRoomId") Long lobbyId) {
        return ResponseEntity.ok(service.changeEscapeRoomState(lobbyId, EscapeRoomState.STOPPED,null));
    }

}
