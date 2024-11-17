package lector.portal.rest.util;


import lector.portal.shared.model.EscapeRoomState;
import lector.portal.dataaccess.LobbyRepository;
import lector.portal.dataaccess.entity.OpenLobbys;
import lector.portal.rest.util.RoomStopper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class RoomStopperTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
    private RoomStopper roomStopper;

    @Mock
    private Logger log;

    @BeforeEach
    public void setUp() {
        log = LoggerFactory.getLogger(RoomStopper.class);
    }

    @Test
    public void testHouseKeeper_NoLobbiesToStop() {
        when(lobbyRepository.findAllByStatePlaying()).thenReturn(Optional.of(Collections.emptyList()));

        roomStopper.HouseKeeper();

        verify(lobbyRepository, times(1)).findAllByStatePlaying();
        verifyNoMoreInteractions(lobbyRepository);
    }

    @Test
    public void testHouseKeeper_StopLobbies() {
        OpenLobbys lobby1 = new OpenLobbys();
        lobby1.setEndTime(LocalDateTime.now().minusMinutes(1));
        lobby1.setState(EscapeRoomState.PLAYING);
        lobby1.setLobby_Id(1L);

        OpenLobbys lobby2 = new OpenLobbys();
        lobby2.setEndTime(LocalDateTime.now().plusMinutes(1));
        lobby2.setState(EscapeRoomState.PLAYING);
        lobby2.setLobby_Id(2L);

        List<OpenLobbys> lobbies = Arrays.asList(lobby1, lobby2);

        when(lobbyRepository.findAllByStatePlaying()).thenReturn(Optional.of(lobbies));

        roomStopper.HouseKeeper();

        verify(lobbyRepository, times(1)).findAllByStatePlaying();
        verify(lobbyRepository, times(1)).save(lobby1);
        verifyNoMoreInteractions(lobbyRepository);
    }
}