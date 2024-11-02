import entity.Escaperoom;
import entity.OpenLobbys;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import types.EscapeRoomState;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        testUser.setUser_id(1L);
        testUser.setFirstname("Leon");
        testUser.setLastname("Doom");
        testUser.setEmail("leon@doom.at");
        return testUser;
    }

    private Escaperoom createTestEscaperoom() {
        Escaperoom escaperoom = new Escaperoom();
        escaperoom.setEscaperoom_id(1L);
        return escaperoom;
    }

    private OpenLobbys createTestLobby(Long lobbyId, EscapeRoomState state) {
        OpenLobbys lobby = new OpenLobbys();
        lobby.setLobby_Id(lobbyId);
        lobby.setEscaperoom(escaperoom);
        lobby.setUser(user);
        lobby.setState(state);
        return lobby;
    }

    @Test
    void findByEscaperoomAndUserAndStateStoppedNot_ReturnsLobbyWithExpectedState() {
        OpenLobbys openLobby = createTestLobby(1L, EscapeRoomState.JOINABLE);

        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.of(openLobby));

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isPresent();
        assertThat(result.get().getState()).isEqualTo(EscapeRoomState.JOINABLE);
    }

    @Test
    void findByEscaperoomAndUserAndStateStoppedNot_ReturnsLobbyWithExpectedId() {
        OpenLobbys openLobby = createTestLobby(2L, EscapeRoomState.JOINABLE);

        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.of(openLobby));

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isPresent();
        assertThat(result.get().getLobby_Id()).isEqualTo(2L);
    }

    @Test
    void findByEscaperoomAndUserAndStateStoppedNot_ReturnsEmptyWhenNoOpenLobby() {
        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any(User.class)))
                .thenReturn(Optional.empty());

        Optional<OpenLobbys> result = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(1L, user);

        assertThat(result).isNotPresent();
    }
}
