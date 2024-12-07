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
        // check if there is a dataset in the database for that name
        Optional<ProcessingRequest> compilingProcessRepositoryById = compilationRepository.findById(playerID);
        if (compilingProcessRepositoryById.isPresent()) {
            if (compilingProcessRepositoryById.get().getCompilingStatus() == CompilingStatus.Done) {
                //checker with the input he is  on
                compilationRepository.delete(compilingProcessRepositoryById.get());
                Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
                if (playerByHttpSessionID.isPresent()) {
                    Optional<EscapeRoomStage> escapeRoomDaoByStageIdAndRoomId = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(playerByHttpSessionID.get().getEscaperoomStageId(), playerByHttpSessionID.get().getEscampeRoom_room_id());
                    if (escapeRoomDaoByStageIdAndRoomId.isPresent()) {
                        Optional<ConsoleNodeCode> byId = codeRiddleRepository.findById(escapeRoomDaoByStageIdAndRoomId.get().getOutputID());
                        if (byId.isPresent()) {
                            if (compilingProcessRepositoryById.get().getOutput().replace("\n","").equals(byId.get().getExpectedOutput())) {
                                Long maxStage = escapeRoomRepo.getMaxStageByRoomId(playerByHttpSessionID.get().getEscampeRoom_room_id());
                                if (playerByHttpSessionID.get().getEscaperoomStageId() + 1 < maxStage) {
                                    Player player = playerByHttpSessionID.get();
                                    player.setEscaperoomStageId(playerByHttpSessionID.get().getEscaperoomStageId() + 1);
                                    player.setScore(player.getScore() + 10L * playerByHttpSessionID.get().getEscaperoomStageId());
                                    Optional<OpenLobbys> byLobbyId = openLobbyRepository.findByLobbyId(player.getEscaperoomSession());
                                    player.setLastStageSolved(byLobbyId.get().getStartTime().until(LocalDateTime.now(), ChronoUnit.SECONDS));
                                    sessionManagementRepository.save(player);
                                    //solved Stage
                                    return CodeStatus.builder().status(CState.SUCCESS).output(compilingProcessRepositoryById.get().getOutput()).build();
                                } else {
                                    // won ROOM
                                    Player player = playerByHttpSessionID.get();
                                    player.setScore(player.getScore() + 10L * playerByHttpSessionID.get().getEscaperoomStageId());
                                    sessionManagementRepository.save(player);
                                    return CodeStatus.builder().status(CState.WON).output(playerByHttpSessionID.get().getEscaperoomSession().toString()).build();
                                }

                            } else {
                                CState c = CState.COMPILED;
                                if (compilingProcessRepositoryById.get().getOutput().equals("COMPILE ERROR")) {
                                    c = CState.ERROR;
                                }
                                return CodeStatus.builder().status(c).output(compilingProcessRepositoryById.get().getOutput()).build();
                            }
                        }
                    } else {
                        return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
                    }
                } else {
                    return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
                }
                return null;
            } else {
                return CodeStatus.builder().status(CState.WAITING).output("").build();
            }
        } else {
            return CodeStatus.builder().status(CState.BADREQUEST).output("").build();
        }
    }
}
