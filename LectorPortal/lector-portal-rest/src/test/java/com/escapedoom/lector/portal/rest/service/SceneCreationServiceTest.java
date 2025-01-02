package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.EscaperoomRepository;
import com.escapedoom.lector.portal.dataaccess.LobbyRepository;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.entity.ConsoleNodeCode;
import com.escapedoom.lector.portal.dataaccess.entity.EscapeRoomStage;
import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.dataaccess.model.EscapeRoomDto;
import com.escapedoom.lector.portal.shared.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SceneCreationServiceTest {
    @InjectMocks
    private EscaperoomService escaperoomService;

    @Mock
    private EscaperoomRepository escaperoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private CodeRiddleService codeRiddleService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createADummyRoom_ShouldCreateRoomSuccessfully() {
        // Arrange
        User mockUser = User.builder()
                .userId(1L)
                .email("leon@doom.at")
                .role(Role.LECTOR)
                .build();

        ConsoleNodeCode mockRiddle1 = ConsoleNodeCode.builder().id(1L).build();
        ConsoleNodeCode mockRiddle2 = ConsoleNodeCode.builder().id(2L).build();

        when(userRepository.findByEmail("leon@doom.at")).thenReturn(Optional.of(mockUser));
        when(codeRiddleService.createCodeRiddle(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockRiddle1)
                .thenReturn(mockRiddle2);

        // Capture the saved escape room
        Escaperoom capturedRoom = new Escaperoom();
        when(escaperoomRepository.save(any(Escaperoom.class)))
                .thenAnswer(invocation -> {
                    Escaperoom room = invocation.getArgument(0);
                    room.setEscapeRoomId(100L); // Simulate generated ID
                    return room;
                });

        // Act
        EscapeRoomDto result = escaperoomService.createADummyRoom();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEscaperoom_id()).isEqualTo(100L);
        assertThat(result.getName()).isEqualTo("Catch me");
        assertThat(result.getTopic()).isEqualTo("Yee");
        assertThat(result.getTime()).isEqualTo(90);
        assertThat(result.getEscapeRoomStages()).hasSize(2);

        EscapeRoomStage stage1 = result.getEscapeRoomStages().get(0);
        assertThat(stage1.getStageId()).isEqualTo(1L);
        assertThat(stage1.getOutputID()).isEqualTo(mockRiddle1.getId());
        assertThat(stage1.getStage()).isNotEmpty();

        EscapeRoomStage stage2 = result.getEscapeRoomStages().get(1);
        assertThat(stage2.getStageId()).isEqualTo(2L);
        assertThat(stage2.getOutputID()).isEqualTo(mockRiddle2.getId());
        assertThat(stage2.getStage()).isNotEmpty();

        // Verify interactions
        verify(userRepository).findByEmail("leon@doom.at");
        verify(codeRiddleService, times(2)).createCodeRiddle(anyString(), anyString(), anyString(), anyString());
        verify(escaperoomRepository).save(any(Escaperoom.class));
    }

}
