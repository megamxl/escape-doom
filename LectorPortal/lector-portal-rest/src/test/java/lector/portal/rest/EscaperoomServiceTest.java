package lector.portal.rest;

import lector.portal.dataaccess.CodeRiddleRepository;
import lector.portal.dataaccess.EscaperoomRepository;
import lector.portal.dataaccess.LobbyRepository;
import lector.portal.dataaccess.UserRepository;
import lector.portal.dataaccess.entity.Escaperoom;
import lector.portal.dataaccess.entity.OpenLobbys;
import lector.portal.dataaccess.entity.User;
import lector.portal.rest.service.EscaperoomDTO;
import lector.portal.rest.service.EscaperoomService;
import lector.portal.shared.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EscaperoomServiceTest {

    @InjectMocks
    private EscaperoomService escaperoomService;

    @Mock
    private EscaperoomRepository escaperoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private CodeRiddleRepository codeRiddleRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a test User object with all required fields
        user = new User();
        user.setUserId(1L); // Assuming you have a user_id field
        user.setFirstname("Leon");
        user.setLastname("Doom");
        user.setEmail("leon@doom.at");
        user.setRole(Role.LECTOR); // Assuming a role field exists

        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        // Set up the security context
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllRoomsByAnUser_ReturnsRoomsSuccessfully() {
        Escaperoom escaperoom = new Escaperoom();
        escaperoom.setEscapeRoomId(1L);
        escaperoom.setName("Test Room");
        escaperoom.setUser(user);
        when(escaperoomRepository.findEscaperoomByUser(user)).thenReturn(Optional.of(List.of(escaperoom)));

        List<EscaperoomDTO> rooms = escaperoomService.getAllRoomsByAnUser();

        assertThat(rooms).isNotNull();
        assertThat(1).isEqualTo(rooms.size());
        assertThat("Test Room").isEqualTo(rooms.get(0).getName());
    }

    @Test
    void openEscapeRoom_RoomOpenedSuccessfully() {
        // Create a mock Escaperoom
        Escaperoom escaperoom = new Escaperoom();
        escaperoom.setEscapeRoomId(1L);

        // Mock repository behavior
        when(escaperoomRepository.getReferenceById(1L)).thenReturn(escaperoom);
        when(lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(anyLong(), any())).thenReturn(Optional.empty());

        // Mock OpenLobbys behavior
        OpenLobbys mockLobby = mock(OpenLobbys.class);
        when(mockLobby.getLobby_Id()).thenReturn(123L); // Set a fake ID
        when(lobbyRepository.save(any())).thenReturn(mockLobby); // Return the mock when saved

        String lobbyId = escaperoomService.openEscapeRoom(1L);

        assertThat(lobbyId).isNotNull();
        assertThat("123").isEqualTo(lobbyId);// Check that the returned ID is the one we set
        verify(lobbyRepository, times(1)).save(any(OpenLobbys.class));
    }
}
