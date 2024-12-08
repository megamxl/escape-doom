package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.EscaperoomRepository;
import com.escapedoom.lector.portal.dataaccess.LobbyRepository;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
import com.escapedoom.lector.portal.dataaccess.entity.OpenLobbys;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import com.squareup.okhttp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EscapeRoomStateTest {
    @Mock
    private EscaperoomRepository escaperoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private CodeRiddleService codeRiddleService;

    @InjectMocks
    private EscaperoomService escaperoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeEscapeRoomStateWithoutPlayingState() {
        // Given
        Long escapeRoomId = 1L;
        EscapeRoomState newState = EscapeRoomState.STOPPED; // Not PLAYING

        Escaperoom mockEscapeRoom = new Escaperoom();
        mockEscapeRoom.setEscapeRoomId(escapeRoomId);

        User mockUser = new User();
        mockUser.setUserId(1L);

        OpenLobbys mockLobby = new OpenLobbys();
        mockLobby.setLobby_Id(100L);
        mockLobby.setEscaperoom(Escaperoom.builder().escapeRoomId(escapeRoomId).build());
        mockLobby.setState(EscapeRoomState.JOINABLE);

        when(escaperoomRepository.findById(escapeRoomId)).thenReturn(Optional.of(mockEscapeRoom));
        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(eq(escapeRoomId), any(User.class)))
                .thenReturn(Optional.of(mockLobby));
        when(escaperoomRepository.getReferenceById(escapeRoomId)).thenReturn(mockEscapeRoom);

        // Mock authenticated user
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // When
        String result = escaperoomService.changeEscapeRoomState(escapeRoomId, newState, null);

        // Then
        assertThat(result).isEqualTo("State of Escape Room with ID: " + escapeRoomId + " changed to " + newState);

        // Verify that the state was updated
        assertThat(mockLobby.getState()).isEqualTo(newState);

        // Ensure no time-related logic was executed
        verify(lobbyRepository).flush();
        verify(lobbyRepository).save(mockLobby);
    }
}
