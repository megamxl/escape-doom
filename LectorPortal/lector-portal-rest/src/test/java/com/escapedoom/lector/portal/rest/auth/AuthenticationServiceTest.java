package com.escapedoom.lector.portal.rest.auth;

import com.escapedoom.lector.portal.rest.service.AuthenticationService;
import com.escapedoom.lector.portal.rest.service.JwtService;
import com.escapedoom.lector.portal.shared.model.Role;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.request.AuthenticationRequest;
import com.escapedoom.lector.portal.shared.request.RegisterRequest;
import com.escapedoom.lector.portal.shared.response.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        try (final var mocks = MockitoAnnotations.openMocks(this)) {
            user = User.builder()
                    .userId(1L)
                    .firstname("John")
                    .lastname("Doe")
                    .email("john.doe@example.com")
                    ._password("hashed_password")
                    .role(Role.LECTOR)
                    .build();

            registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password");

            authenticationRequest = new AuthenticationRequest("john.doe@example.com", "password");
        }
    }

    @Test
    void testRegister() {
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("mock_jwt_token");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock_jwt_token");

        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void testAuthenticate() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("mock_jwt_token");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock_jwt_token");

        verify(userRepository, times(1)).findByEmail(any());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void testAuthenticateThrowsExceptionWhenUserNotFound() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            authenticationService.authenticate(authenticationRequest);
        });

        verify(userRepository, times(1)).findByEmail(any());
    }
}
