package com.escapedoom.gamesession.rest.service;

import com.escapedoom.gamesession.dataaccess.*;
import com.escapedoom.gamesession.dataaccess.entity.*;
import com.escapedoom.gamesession.rest.config.redis.KafkaConfigProperties;
import com.escapedoom.gamesession.rest.model.code.CState;
import com.escapedoom.gamesession.rest.model.code.CodeCompilationRequest;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.rest.util.CodeSniptes;
import com.escapedoom.gamesession.shared.CompilingStatus;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    private final SessionManagementRepository sessionManagementRepository;

    private final OpenLobbyRepository openLobbyRepository;

    private final EscapeRoomRepository escapeRoomRepo;

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final KafkaConfigProperties kafkaConfigProperties;

    private final ObjectMapper myJsonSlave;

    private final CompilationRepository compilationRepository;

    private final CodeRiddleRepository codeRiddleRepository;

    public void startCompiling(CodeCompilationRequest codeCompilationRequest) {
        log.debug("Compilation request session ID: {}", codeCompilationRequest.getPlayerSessionId());

        try {
            if (compilationRepository.findById(codeCompilationRequest.getPlayerSessionId()).isPresent()) {
                log.warn("Compilation request already exists for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId());
                return;
            }

            var curr = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilationRequest.getPlayerSessionId());
            if (curr.isPresent()) {
                Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
                if (lobbyId.isPresent() && lobbyId.get().getState() != EscapeRoomState.PLAYING) {
                    log.warn("Lobby state is not in PLAYING, for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId());
                    return;
                }
            }

            Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilationRequest.getPlayerSessionId());
            if (playerByHttpSessionID.isPresent()) {
                Optional<EscapeRoomStage> escapeRoomStage = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(
                        playerByHttpSessionID.get().getEscaperoomStageId(),
                        playerByHttpSessionID.get().getEscampeRoom_room_id()
                );
                if (escapeRoomStage.isPresent()) {
                    Optional<ConsoleNodeCode> consoleNodeCode = codeRiddleRepository.findById(escapeRoomStage.get().getOutputID());
                    if (consoleNodeCode.isPresent()) {
                        log.debug("Generating code snippet for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId());
                        codeCompilationRequest.setDateTime(LocalDateTime.now());
                        codeCompilationRequest.setCode(CodeSniptes.javaClassGenerator(
                                consoleNodeCode.get().getInput(),
                                consoleNodeCode.get().getVariableName(),
                                codeCompilationRequest.getCode())
                        );
                    }
                }
            }

            if (codeCompilationRequest.getDateTime() == null) {
                log.warn("DateTime is null for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId());
                return;
            }

            String requestAsJsonString = myJsonSlave.writeValueAsString(codeCompilationRequest);
            kafkaTemplate.send(kafkaConfigProperties.getCodeCompilerTopic(), requestAsJsonString);

            compilationRepository.save(ProcessingRequest.builder()
                    .userID(codeCompilationRequest.getPlayerSessionId())
                    .compilingStatus(CompilingStatus.Submitted)
                    .build());
            log.info("Compilation request submitted successfully for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId());
        } catch (JsonProcessingException e) {
            log.error("Error serializing CodeCompilationRequest to JSON. PlayerSessionId: {}, Error: {}",
                    codeCompilationRequest.getPlayerSessionId(), e.getMessage(), e);
            throw new RuntimeException("Failed to serialize CodeCompilationRequest to JSON.", e);
        } catch (Exception ex) {
            log.error("Unexpected error during compilation request for PlayerSessionId: {}", codeCompilationRequest.getPlayerSessionId(), ex);
            throw ex;
        }
    }

    @KafkaListener(topics = "computedCode")
    @Transactional
    public void receivingKafkaMessages(final ConsumerRecord<String, String> message) {
        log.info("Received Kafka message for PlayerSessionId: {}", message.key());

        var process = compilationRepository.findById(message.key());
        if (process.isPresent()) {
            ProcessingRequest processingRequest = process.get();
            processingRequest.setOutput(message.value());
            processingRequest.setCompilingStatus(CompilingStatus.Done);
            compilationRepository.save(processingRequest);
        } else {
            log.error("Should have not received This message {}" , message);
        }
    }

    public CodeStatus getResult(String playerID) {
        // Check if there is a dataset in the database for that name
        log.debug("Fetching compilation result for Player ID: {}", playerID);

        try {
            Optional<ProcessingRequest> processingRequestOpt = compilationRepository.findById(playerID);
            if (processingRequestOpt.isEmpty()) {
                log.warn("No compilation request found for Player ID: {}", playerID);
                return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
            }

            ProcessingRequest processingRequest = processingRequestOpt.get();
            if (processingRequest.getCompilingStatus() != CompilingStatus.Done) {
                log.warn("Compilation is still in progress for Player ID: {}", playerID);
                return CodeStatus.builder().status(CState.WAITING).output("").build();
            }

            compilationRepository.delete(processingRequest);
            log.info("Deleted completed processing request for Player ID: {}", playerID);

            Optional<Player> playerOpt = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
            if (playerOpt.isEmpty()) {
                log.debug("No player found for Player ID: {}", playerID);
                return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
            }

            Player player = playerOpt.get();
            Optional<EscapeRoomStage> escapeRoomStageOpt = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(
                    player.getEscaperoomStageId(), player.getEscampeRoom_room_id()
            );
            if (escapeRoomStageOpt.isEmpty()) {
                log.debug("No escape room stage found for Player ID: {}", playerID);
                return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
            }

            EscapeRoomStage escapeRoomStage = escapeRoomStageOpt.get();
            Optional<ConsoleNodeCode> consoleNodeCodeOpt = codeRiddleRepository.findById(escapeRoomStage.getOutputID());
            if (consoleNodeCodeOpt.isEmpty()) {
                log.debug("No console node code found for Player ID: {}", playerID);
                return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
            }

            ConsoleNodeCode consoleNodeCode = consoleNodeCodeOpt.get();
            if (!processingRequest.getOutput().replace("\n", "").equals(consoleNodeCode.getExpectedOutput())) {
                log.debug("Compilation result mismatch for Player ID: {}", playerID);
                return CodeStatus.builder()
                        .status(processingRequest.getOutput().equals("COMPILE ERROR") ? CState.ERROR : CState.COMPILED)
                        .output(processingRequest.getOutput())
                        .build();
            }

            // Player has solved the stage
            Long maxStage = escapeRoomRepo.getMaxStageByRoomId(player.getEscampeRoom_room_id());
            if (player.getEscaperoomStageId() + 1 < maxStage) {
                player.setEscaperoomStageId(player.getEscaperoomStageId() + 1);
                player.setScore(player.getScore() + 10L * player.getEscaperoomStageId());

                Optional<OpenLobbys> byLobbyId = openLobbyRepository.findByLobbyId(player.getEscaperoomSession());
                byLobbyId.ifPresent(openLobbys -> player.setLastStageSolved(openLobbys.getStartTime().until(LocalDateTime.now(), ChronoUnit.SECONDS)));

                sessionManagementRepository.save(player);
                log.debug("Player {} progressed to the next stage.", playerID);
                return CodeStatus.builder().status(CState.SUCCESS).output(processingRequest.getOutput()).build();
            }

            // Player has won the room
            player.setScore(player.getScore() + 10L * player.getEscaperoomStageId());
            sessionManagementRepository.save(player);
            return CodeStatus.builder().status(CState.WON).output(player.getEscaperoomSession().toString()).build();

        } catch (Exception ex) {
            log.error("Unexpected error while fetching result for Player ID: {}", playerID, ex);
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }
    }

}
