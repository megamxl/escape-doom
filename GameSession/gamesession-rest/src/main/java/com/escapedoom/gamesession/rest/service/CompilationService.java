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
        if (compilationRepository.findById(codeCompilationRequest.getPlayerSessionId()).isPresent()) {
            return;
        }
        var curr = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilationRequest.getPlayerSessionId());
        if (curr.isPresent()) {
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                if (lobbyId.get().getState() != EscapeRoomState.PLAYING) {
                    return;
                }
            }
        }

        String requestAsJsoString = null;

        //TODO LIMIT TEST
        //TODO RETURN ERRRORS TAHAT FRONTEND UNDERSTANDS
        Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilationRequest.getPlayerSessionId());
        if (playerByHttpSessionID.isPresent()) {
            Optional<EscapeRoomStage> escapeRoomDaoByStageIdAndRoomId = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(playerByHttpSessionID.get().getEscaperoomStageId(), playerByHttpSessionID.get().getEscampeRoom_room_id());
            if (escapeRoomDaoByStageIdAndRoomId.isPresent()) {
                Optional<ConsoleNodeCode> byId = codeRiddleRepository.findById(escapeRoomDaoByStageIdAndRoomId.get().getOutputID());
                if (byId.isPresent()) {
                    codeCompilationRequest.setDateTime(LocalDateTime.now());
                    codeCompilationRequest.setCode(CodeSniptes.javaClassGenerator(byId.get().getInput(), byId.get().getVariableName() , codeCompilationRequest.getCode()));
                }
            }
        }
        if (codeCompilationRequest.getDateTime() == null) {
            return;
        }

        try {
            requestAsJsoString = myJsonSlave.writeValueAsString(codeCompilationRequest);
        } catch (JsonProcessingException e) {
            return;
        }
        kafkaTemplate.send(kafkaConfigProperties.getCodeCompilerTopic(), requestAsJsoString);

        compilationRepository.save(ProcessingRequest.builder()
                .userID(codeCompilationRequest.getPlayerSessionId())
                .compilingStatus(CompilingStatus.Submitted)
                .build());
    }

    @KafkaListener(topics = "computedCode")
    @Transactional
    public void receivingKafkaMessages(final ConsumerRecord<String, String> message) {
        System.out.print(message.key());
        var process = compilationRepository.findById(message.key());
        if (process.isPresent()) {
            ProcessingRequest processingRequest = process.get();
            processingRequest.setOutput(message.value());
            processingRequest.setCompilingStatus(CompilingStatus.Done);
            compilationRepository.save(processingRequest);
        } else {
            log.atError().log("Should have not received This message {}" , message);
        }
    }

    public CodeStatus getResult(String playerID) {
        // Check if there is a dataset in the database for that name
        Optional<ProcessingRequest> compilationRepositoryById = compilationRepository.findById(playerID);
        if (!compilationRepositoryById.isPresent()) {
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }

        ProcessingRequest processingRequest = compilationRepositoryById.get();
        if (processingRequest.getCompilingStatus() != CompilingStatus.Done) {
            return CodeStatus.builder().status(CState.WAITING).output("").build();
        }

        compilationRepository.delete(processingRequest);

        Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
        if (!playerByHttpSessionID.isPresent()) {
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }

        Player player = playerByHttpSessionID.get();
        Optional<EscapeRoomStage> escapeRoomDaoByStageIdAndRoomId = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(
                player.getEscaperoomStageId(), player.getEscampeRoom_room_id());
        if (!escapeRoomDaoByStageIdAndRoomId.isPresent()) {
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }

        EscapeRoomStage escapeRoomDao = escapeRoomDaoByStageIdAndRoomId.get();
        Optional<ConsoleNodeCode> byId = codeRiddleRepository.findById(escapeRoomDao.getOutputID());
        if (!byId.isPresent()) {
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }

        ConsoleNodeCode consoleNodeCode = byId.get();
        if (!processingRequest.getOutput().replace("\n", "").equals(consoleNodeCode.getExpectedOutput())) {
            CState status = processingRequest.getOutput().equals("COMPILE ERROR") ? CState.ERROR : CState.COMPILED;
            return CodeStatus.builder().status(status).output(processingRequest.getOutput()).build();
        }

        // Player has solved the stage
        Long maxStage = escapeRoomRepo.getMaxStageByRoomId(player.getEscampeRoom_room_id());
        if (player.getEscaperoomStageId() + 1 < maxStage) {
            player.setEscaperoomStageId(player.getEscaperoomStageId() + 1);
            player.setScore(player.getScore() + 10L * player.getEscaperoomStageId());

            Optional<OpenLobbys> byLobbyId = openLobbyRepository.findByLobbyId(player.getEscaperoomSession());
            if (byLobbyId.isPresent()) {
                player.setLastStageSolved(byLobbyId.get().getStartTime().until(LocalDateTime.now(), ChronoUnit.SECONDS));
            }

            sessionManagementRepository.save(player);
            return CodeStatus.builder().status(CState.SUCCESS).output(processingRequest.getOutput()).build();
        }

        // Player has won the room
        player.setScore(player.getScore() + 10L * player.getEscaperoomStageId());
        sessionManagementRepository.save(player);
        return CodeStatus.builder().status(CState.WON).output(player.getEscaperoomSession().toString()).build();
    }
}
