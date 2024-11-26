package com.escapedoom.lector.portal.rest.controller;

import com.escapedoom.lector.portal.rest.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import com.escapedoom.lector.portal.shared.request.AuthenticationRequest;
import com.escapedoom.lector.portal.shared.request.RegisterRequest;
import com.escapedoom.lector.portal.shared.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginAndRegisterController {

    private final AuthenticationService service;

    @PostMapping("/register")
    
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
