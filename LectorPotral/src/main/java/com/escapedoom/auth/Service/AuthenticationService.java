package com.escapedoom.auth.Service;


import com.escapedoom.auth.data.dataclasses.Requests.AuthenticationRequest;
import com.escapedoom.auth.data.dataclasses.Requests.RegisterRequest;
import com.escapedoom.auth.data.dataclasses.Respones.AuthenticationResponse;
import com.escapedoom.auth.data.dataclasses.models.user.Role;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.UserRepository;
import com.escapedoom.auth.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static io.jsonwebtoken.lang.Assert.notNull;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        notNull(registerRequest.getPassword());
        notNull(registerRequest.getEmail());

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
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        var user = repository.findByEmail(authRequest.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
