package com.escapedoom.auth.controller;

import com.escapedoom.auth.Service.AuthenticationService;
import com.escapedoom.auth.data.dataclasses.Requests.AuthenticationRequest;
import com.escapedoom.auth.data.dataclasses.Requests.RegisterRequest;
import com.escapedoom.auth.data.dataclasses.Respones.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
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

    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
