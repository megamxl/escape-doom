package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.config.TestApplication;
import com.escapedoom.gamesession.dataaccess.config.TestJpaConfig;
import com.escapedoom.gamesession.dataaccess.entity.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@Transactional
public class SessionManagementRepositoryTest {

    @Autowired
    private SessionManagementRepository sessionManagementRepository;

    @Test
    public void testDeleteAllBySession() {
        sessionManagementRepository.deleteAllByEscaperoomSession(1L);
        List<Player> players = sessionManagementRepository.findAllByEscaperoomSession(1L).orElseThrow();
        assertThat(players).isEmpty();
    }
}
