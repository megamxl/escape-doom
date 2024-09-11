package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.data.EscapeRoomDao;
import com.escapedoom.gamesession.data.codeCompiling.*;
import com.escapedoom.gamesession.data.response.JoinResponse;
import com.escapedoom.gamesession.data.response.StageResponse;
import com.escapedoom.gamesession.data.response.StatusReturn;
import com.escapedoom.gamesession.repositories.*;
import com.escapedoom.gamesession.utils.CodeSniptes;
import com.escapedoom.gamesession.utils.SseEmitterExtended;
import com.escapedoom.gamesession.configuration.redis.KafkaConfigProperties;
import com.escapedoom.gamesession.data.EscapeRoomState;
import com.escapedoom.gamesession.data.OpenLobbys;
import com.escapedoom.gamesession.data.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStateManagementService {

    private final SessionManagementRepository sessionManagementRepository;

    private final OpenLobbyRepository openLobbyRepository;

    private final EscapeRoomRepo escapeRoomRepo;

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final KafkaConfigProperties kafkaConfigProperties;

    private final ObjectMapper myJsonSlave;

    private final CompilingProcessRepository compilingProcessRepository;

    private final CodeRiddleRepository codeRiddleRepository;

    private final String ALL_NAME_EVENT = "allNames";
    private final String YOUR_NAME_EVENT = "yourName";
    private final String START_PLAYING_EVENT = "started";
    public final List<SseEmitterExtended> sseEmitters = new CopyOnWriteArrayList<>();

    private boolean update = false;


    private ArrayList<String> firstNames = new ArrayList<>() {{
        add("Shadow");
        add("Dark");
        add("Crimson");
        add("Blaze");
        add("Frost");
        add("Mystic");
        add("Iron");
        add("Silver");
        add("Void");
        add("Sky");
        add("Moonlight");
        add("Blood");
        add("Night");
        add("Ice");
        add("Phoenix");
        add("Ghost");
        add("Dragon");
    }};

    private ArrayList<String> secondNames = new ArrayList<>() {{
        add("Blade");
        add("Night");
        add("Viper");
        add("Fire");
        add("Fang");
        add("Gaze");
        add("Fist");
        add("Storm");
        add("Walker");
        add("Watcher");
        add("Assassin");
        add("Moon");
        add("Wing");
        add("Slayer");
        add("Queen");
        add("Rider");
        add("Bringer");
        add("Reaper");
        add("Slayer");
    }};

    private final Random random = new Random();

    public JoinResponse mangeStateBySessionID(String httpSessionID, Long escaperoomSession) {

        Optional<OpenLobbys> lobbyOpt = openLobbyRepository.findByLobbyId(escaperoomSession);
        OpenLobbys lobby = null;
        if (lobbyOpt.isEmpty()) {
            JoinResponse.builder()
                    .state(EscapeRoomState.STOPPED)
                    .name("")
                    .sessionId("")
                    .build();
        } else {
            lobby = lobbyOpt.get();
        }

        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpSessionID);
        Player player;
        if (optplayer.isPresent()) {
            player = optplayer.get();
            if(lobby == null) lobby = new OpenLobbys();
            lobby.setState(EscapeRoomState.STOPPED);
            switch (lobby.getState()) {
                case JOINABLE -> {
                    sseEmitters.removeIf(sseEmitterExtended -> sseEmitterExtended.getHttpID().equals(httpSessionID));
                    return JoinResponse.builder()
                            .state(lobby.getState())
                            .sessionId(httpSessionID)
                            .name(player.getName())
                            .build();
                }
                case PLAYING -> {
                    return JoinResponse.builder()
                            .state(lobby.getState())
                            .sessionId(httpSessionID)
                            .name(player.getName())
                            .build();
                }
                case STOPPED -> {
                    JoinResponse.builder()
                            .state(lobby.getState())
                            .name("")
                            .sessionId("")
                            .build();
                }
            }
        } else {
            if (lobby != null) {
                player = Player.builder()
                        .name(getRandomName())
                        .escampeRoom_room_id(lobby.getEscaperoom_escaperoom_id())
                        .httpSessionID(httpSessionID)
                        .escaperoomSession(escaperoomSession)
                        .escaperoomStageId(1L)
                        .score(0L)
                        .lastStageSolved(null)
                        .build();
                sessionManagementRepository.save(player);
                update = true;
                return JoinResponse.builder()
                        .state(lobby.getState())
                        .sessionId(httpSessionID)
                        .name(player.getName())
                        .build();
            }
        }
        return JoinResponse.builder()
                .state(EscapeRoomState.STOPPED)
                .name("")
                .sessionId("")
                .build();
    }

    private void inforAllPlayersAboutPlayers(Player player, Boolean playing) {
        var jsonPlayers = new JSONObject();
        if (playing) {
            informClients(player.getEscaperoomSession(), START_PLAYING_EVENT, new JSONObject());
        } else {
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            if (players.isPresent()) {
                players.get().stream().filter(player1 -> Objects.equals(player1.getEscaperoomSession(), player.getEscaperoomSession()));
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            informClients(player.getEscaperoomSession(), ALL_NAME_EVENT, jsonPlayers.toString());
        }
    }

    public SseEmitterExtended lobbyConnection(String httpId) {

        SseEmitterExtended sseEmitter = new SseEmitterExtended();
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
            sseEmitters.remove(sseEmitter);
        });
        Player player ;
        var optplayer = sessionManagementRepository.findPlayerByHttpSessionID(httpId);
        if (optplayer.isPresent()) {
            player = optplayer.get();
        } else {
            return null;
        }

        sseEmitter.setHttpID(httpId);
        sseEmitter.setLobby_id(player.getEscaperoomSession());
        sseEmitter.setName(player.getName());
        sseEmitters.add(sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event().name(YOUR_NAME_EVENT).data(sseEmitter.getName()));
            var players = sessionManagementRepository.findAllByEscaperoomSession(player.getEscaperoomSession());
            var jsonPlayers = new JSONObject();
            if (players.isPresent()) {
                players.get().stream().filter(player1 -> Objects.equals(player1.getEscaperoomSession(), player.getEscaperoomSession()));
                jsonPlayers.put("players", players.get().stream().map(Player::getName).collect(Collectors.toList()));
            }
            sseEmitter.send(SseEmitter.event().name(ALL_NAME_EVENT).data(jsonPlayers.toString()));
        } catch (Exception e) {
            System.out.println("should not happen");;
        }
        sseEmitter.onCompletion(() -> {
            synchronized (this.sseEmitters) {
                this.sseEmitters.remove(sseEmitter);
            }
        });
        if (update) {
            inforAllPlayersAboutPlayers(player, false);
            update = false;
        }
        return sseEmitter;
    }

    public void informClients(Long esceproomId, String eventName, Object toSend) {
        if (sseEmitters.isEmpty()) {
            log.info("No active Emitters ");
            return;
        }

        ArrayList<SseEmitterExtended> failure = new ArrayList<>();
        for (SseEmitterExtended sseEmitterExtended : sseEmitters) {
            if (sseEmitterExtended != null) {
                try {
                    if (sseEmitterExtended.getLobby_id() == esceproomId) {
                        sseEmitterExtended.send(SseEmitter.event().name(eventName).data(toSend));
                        sseEmitterExtended.complete();
                    }
                } catch (Exception ignored) {
                    failure.add(sseEmitterExtended);
                }
                if (!failure.isEmpty()) {
                    sseEmitters.removeAll(failure);
                }
            }
        }
    }


    @Transactional
    public String deleteAllPlayersByEscaperoomID(Long id) {
        sessionManagementRepository.deleteAllByEscaperoomSession(id);
        return "done";
    }

    public List<Player> getAllPlayersByEscapeRoomID(Long id) {
        return sessionManagementRepository.findAllByEscaperoomSession(id).orElseThrow();
    }

    private String getRandomName() {
        return firstNames.get((random.nextInt(0, firstNames.size()))) + secondNames.get(random.nextInt(0, secondNames.size()));
    }

    public void informAboutStart(Long id) {
        if (openLobbyRepository.findByLobbyId(id).get().getState() == EscapeRoomState.PLAYING) {

            Optional<List<Player>> allByEscaperoomSession = sessionManagementRepository.findAllByEscaperoomSession(id);
            if (allByEscaperoomSession.isPresent()) {

                // Added this since a partial restart of the system caused no new players to join :(
                if (allByEscaperoomSession.get().size() != 0) {
                    inforAllPlayersAboutPlayers(allByEscaperoomSession.get().get(0),true);
                    System.out.println("informing clients");
                } else {
                    System.out.println("No player found!");
                }
            }

        }

    }
    public StageResponse returnStageToPlayer(String httpSession) {

        var curr = sessionManagementRepository.findPlayerByHttpSessionID(httpSession);
        if (curr.isPresent()) {
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                if (lobbyId.get().getState() == EscapeRoomState.PLAYING) {
                    return StageResponse.builder()
                            .stage(escapeRoomRepo.getEscapeRoomStageByEscaperoomIDAndStageNumber(curr.get().getEscampeRoom_room_id(), curr.get().getEscaperoomStageId()))
                            .roomID(lobbyId.get().getLobbyId())
                            .state(lobbyId.get().getState()).build();
                } else if (lobbyId.get().getState() == EscapeRoomState.JOINABLE) {
                    return StageResponse.builder()
                            .stage(new ArrayList<>())
                            .state(lobbyId.get().getState())
                            .roomID(lobbyId.get().getLobbyId())
                            .build();
                }
            }
        }
        return StageResponse.builder()
                .stage(new ArrayList<>())
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }

    public void startCompiling(CodeCompilingRequestEvent codeCompilingRequestEvent) {
        if (compilingProcessRepository.findById(codeCompilingRequestEvent.getPlayerSessionId()).isPresent()) {
            return;
        }
        var curr = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilingRequestEvent.getPlayerSessionId());
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
        Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(codeCompilingRequestEvent.getPlayerSessionId());
        if (playerByHttpSessionID.isPresent()) {
            Optional<EscapeRoomDao> escapeRoomDaoByStageIdAndRoomId = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(playerByHttpSessionID.get().getEscaperoomStageId(), playerByHttpSessionID.get().getEscampeRoom_room_id());
            if (escapeRoomDaoByStageIdAndRoomId.isPresent()) {
                Optional<ConsoleNodeCode> byId = codeRiddleRepository.findById(escapeRoomDaoByStageIdAndRoomId.get().getOutputID());
                if (byId.isPresent()) {
                    codeCompilingRequestEvent.setDateTime(LocalDateTime.now());
                    codeCompilingRequestEvent.setCode(CodeSniptes.javaClassGenerator(byId.get().getInput(), byId.get().getVariableName() ,codeCompilingRequestEvent.getCode()));
                }
            }
        }
        if (codeCompilingRequestEvent.getDateTime() == null) {
            return;
        }

        try {
            requestAsJsoString = myJsonSlave.writeValueAsString(codeCompilingRequestEvent);
        } catch (JsonProcessingException e) {
            return;
        }
        kafkaTemplate.send(kafkaConfigProperties.getCodeCompilerTopic(), requestAsJsoString);

        compilingProcessRepository.save(ProcessingRequest.builder()
                .userID(codeCompilingRequestEvent.getPlayerSessionId())
                .compilingStatus(CompilingStatus.Submitted)
                .build());
    }

    @KafkaListener(topics = "computedCode")
    @Transactional
    public void recivingKafkaMessges(final ConsumerRecord<String, String> message) {
        System.out.print(message.key());
        var process = compilingProcessRepository.findById(message.key());
        if (process.isPresent()) {
            ProcessingRequest processingRequest = process.get();
            processingRequest.setOutput(message.value());
            processingRequest.setCompilingStatus(CompilingStatus.Done);
            compilingProcessRepository.save(processingRequest);
        } else {
            log.atError().log("Should have not received This message {}" , message);
        }
    }

    public CodeStatus getResult(String playerID) {
        // check if there is a dataset in the database for that name
        Optional<ProcessingRequest> compilingProcessRepositoryById = compilingProcessRepository.findById(playerID);
        if (compilingProcessRepositoryById.isPresent()) {
            if (compilingProcessRepositoryById.get().getCompilingStatus() == CompilingStatus.Done) {
                //checker with the input he is  on
                compilingProcessRepository.delete(compilingProcessRepositoryById.get());
                Optional<Player> playerByHttpSessionID = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
                if (playerByHttpSessionID.isPresent()) {
                    Optional<EscapeRoomDao> escapeRoomDaoByStageIdAndRoomId = escapeRoomRepo.findEscapeRoomDaoByStageIdAndRoomId(playerByHttpSessionID.get().getEscaperoomStageId(), playerByHttpSessionID.get().getEscampeRoom_room_id());
                    if (escapeRoomDaoByStageIdAndRoomId.isPresent()) {
                        Optional<ConsoleNodeCode> byId = codeRiddleRepository.findById(escapeRoomDaoByStageIdAndRoomId.get().getOutputID());
                        if (byId.isPresent()) {
                            if (compilingProcessRepositoryById.get().getOutput().replace("\n","").equals(byId.get().getExpectedOutput())) {
                                Long maxStage = escapeRoomRepo.getMaxStage(playerByHttpSessionID.get().getEscampeRoom_room_id());
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

    public StatusReturn getCurrentStatus(String playerID) {
        var curr = sessionManagementRepository.findPlayerByHttpSessionID(playerID);
        if (curr.isPresent()) {
            Optional<OpenLobbys> lobbyId = openLobbyRepository.findByLobbyId(curr.get().getEscaperoomSession());
            if (lobbyId.isPresent()) {
                    return StatusReturn.builder()
                            .state(lobbyId.get().getState())
                            .roomID(lobbyId.get().getLobbyId())
                            .build();
            }
        }
        return StatusReturn.builder()
                .state(EscapeRoomState.STOPPED)
                .roomID(null)
                .build();
    }

}
