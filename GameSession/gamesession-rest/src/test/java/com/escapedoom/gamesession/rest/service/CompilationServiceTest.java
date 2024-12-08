package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.*;
import com.escapedoom.gamesession.dataaccess.entity.*;
import com.escapedoom.gamesession.rest.config.redis.KafkaConfigProperties;
import com.escapedoom.gamesession.rest.model.code.CState;
import com.escapedoom.gamesession.rest.model.code.CodeCompilationRequest;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.shared.CompilingStatus;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CompilationServiceTest {

    private SessionManagementRepository sessionManagementRepository;
    private OpenLobbyRepository openLobbyRepository;
    private EscapeRoomRepository escapeRoomRepo;
    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaConfigProperties kafkaConfigProperties;
    private ObjectMapper myJsonSlave;
    private CompilationRepository compilationRepository;
    private CodeRiddleRepository codeRiddleRepository;
    private CompilationService compilationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        sessionManagementRepository = mock(SessionManagementRepository.class);
        openLobbyRepository = mock(OpenLobbyRepository.class);
        escapeRoomRepo = mock(EscapeRoomRepository.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaConfigProperties = mock(KafkaConfigProperties.class);
        myJsonSlave = mock(ObjectMapper.class);
        compilationRepository = mock(CompilationRepository.class);
        codeRiddleRepository = mock(CodeRiddleRepository.class);

        compilationService = new CompilationService(
                sessionManagementRepository,
                openLobbyRepository,
                escapeRoomRepo,
                kafkaTemplate,
                kafkaConfigProperties,
                myJsonSlave,
                compilationRepository,
                codeRiddleRepository
        );
    }

    @Test
    void getResult_noRequestFound_returnsBadRequest() {
        // Given
        String playerId = "player123";
        when(compilationRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        CodeStatus result = compilationService.getResult(playerId);

        // Then
        assertEquals(CState.BADREQUEST, result.getStatus());
        assertEquals("", result.getOutput());
        verify(compilationRepository, never()).delete(any());
    }

    @Test
    void getResult_requestNotDone_returnsWaiting() {
        // Given
        String playerId = "player123";
        ProcessingRequest request = new ProcessingRequest();
        request.setCompilingStatus(CompilingStatus.Submitted);
        when(compilationRepository.findById(playerId)).thenReturn(Optional.of(request));

        // When
        CodeStatus result = compilationService.getResult(playerId);

        // Then
        assertEquals(CState.WAITING, result.getStatus());
        assertEquals("", result.getOutput());
        verify(compilationRepository, never()).delete(any());
    }

    @Test
    void getResult_validRequestButNoPlayer_returnsBadRequest() {
        // Given
        String playerId = "player123";
        ProcessingRequest request = new ProcessingRequest();
        request.setCompilingStatus(CompilingStatus.Done);
        request.setOutput("some output");
        when(compilationRepository.findById(playerId)).thenReturn(Optional.of(request));
        when(sessionManagementRepository.findPlayerByHttpSessionID(playerId)).thenReturn(Optional.empty());

        // When
        CodeStatus result = compilationService.getResult(playerId);

        // Then
        assertEquals(CState.BADREQUEST, result.getStatus());
        assertEquals("", result.getOutput());
        verify(compilationRepository).delete(request);
    }

    @Test
    void getResult_playerSolvesStage_returnsSuccess() {
        // Given
        String playerId = "player123";
        ProcessingRequest request = new ProcessingRequest();
        request.setCompilingStatus(CompilingStatus.Done);
        request.setOutput("expected output");
        when(compilationRepository.findById(playerId)).thenReturn(Optional.of(request));

        Player player = new Player();
        player.setEscaperoomStageId(1L);
        player.setEscampeRoom_room_id(123L);
        player.setScore(0L);
        when(sessionManagementRepository.findPlayerByHttpSessionID(playerId)).thenReturn(Optional.of(player));

        EscapeRoomStage stage = new EscapeRoomStage();
        stage.setOutputID(123L);
        when(escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(1L, 123L)).thenReturn(Optional.of(stage));

        ConsoleNodeCode code = new ConsoleNodeCode();
        code.setExpectedOutput("expected output");
        when(codeRiddleRepository.findById(123L)).thenReturn(Optional.of(code));

        when(escapeRoomRepo.getMaxStageByRoomId(123L)).thenReturn(3L);

        // When
        CodeStatus result = compilationService.getResult(playerId);

        // Then
        assertEquals(CState.SUCCESS, result.getStatus());
        assertEquals("expected output", result.getOutput());
        assertEquals(20L, player.getScore());
        verify(sessionManagementRepository).save(player);
    }



    @Test
    void receivingKafkaMessages_withMatchingProcessingRequest_updatesAndSavesRequest() {
        // Given
        String playerSessionId = "player123";
        String kafkaMessageValue = "Compilation successful";
        ConsumerRecord<String, String> message = new ConsumerRecord<>("test-topic", 0, 0, playerSessionId, kafkaMessageValue);

        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setCompilingStatus(CompilingStatus.Submitted);
        when(compilationRepository.findById(playerSessionId)).thenReturn(Optional.of(processingRequest));

        // When
        compilationService.receivingKafkaMessages(message);

        // Then
        ArgumentCaptor<ProcessingRequest> captor = ArgumentCaptor.forClass(ProcessingRequest.class);
        verify(compilationRepository).save(captor.capture());
        ProcessingRequest savedRequest = captor.getValue();

        assertEquals(kafkaMessageValue, savedRequest.getOutput());
        assertEquals(CompilingStatus.Done, savedRequest.getCompilingStatus());
    }


    @Test
    void receivingKafkaMessages_withNoMatchingProcessingRequest_logsError() {
        // Given
        String playerSessionId = "invalid-player";
        String kafkaMessageValue = "Compilation failed";
        ConsumerRecord<String, String> message = new ConsumerRecord<>("test-topic", 0, 0, playerSessionId, kafkaMessageValue);

        when(compilationRepository.findById(playerSessionId)).thenReturn(Optional.empty());

        // When
        compilationService.receivingKafkaMessages(message);

        // Then
        verify(compilationRepository, never()).save(any());
        // Use a logging framework to assert logs if needed (e.g., LogCaptor or similar tools)
    }


    @Test
    void startCompiling_existingRequest_logsWarningAndExits() {
        // Given
        CodeCompilationRequest request = new CodeCompilationRequest();
        request.setPlayerSessionId("player123");

        when(compilationRepository.findById("player123")).thenReturn(Optional.of(new ProcessingRequest()));

        // When
        compilationService.startCompiling(request);

        // Then
        verify(compilationRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void startCompiling_invalidLobbyState_logsWarningAndExits() {
        // Given
        CodeCompilationRequest request = new CodeCompilationRequest();
        request.setPlayerSessionId("player123");

        Player player = new Player();
        player.setEscaperoomSession(123L);
        when(sessionManagementRepository.findPlayerByHttpSessionID("player123")).thenReturn(Optional.of(player));

        OpenLobbys lobby = new OpenLobbys();
        lobby.setState(EscapeRoomState.STOPPED);
        when(openLobbyRepository.findByLobbyId(123L)).thenReturn(Optional.of(lobby));

        // When
        compilationService.startCompiling(request);

        // Then
        verify(compilationRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }


}
