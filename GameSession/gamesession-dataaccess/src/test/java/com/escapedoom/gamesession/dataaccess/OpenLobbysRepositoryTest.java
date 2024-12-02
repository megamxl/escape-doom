package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.config.TestApplication;
import com.escapedoom.gamesession.dataaccess.config.TestJpaConfig;
import com.escapedoom.gamesession.dataaccess.entity.OpenLobbys;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@Transactional
public class OpenLobbysRepositoryTest {

    @Autowired
    private OpenLobbyRepository openLobbysRepository;

    private OpenLobbys openLobbys;

    @BeforeEach
    public void setUp() {
        openLobbys = OpenLobbys.builder()
                .escaperoom_escaperoom_id(1L)
                .user_user_id(1L)
                .state(EscapeRoomState.JOINABLE)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .build();
    }

    @Test
    public void testSaveOpenLobbys() {
        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);
        assertThat(savedLobby).isNotNull();
        assertThat(savedLobby.getLobbyId()).isNotNull();
        assertThat(savedLobby.getEscaperoom_escaperoom_id()).isEqualTo(1L);
        assertThat(savedLobby.getState()).isEqualTo(EscapeRoomState.JOINABLE);
    }

    @Test
    public void testFindById() {
        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);
        OpenLobbys foundLobby = openLobbysRepository.findById(savedLobby.getLobbyId()).orElseThrow();
        assertThat(foundLobby).isNotNull();
        assertThat(foundLobby.getLobbyId()).isEqualTo(savedLobby.getLobbyId());
        assertThat(foundLobby.getState()).isEqualTo(savedLobby.getState());
    }

    @Test
    public void testStateEnumMapping() {
        openLobbys.setState(EscapeRoomState.STOPPED);
        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);

        OpenLobbys foundLobby = openLobbysRepository.findById(savedLobby.getLobbyId()).orElseThrow();
        assertThat(foundLobby.getState()).isEqualTo(EscapeRoomState.STOPPED);
    }

    @Test
    public void testTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        openLobbys.setStartTime(now);
        openLobbys.setEndTime(now.plusHours(2));

        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);
        OpenLobbys foundLobby = openLobbysRepository.findById(savedLobby.getLobbyId()).orElseThrow();

        assertThat(foundLobby.getStartTime()).isEqualTo(now);
        assertThat(foundLobby.getEndTime()).isEqualTo(now.plusHours(2));
    }

    @Test
    public void testUpdateState() {
        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);
        savedLobby.setState(EscapeRoomState.PLAYING);
        OpenLobbys updatedLobby = openLobbysRepository.save(savedLobby);

        assertThat(updatedLobby.getState()).isEqualTo(EscapeRoomState.PLAYING);
    }

    @Test
    public void testDeleteById() {
        OpenLobbys savedLobby = openLobbysRepository.save(openLobbys);
        Long lobbyId = savedLobby.getLobbyId();
        openLobbysRepository.deleteById(lobbyId);
        assertThat(openLobbysRepository.findById(lobbyId).isEmpty()).isTrue();
    }
}
