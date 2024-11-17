package lector.portal.web.controller;

import lector.portal.rest.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lector.portal.shared.request.AuthenticationRequest;
import lector.portal.shared.request.RegisterRequest;
import lector.portal.shared.response.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    @CrossOrigin(origins = "http://localhost", allowCredentials = "true")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
