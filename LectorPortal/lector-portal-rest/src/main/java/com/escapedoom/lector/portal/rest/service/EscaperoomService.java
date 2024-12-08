package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.entity.*;
import com.escapedoom.lector.portal.rest.model.EscaperoomResponse;
import com.escapedoom.lector.portal.shared.model.*;
import com.escapedoom.lector.portal.shared.model.Scenes;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.transaction.Transactional;
import com.escapedoom.lector.portal.dataaccess.EscaperoomRepository;
import com.escapedoom.lector.portal.dataaccess.LobbyRepository;
import com.escapedoom.lector.portal.dataaccess.UserRepository;
import com.escapedoom.lector.portal.dataaccess.model.EscapeRoomDto;
import com.escapedoom.lector.portal.shared.console.ConsoleNodeInfo;
import com.escapedoom.lector.portal.shared.console.DataNodeInfo;
import com.escapedoom.lector.portal.shared.console.DetailsNodeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static io.jsonwebtoken.lang.Assert.notNull;

@Service
@RequiredArgsConstructor
@Configuration
@Slf4j
public class EscaperoomService {

    private final EscaperoomRepository escaperoomRepository;

    private final UserRepository userRepository;

    private final LobbyRepository lobbyRepository;

    private final CodeRiddleService codeRiddleService;

    @Value("${gamesesion.url}")
    private String urlOfGameSession;

