package com.escapedoom.gamesession.rest.services;

import com.escapedoom.gamesession.dataaccess.SessionManagementRepository;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.utils.SseEmitterExtended;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private SessionManagementRepository sessionManagementRepository;

    @InjectMocks
    private NotificationService notificationService;

    private static final String HTTP_SESSION_ID = "testSessionId";
    private static final Long ESCAPE_ROOM_ID = 1L;

    @BeforeEach
    void setUp() {
        notificationService.getSseEmitters().clear();
    }

    @Test
    void testEstablishLobbyConnection_WhenPlayerExists() {
        Player player = new Player();
        player.setEscaperoomSession(ESCAPE_ROOM_ID);
        player.setName("TestPlayer");
        when(sessionManagementRepository.findPlayerByHttpSessionID(HTTP_SESSION_ID)).thenReturn(Optional.of(player));

        SseEmitterExtended sseEmitter = notificationService.establishLobbyConnection(HTTP_SESSION_ID);

        assertThat(sseEmitter).isNotNull();
        assertThat(sseEmitter.getName()).isEqualTo("TestPlayer");
        assertThat(sseEmitter.getLobby_id()).isEqualTo(ESCAPE_ROOM_ID);
        assertThat(sseEmitter.getHttpID()).isEqualTo(HTTP_SESSION_ID);
        assertThat(notificationService.getSseEmitters()).contains(sseEmitter);
    }
}
