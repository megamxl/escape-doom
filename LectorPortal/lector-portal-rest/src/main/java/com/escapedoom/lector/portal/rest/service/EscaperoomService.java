package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.entity.*;
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
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public EscapeRoomDto createADummyRoom() {
        User user = userRepository.findByEmail("leon@doom.at")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return createADummyRoomForStart(user);
    }


    @Transactional
    public EscapeRoomDto createADummyRoomForStart(User user) {
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

        return mapToDto(dummyRoom);
    }

    private ConsoleNodeCode createCodeRiddle1() {
        return codeRiddleService.createCodeRiddle(
                "/**\n" +
                        "* @param boardInput the input string\n" +
                        "* @return the message you need\n" +
                        "*/\n" +
                        "public static String solve(String boardInput) {\n\n}",
                "public static String boardInput = \"lipps$M$Eq$mrxiviwxih$mr$Wlmjxmrk\"; \n\n",
                "hello I Am interested in Shifting",
                "boardInput"
        );
    }

    private ConsoleNodeCode createCodeRiddle2() {
        return codeRiddleService.createCodeRiddle(
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
    }

    private List<Scenes> createScenesForStage1(ConsoleNodeCode code) {
        return List.of(
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
    }

    private List<Scenes> createScenesForStage2(ConsoleNodeCode code) {
        return List.of(
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
    }

    private Node createConsoleNode(ConsoleNodeCode code, String description) {
        return Node.builder()
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
    }

    private Node createDetailsNode(String title, String description, String imageUrl) {
        return Node.builder()
                .type(NodeType.DETAILS)
                .pos(Position.builder().x(0.475).y(0.3).build())
                .nodeInfos(DetailsNodeInfo.builder()
                        .desc(description)
                        .png(imageUrl)
                        .title(title)
                        .build())
                .build();
    }

    private Node createStoryNode(String title, String description) {
        return Node.builder()
                .type(NodeType.STORY)
                .pos(Position.builder().x(0.03).y(0.75).build())
                .nodeInfos(DataNodeInfo.builder()
                        .title(title)
                        .desc(description)
                        .build())
                .build();
    }

    private EscapeRoomStage createStage(Long stageId, ConsoleNodeCode code, Escaperoom room, List<Scenes> scenes) {
        return EscapeRoomStage.builder()
                .stageId(stageId)
                .outputID(code.getId())
                .escaperoom(room)
                .stage(scenes)
                .build();
    }

    private EscapeRoomDto mapToDto(Escaperoom room) {
        return EscapeRoomDto.builder()
                .escaperoom_id(room.getEscapeRoomId())
                .name(room.getName())
                .topic(room.getTopic())
                .time(room.getTime())
                .escapeRoomStages(room.getEscapeRoomStages())
                .build();
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        var rooms = escaperoomRepository.findEscaperoomByUser(getUser()).orElseThrow();
        List<EscaperoomDTO> returnList = new ArrayList<>();
        for (Escaperoom escaperoom : rooms) {
            var byEscaperoomAndUserAndStateStoppedNot = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscapeRoomId(), getUser());
            EscapeRoomState escapeRoomState = EscapeRoomState.STOPPED;
            if (byEscaperoomAndUserAndStateStoppedNot.isPresent()) {
                escapeRoomState = byEscaperoomAndUserAndStateStoppedNot.get().getState();
            }
            returnList.add(new EscaperoomDTO(escaperoom, escapeRoomState));
        }

        return returnList;
    }

    public String openEscapeRoom(Long escapeRoomId) {
        notNull(escapeRoomId);

        Escaperoom escaperoom = Objects.requireNonNull(getEscapeRoomAndCheckForUser(escapeRoomId), "Escape room cannot be null");

        var curr = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscapeRoomId(), getUser());
        if (curr.isPresent() && curr.get().getState() != EscapeRoomState.STOPPED) {
            return curr.get().getLobby_Id().toString();
        }
        var newRoom = lobbyRepository.save(OpenLobbys.builder().escaperoom(escaperoom).user(getUser()).state(EscapeRoomState.JOINABLE).build());
        return newRoom.getLobby_Id().toString();

    }

    public String changeEscapeRoomState(Long escapeRoomId, EscapeRoomState escapeRoomState, Long time) {
        notNull(escapeRoomId);
        notNull(escapeRoomState);

        if (escapeRoomState == EscapeRoomState.PLAYING) {
            notNull(time);
        }

        Escaperoom escapeRoom = Objects.requireNonNull(getEscapeRoomAndCheckForUser(escapeRoomId), "Escape room cannot be null");

        if (getEscapeRoomAndCheckForUser(escapeRoomId) == null) {
            //TODO Make this return better Things
            return null;
        }

        OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escapeRoom.getEscapeRoomId(), getUser()).orElseThrow();
        openLobbys.setState(escapeRoomState);
        lobbyRepository.flush();
        lobbyRepository.save(openLobbys);
        if (escapeRoomState == EscapeRoomState.PLAYING) {
            openLobbys.setEndTime(LocalDateTime.now().plusMinutes(time));
            openLobbys.setStartTime(LocalDateTime.now());
            lobbyRepository.flush();
            lobbyRepository.save(openLobbys);
            informSession(openLobbys.getLobby_Id());
        }
        return "Stopped EscapeRoom with ID";

    }

    private void informSession(Long id) {

        //TODO Change url to Kafka To skip The Http client

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlOfGameSession + "/info/started/" + id)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            log.error("Couldn't start Session {} Exception {}", id, e.getMessage());
        }

    }

    private Escaperoom getEscapeRoomAndCheckForUser(Long escapeRoomId) {
        Optional<Escaperoom> escapeRoom = Optional.of(escaperoomRepository.getReferenceById(escapeRoomId));

        if (getUser() != null) {
            return escapeRoom.get();
        }

        return null;
    }

}