    private User getUser() {
        log.debug("Fetching authenticated user from security context.");
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public EscapeRoomDto createADummyRoom() {
        log.debug("Creating dummy escape room for user 'leon@doom.at'");
        User user = userRepository.findByEmail("leon@doom.at")
                .orElseThrow(() -> new IllegalArgumentException("User 'leon@doom.at' not found."));
        log.info("Created dummy escape room for user 'leon@doom.at'");
        return createADummyRoomForStart(user);
    }


    @Transactional
    public EscapeRoomDto createADummyRoomForStart(User user) {
        log.debug("Creating dummy escape room for user with ID: {}", user.getUserId());
        ConsoleNodeCode riddle1 = createCodeRiddle1();
        ConsoleNodeCode riddle2 = createCodeRiddle2();

        List<Scenes> scenesStage1 = createScenesForStage1(riddle1);
        List<Scenes> scenesStage2 = createScenesForStage2(riddle2);

        Escaperoom dummyRoom = Escaperoom.builder()
                .user(user)
                .name("Catch me")
                .topic("Yee")
                .escapeRoomStages(Collections.emptyList())
                .time(90)
                .build();

        List<EscapeRoomStage> stages = List.of(
                createStage(1L, riddle1, dummyRoom, scenesStage1),
                createStage(2L, riddle2, dummyRoom, scenesStage2)
        );

        dummyRoom.setEscapeRoomStages(stages);
        escaperoomRepository.save(dummyRoom);
        log.info("Created dummy escape room with ID: {}", dummyRoom.getEscapeRoomId());
        return mapToDto(dummyRoom);
    }

    private ConsoleNodeCode createCodeRiddle1() {
        log.debug("Creating Code Riddle 1 with static variable: \"lipps$M$Eq$mrxiviwxih$mr$Wlmjxmrk\" and key: boardInput");
        ConsoleNodeCode codeRiddle = codeRiddleService.createCodeRiddle(
                "/**\n" +
                        "* @param boardInput the input string\n" +
                        "* @return the message you need\n" +
                        "*/\n" +
                        "public static String solve(String boardInput) {\n\n}",
                "public static String boardInput = \"lipps$M$Eq$mrxiviwxih$mr$Wlmjxmrk\"; \n\n",
                "hello I Am interested in Shifting",
                "boardInput"
        );
        log.info("Code Riddle 1 created successfully with ID: {}", codeRiddle.getId());
        return codeRiddle;
    }

    private ConsoleNodeCode createCodeRiddle2() {
        log.debug("Creating Code Riddle 2 with parameter key: listOfBinary and example input: List of Lists of Booleans.");

        ConsoleNodeCode codeRiddle = codeRiddleService.createCodeRiddle(
                "/**\n" +
                        "* @param input is a List of Lists of Booleans \n" +
                        "*              Example \n" +
                        "*              [\n" +
                        "*                  [true,true,false,true],\n" +
                        "*                  [false,true,false,true,true,true],\n" +
                        "*                  [true,true],\n" +
                        "*              ]\n" +
                        "* @return the current Floor\n" +
                        "*/\n" +
                        "public static int solve(List<List<Boolean>> input) {\n\n}",
                "public static List<List<Boolean>> listOfBinary = List.of(\n" +
                        "List.of(true, false, false, false, true, true),\n" +
                        "List.of(true, true, false, false),\n" +
                        "List.of(true,true,false,true,false,true,true,true,false),\n" +
                        "List.of(true,true,false,true),\n" +
                        "List.of(true,true,false,true,true),\n" +
                        "List.of(true,false,false,true,false,true,false,true,false,false),\n" +
                        "List.of(true,false,false,false,false,false,false,false),\n" +
                        "List.of(true,false,false,true,false,false,false,false,true),\n" +
                        "List.of(true,false,false,false,true,true,true,true,false),\n" +
                        "List.of(true,false,false,false,false,true,true,true,true,true,true)\n" +
                        ");",
                "-1",
                "listOfBinary"
        );

        log.info("Code Riddle 2 created successfully.");
        return codeRiddle;
    }

    private List<Scenes> createScenesForStage1(ConsoleNodeCode code) {
        log.debug("Creating scenes for stage 1 with background image: https://i.imgur.com/fICDEUI.png and initial console node.");
        List<Scenes> scenes = List.of(
                Scenes.builder()
                        .name("startScene")
                        .bgImg("https://i.imgur.com/fICDEUI.png")
                        .nodes(List.of(
                                createConsoleNode(code, "The door is locked..."),
                                createDetailsNode("An old friend", "This photo looks familiar...", "https://asset.museum-digital.org/brandenburg/images/202004/gaius-julius-caesar-100-44-v-chr-38964.jpg"),
                                createStoryNode("Introduction & Story", "You fell asleep...")
                        ))
                        .build()
        );
        log.info("Scene for stage 1 created successfully with {} nodes.", scenes.get(0).getNodes().size());
        return scenes;
    }

    private List<Scenes> createScenesForStage2(ConsoleNodeCode code) {
        log.debug("Creating scenes for stage 2 with background image: https://images.unsplash.com/photo-1592256410394-51c948ec13d5?auto=format&fit=crop&w=1170&q=80 and initial console node.");

        List<Scenes> scenes = List.of(
                Scenes.builder()
                        .name("secondScene")
                        .bgImg("https://images.unsplash.com/photo-1592256410394-51c948ec13d5?auto=format&fit=crop&w=1170&q=80")
                        .nodes(List.of(
                                createConsoleNode(code, "A list of boolean lists..."),
                                createDetailsNode("Even diff Odd", "On a note in the elevator...", "https://img.freepik.com/free-icon/mathematical-operations.jpg"),
                                createStoryNode("Riddle 2", "After finally getting out of the room...")
                        ))
                        .build()
        );

        log.info("Scene for stage 2 created successfully with {} nodes.", scenes.get(0).getNodes().size());
        return scenes;
    }

    private Node createConsoleNode(ConsoleNodeCode code, String description) {
        log.debug("Creating console node with description: '{}' and output ID: {}", description, code.getId());

        Node consoleNode = Node.builder()
                .type(NodeType.CONSOLE)
                .pos(Position.builder().x(0.625).y(0.3).build())
                .nodeInfos(ConsoleNodeInfo.builder()
                        .outputID(code.getId())
                        .codeSnipped(code.getFunctionSignature())
                        .desc(description)
                        .returnType("String")
                        .png("png.url")
                        .build())
                .build();

        log.info("Console node created successfully with description: '{}'", description);
        return consoleNode;
    }

    private Node createDetailsNode(String title, String description, String imageUrl) {
        log.debug("Creating details node with title: '{}', description: '{}', and image URL: {}", title, description, imageUrl);

        Node detailsNode = Node.builder()
                .type(NodeType.DETAILS)
                .pos(Position.builder().x(0.475).y(0.3).build())
                .nodeInfos(DetailsNodeInfo.builder()
                        .desc(description)
                        .png(imageUrl)
                        .title(title)
                        .build())
                .build();

        log.info("Details node created successfully with title: '{}'", title);
        return detailsNode;
    }

    private Node createStoryNode(String title, String description) {
        log.debug("Creating story node with title: '{}' and description: '{}'", title, description);

        Node storyNode = Node.builder()
                .type(NodeType.STORY)
                .pos(Position.builder().x(0.03).y(0.75).build())
                .nodeInfos(DataNodeInfo.builder()
                        .title(title)
                        .desc(description)
                        .build())
                .build();

        log.info("Story node created successfully with title: '{}'", title);
        return storyNode;
    }

    private EscapeRoomStage createStage(Long stageId, ConsoleNodeCode code, Escaperoom room, List<Scenes> scenes) {
        log.debug("Creating stage with ID: {}, output ID: {}, and {} scenes for escape room: {}",
                stageId, code.getId(), scenes.size(), room.getEscapeRoomId());

        EscapeRoomStage stage = EscapeRoomStage.builder()
                .stageId(stageId)
                .outputID(code.getId())
                .escaperoom(room)
                .stage(scenes)
                .build();

        log.info("Stage created successfully with ID: {} for escape room: {}", stageId, room.getEscapeRoomId());
        return stage;
    }

    private EscapeRoomDto mapToDto(Escaperoom room) {
        log.debug("Mapping escape room to DTO with ID: {}, name: '{}', and topic: '{}'",
                room.getEscapeRoomId(), room.getName(), room.getTopic());

        EscapeRoomDto escapeRoomDto = EscapeRoomDto.builder()
                .escaperoom_id(room.getEscapeRoomId())
                .name(room.getName())
                .topic(room.getTopic())
                .time(room.getTime())
                .escapeRoomStages(room.getEscapeRoomStages())
                .build();

        log.info("Escape room successfully mapped to DTO with ID: {}", escapeRoomDto.getEscaperoom_id());
        return escapeRoomDto;
    }

    public List<EscaperoomResponse> getAllRoomsByAnUser() {
        log.debug("Fetching all escape rooms for the authenticated user.");

        var rooms = escaperoomRepository.findEscaperoomByUser(getUser())
                .orElseThrow(() -> {
                    log.error("No escape rooms found for the authenticated user.");
                    return new NoSuchElementException("No escape rooms found for the user.");
                });

        log.info("Found {} escape rooms for the authenticated user.", rooms.size());

        List<EscaperoomResponse> returnList = new ArrayList<>();
        for (Escaperoom escaperoom : rooms) {
            log.debug("Processing escape room with ID: {}", escaperoom.getEscapeRoomId());

            var byEscaperoomAndUserAndStateStoppedNot = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscapeRoomId(), getUser());
            EscapeRoomState escapeRoomState = EscapeRoomState.STOPPED;

            if (byEscaperoomAndUserAndStateStoppedNot.isPresent()) {
                escapeRoomState = byEscaperoomAndUserAndStateStoppedNot.get().getState();
                log.debug("Escape room ID: {} is in state: {}", escaperoom.getEscapeRoomId(), escapeRoomState);
            } else {
                log.debug("Escape room ID: {} has no active lobby, defaulting to state: STOPPED.", escaperoom.getEscapeRoomId());
            }

            returnList.add(new EscaperoomResponse(escaperoom, escapeRoomState));
        }

        log.info("Processed {} escape rooms for the user.", returnList.size());
        return returnList;
    }

