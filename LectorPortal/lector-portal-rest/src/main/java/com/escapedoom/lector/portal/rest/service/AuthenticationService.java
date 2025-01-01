package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.shared.model.Role;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.request.AuthenticationRequest;
import lombok.AllArgsConstructor;
import com.escapedoom.lector.portal.shared.request.RegisterRequest;
import com.escapedoom.lector.portal.shared.response.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        try {
            if (registerRequest.getPassword() == null || registerRequest.getEmail() == null) {
                throw new IllegalArgumentException("Email and password cannot be null");
            }

            var user = User.builder()
                    .firstname(registerRequest.getFirstname())
                    .lastname(registerRequest.getLastname())
                    .email(registerRequest.getEmail())
                    ._password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(Role.LECTOR)
                    .build();

            repository.save(user);

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred during registration.", ex);
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        var user = repository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + authRequest.getEmail()));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
