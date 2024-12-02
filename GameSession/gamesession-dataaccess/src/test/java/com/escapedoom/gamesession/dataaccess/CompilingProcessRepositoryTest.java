package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.config.TestApplication;
import com.escapedoom.gamesession.dataaccess.config.TestJpaConfig;
import com.escapedoom.gamesession.dataaccess.entity.ProcessingRequest;
import com.escapedoom.gamesession.shared.CompilingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@Transactional
public class CompilingProcessRepositoryTest {

    @Autowired
    private CompilingProcessRepository compilingProcessRepository;

    private ProcessingRequest processingRequest;

    @BeforeEach
    public void setUp() {
        processingRequest = ProcessingRequest.builder()
                .userID("user123")
                .compilingStatus(CompilingStatus.Submitted)
                .output("Compilation started")
                .build();
    }

    @Test
    public void testSaveProcessingRequest() {
        ProcessingRequest savedRequest = compilingProcessRepository.save(processingRequest);
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getUserID()).isEqualTo("user123");
        assertThat(savedRequest.getCompilingStatus()).isEqualTo(CompilingStatus.Submitted);
        assertThat(savedRequest.getOutput()).isEqualTo("Compilation started");
    }

    @Test
    public void testFindByUserID() {
        ProcessingRequest savedRequest = compilingProcessRepository.save(processingRequest);
        ProcessingRequest foundRequest = compilingProcessRepository.findById(savedRequest.getUserID()).orElseThrow();
        assertThat(foundRequest).isNotNull();
        assertThat(foundRequest.getUserID()).isEqualTo(savedRequest.getUserID());
        assertThat(foundRequest.getCompilingStatus()).isEqualTo(savedRequest.getCompilingStatus());
        assertThat(foundRequest.getOutput()).isEqualTo(savedRequest.getOutput());
    }

    @Test
    public void testCompilingStatusEnumMapping() {
        processingRequest.setCompilingStatus(CompilingStatus.Done);
        ProcessingRequest savedRequest = compilingProcessRepository.save(processingRequest);

        ProcessingRequest foundRequest = compilingProcessRepository.findById(savedRequest.getUserID()).orElseThrow();
        assertThat(foundRequest.getCompilingStatus()).isEqualTo(CompilingStatus.Done);
    }

    @Test
    public void testUpdateOutput() {
        ProcessingRequest savedRequest = compilingProcessRepository.save(processingRequest);
        savedRequest.setOutput("Compilation finished successfully");
        ProcessingRequest updatedRequest = compilingProcessRepository.save(savedRequest);

        assertThat(updatedRequest.getOutput()).isEqualTo("Compilation finished successfully");
    }

    @Test
    public void testDeleteByUserID() {
        ProcessingRequest savedRequest = compilingProcessRepository.save(processingRequest);
        String userID = savedRequest.getUserID();
        compilingProcessRepository.deleteById(userID);
        assertThat(compilingProcessRepository.findById(userID).isEmpty()).isTrue();
    }
}