    public String openEscapeRoom(Long escapeRoomId) {
        log.debug("Opening escape room with ID: {}", escapeRoomId);
        notNull(escapeRoomId);

        Escaperoom escaperoom = Objects.requireNonNull(getEscapeRoomAndCheckForUser(escapeRoomId), "Escape room cannot be null");
        log.debug("Fetched escape room with ID: {} for the authenticated user.", escapeRoomId);

        var curr = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscapeRoomId(), getUser());
        if (curr.isPresent() && curr.get().getState() != EscapeRoomState.STOPPED) {
            log.info("An active lobby exists for escape room ID: {} with state: {}. Returning existing lobby ID: {}",
                    escapeRoomId, curr.get().getState(), curr.get().getLobby_Id());
            return curr.get().getLobby_Id().toString();
        }

        log.debug("No active lobby found for escape room ID: {}. Creating a new lobby.", escapeRoomId);
        var newRoom = lobbyRepository.save(
                OpenLobbys.builder()
                        .escaperoom(escaperoom)
                        .user(getUser())
                        .state(EscapeRoomState.JOINABLE)
                        .build()
        );
        log.info("New lobby created for escape room ID: {} with lobby ID: {}", escapeRoomId, newRoom.getLobby_Id());
        return newRoom.getLobby_Id().toString();
    }

    public String changeEscapeRoomState(Long escapeRoomId, EscapeRoomState escapeRoomState, Long time) {
        log.debug("Changing state of escape room with ID: {} to state: {}", escapeRoomId, escapeRoomState);
        notNull(escapeRoomId);
        notNull(escapeRoomState);

        if (escapeRoomState == EscapeRoomState.PLAYING) {
            notNull(time);
            log.debug("Escape room is entering PLAYING state. Time provided: {} minutes.", time);
        }

        Escaperoom escapeRoom = Objects.requireNonNull(getEscapeRoomAndCheckForUser(escapeRoomId), "Escape room cannot be null");
        log.debug("Escape room with ID: {} successfully retrieved for the authenticated user.", escapeRoomId);

        OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escapeRoom.getEscapeRoomId(), getUser())
                .orElseThrow(() -> {
                    log.error("No active lobby found for escape room ID: {}", escapeRoomId);
                    return new NoSuchElementException("No active lobby found for escape room ID: " + escapeRoomId);
                });

        log.debug("Found lobby with ID: {} for escape room ID: {}. Current state: {}", openLobbys.getLobby_Id(), escapeRoomId, openLobbys.getState());
        openLobbys.setState(escapeRoomState);
        lobbyRepository.flush();
        lobbyRepository.save(openLobbys);
        log.info("Updated state of lobby ID: {} to {}", openLobbys.getLobby_Id(), escapeRoomState);

        handelRoomStatePlaying(escapeRoomState, time, openLobbys);

        log.info("Changed state of escape room ID: {} to {}", escapeRoomId, escapeRoomState);
        return "State of Escape Room with ID: " + escapeRoomId + " changed to " + escapeRoomState;
    }

    protected void handelRoomStatePlaying(EscapeRoomState escapeRoomState, Long time, OpenLobbys openLobbys) {
        if (escapeRoomState == EscapeRoomState.PLAYING) {
            openLobbys.setEndTime(LocalDateTime.now().plusMinutes(time));
            openLobbys.setStartTime(LocalDateTime.now());
            log.debug("Set start time to now and end time to {} minutes from now for lobby ID: {}", time, openLobbys.getLobby_Id());
            lobbyRepository.flush();
            lobbyRepository.save(openLobbys);
            informSession(openLobbys.getLobby_Id());
            log.info("Session informed for lobby ID: {}.", openLobbys.getLobby_Id());
        }
    }

    private void informSession(Long id) {
        log.debug("Informing session for lobby ID: {} with URL: {}/info/started/{}", id, urlOfGameSession, id);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlOfGameSession + "/info/started/" + id)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                log.info("Successfully informed session for lobby ID: {}. Response: {}", id, response.body().string());
            } else {
                log.warn("Failed to inform session for lobby ID: {}. HTTP Status: {}", id, response.code());
            }
        } catch (IOException e) {
            log.error("Couldn't start session for lobby ID: {}. Exception: {}", id, e.getMessage());
        }
    }

    private Escaperoom getEscapeRoomAndCheckForUser(Long escapeRoomId) {
        log.debug("Retrieving escape room with ID: {}", escapeRoomId);

        Optional<Escaperoom> escapeRoom = Optional.of(escaperoomRepository.getReferenceById(escapeRoomId));
        if (getUser() != null) {
            log.debug("Authenticated user found. Returning escape room with ID: {}", escapeRoomId);
            return escapeRoom.get();
        }

        log.warn("No authenticated user found. Returning null for escape room ID: {}", escapeRoomId);
        return null;
    }
}
