package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.EscaperoomRepository;
import com.escapedoom.lector.portal.dataaccess.LobbyRepository;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
import com.escapedoom.lector.portal.dataaccess.entity.OpenLobbys;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setUp() {
        // Mock SecurityContextHolder
        securityContextHolderMock = mockStatic(SecurityContextHolder.class);

        // Mock SecurityContext
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        User mockUser = User.builder()
                .userId(1L)
                .email("test@example.com")
                .build();

        // Configure SecurityContextHolder to return mocked SecurityContext
        when(SecurityContextHolder.getContext()).thenReturn(mockSecurityContext);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

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
