package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.entity.EscapeRoomStage;
import com.escapedoom.gamesession.dataaccess.config.TestApplication;
import com.escapedoom.gamesession.dataaccess.config.TestJpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@Transactional
public class EscapeRoomStageRepositoryTest {

    @Autowired
    private EscapeRoomRepository escapeRoomRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** Das muss funktionieren */
    @Test
    public void verifyDatabaseContents() {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * FROM escaperoom");
        System.out.println("Database contents: " + results);
        assertThat(results).hasSize(2);
    }

    @Test
    @Transactional
    public void testSaveAndDeleteEscapeRoom() {
        EscapeRoomStage newEscapeRoomStage = EscapeRoomStage.builder()
                .roomId(1L)
                .stageId(1L)
                .stage("{ \"puzzle\": \"Find the key\" }")
                .build();

        EscapeRoomStage savedEscapeRoomStage = escapeRoomRepository.save(newEscapeRoomStage);

        assertThat(savedEscapeRoomStage)
                .isNotNull()
                .extracting(EscapeRoomStage::getRoomId)
                .isEqualTo(1L);

        escapeRoomRepository.delete(savedEscapeRoomStage);

        assertThat(escapeRoomRepository.findById(savedEscapeRoomStage.getRoomId())).isNotPresent();
    }

    @Test
    public void testGetMaxStage() {
        Long maxStage = escapeRoomRepository.getMaxStageByRoomId(1L);
        assertThat(maxStage).isNotNull().isEqualTo(3L);
    }

}
