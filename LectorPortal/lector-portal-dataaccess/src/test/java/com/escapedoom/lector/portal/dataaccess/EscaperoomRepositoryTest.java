package com.escapedoom.lector.portal.dataaccess;

import com.escapedoom.lector.portal.dataaccess.config.TestApplication;
import com.escapedoom.lector.portal.dataaccess.config.TestJpaConfig;
import com.escapedoom.lector.portal.dataaccess.entity.Escaperoom;
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
public class EscaperoomRepositoryTest {

    @Autowired
    private EscaperoomRepository escaperoomRepository;

    @Test
    public void testFindAllEscapeRooms() {
        List<Escaperoom> escaperooms = escaperoomRepository.findAll();
        assertThat(escaperooms)
                .isNotNull()
                .hasSize(2);
    }

    @Test
    @Transactional
    public void testDeleteEscapeRoom() {
        Escaperoom newEscapeRoom = Escaperoom.builder()
                .name("Art Escape Room")
                .topic("Art")
                .time(30)
                .maxStage(1L)
                .build();

        Escaperoom savedEscapeRoom = escaperoomRepository.save(newEscapeRoom);

        escaperoomRepository.delete(savedEscapeRoom);

        assertThat(escaperoomRepository.findById(savedEscapeRoom.getEscapeRoomId())).isNotPresent();
    }
}
