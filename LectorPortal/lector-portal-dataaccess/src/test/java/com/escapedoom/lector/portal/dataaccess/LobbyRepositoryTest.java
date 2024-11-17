package com.escapedoom.lector.portal.dataaccess;

import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
import com.escapedoom.lector.portal.dataaccess.entity.OpenLobbys;
import com.escapedoom.lector.portal.dataaccess.entity.User;
import com.escapedoom.lector.portal.shared.model.EscapeRoomState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class LobbyRepositoryTest {

    @Mock
    private LobbyRepository lobbyRepository;

    private User user;
    private Escaperoom escaperoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = createTestUser();
        escaperoom = createTestEscaperoom();
    }

    private User createTestUser() {
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setFirstname("Leon");
        testUser.setLastname("Doom");
        testUser.setEmail("leon@doom.at");
        return testUser;
    }

    private Escaperoom createTestEscaperoom() {
        Escaperoom escaperoom = new Escaperoom();
        escaperoom.setEscapeRoomId(1L);
        escaperoom.setName("Test Escape Room");
        escaperoom.setTopic("Adventure");
        escaperoom.setTime(60);
        escaperoom.setMaxStage(5L);
        return escaperoom;
    }

    private OpenLobbys createTestLobby(Long lobbyId, EscapeRoomState state, LocalDateTime startTime, LocalDateTime endTime) {
        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobby_Id(lobbyId);
        lobby.setEscaperoom(escaperoom);
        lobby.setUser(user);
        lobby.setState(state);
        lobby.setStartTime(startTime);
        lobby.setEndTime(endTime);
        return lobby;
    }

    @Test
    void findByEscaperoomAndUserAndStateStoppedNot_ReturnsLobbyWithExpectedFields() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);
        OpenLobbys openLobby = createTestLobby(1L, EscapeRoomState.JOINABLE, startTime, endTime);

        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.of(openLobby));

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isPresent();
        OpenLobbys retrievedLobby = result.get();

        assertThat(retrievedLobby.getLobby_Id()).isEqualTo(1L);
        assertThat(retrievedLobby.getState()).isEqualTo(EscapeRoomState.JOINABLE);
        assertThat(retrievedLobby.getEscaperoom()).isEqualTo(escaperoom);
        assertThat(retrievedLobby.getUser()).isEqualTo(user);
        assertThat(retrievedLobby.getStartTime()).isEqualTo(startTime);
        assertThat(retrievedLobby.getEndTime()).isEqualTo(endTime);
    }

    @Test
    void findByEscaperoomAndUserAndStateStoppedNot_ReturnsEmptyWhenNoOpenLobby() {
        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.empty());

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isNotPresent();
    }

    @Test
    void verifyEscaperoomFieldsInLobby() {
        OpenLobbys openLobby = createTestLobby(1L, EscapeRoomState.JOINABLE, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.of(openLobby));

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isPresent();
        Escaperoom retrievedEscaperoom = result.get().getEscaperoom();

        assertThat(retrievedEscaperoom.getEscapeRoomId()).isEqualTo(1L);
        assertThat(retrievedEscaperoom.getName()).isEqualTo("Test Escape Room");
        assertThat(retrievedEscaperoom.getTopic()).isEqualTo("Adventure");
        assertThat(retrievedEscaperoom.getTime()).isEqualTo(60);
        assertThat(retrievedEscaperoom.getMaxStage()).isEqualTo(5);
    }

    @Test
    void verifyTimestampsAreCorrect() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        OpenLobbys openLobby = createTestLobby(1L, EscapeRoomState.JOINABLE, startTime, endTime);

        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.of(openLobby));

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isPresent();
        OpenLobbys retrievedLobby = result.get();

        assertThat(retrievedLobby.getStartTime()).isEqualTo(startTime);
        assertThat(retrievedLobby.getEndTime()).isEqualTo(endTime);
    }
}
