package com.escapedoom.auth.Service;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.*;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.*;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.ConsoleNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DataNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.console.DetailsNodeInfo;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import com.escapedoom.auth.data.dataclasses.repositories.CodeRiddleRepository;
import com.escapedoom.auth.data.dataclasses.repositories.EscaperoomRepository;
import com.escapedoom.auth.data.dataclasses.repositories.LobbyRepository;
import com.escapedoom.auth.data.dataclasses.repositories.UserRepository;
import com.escapedoom.auth.data.dtos.EscaperoomDTO;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    private final LobbyRepository lobbyRepository;

    private final CodeRiddleRepository codeRiddleRepository;

    @Value("${gamesesion.url}")
    private String urlOfGameSession;

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public EscapeRoomDto createADummyRoom() {
        return  createADummyRoomForStart(userRepository.findByEmail("leon@doom.at").get());
    }


    @Transactional
    public EscapeRoomDto createADummyRoomForStart(User user) {

        //FIXME this is the way to add code to the database
        ConsoleNodeCode save = codeRiddleRepository.save(ConsoleNodeCode.builder()
                .language(CodingLanguage.Java)
                .functionSignature("/**\n" +
                        "* @param boardInput the input string\n" +
                        "* @return the message you need\n" +
                        "*/\n" +
                        "public static String solve(String boardInput) {\n\n}")
                .input("public static String boardInput = \"lipps$M$Eq$mrxiviwxih$mr$Wlmjxmrk\"; \n\n")
                .expectedOutput("hello I Am interested in Shifting")
                .variableName("boardInput")
                .build());

        ConsoleNodeCode save2 = codeRiddleRepository.save(ConsoleNodeCode.builder()
                .language(CodingLanguage.Java)
                .functionSignature("/**\n" +
                        "* @param input is a List of Lists of Booleans \n" +
                        "*              Example \n" +
                        "*              [\n" +
                        "*                  [true,true,false,true],\n" +
                        "*                  [false,true,false,true,true,true],\n" +
                        "*                  [true,true],\n" +
                        "*              ]\n" +
                        "* @return the current Floor\n" +
                        "*/\n" +
                        "public static int solve(List<List<Boolean>> input) {\n\n}")
                .input("     public static List<List<Boolean>> listOfBinary = List.of(\n" +
                        "                List.of(true, false, false, false, true, true),\n" +
                        "                List.of(true, true, false, false),\n" +
                        "                List.of(true,true,false,true,false,true,true,true,false),\n" +
                        "                List.of(true,true,false,true),\n" +
                        "                List.of(true,true,false,true,true),\n" +
                        "                List.of(true,false,false,true,false,true,false,true,false,false),\n" +
                        "                List.of(true,false,false,false,false,false,false,false),\n" +
                        "                List.of(true,false,false,true,false,false,false,false,true),\n" +
                        "                List.of(true,false,false,false,true,true,true,true,false),\n" +
                        "                List.of(true,false,false,false,false,true,true,true,true,true,true)\n" +
                        "        ); \n\n")
                .expectedOutput("-1")
                .variableName("listOfBinary")
                .build());


        //FIXME create Lists of the Scenses Per Stage filed with Node Lists
        List<Scenes> scenesListForStage1 = List.of(
                Scenes.builder()
                        .name("startScene")
                        .bgImg("https://i.imgur.com/fICDEUI.png")
                        .nodes(List.of(
                                        Node.builder()
                                                .type(NodeType.Console)
                                                .pos(Position.builder()
                                                        .x(0.625)
                                                        .y(0.3)
                                                        .build())
                                                .nodeInfos(ConsoleNodeInfo.builder()
                                                        .outputID(save.getId())
                                                        .codeSnipped(save.getFunctionSignature())
                                                        .desc("The door is locked by a passcode, next to the door is some encrypted text. Maybe I will find some hints in the room of what to do with it")
                                                        .returnType("String")
                                                        .exampleInput("\"Hello i am the input\"")
                                                        .png("png.url")
                                                        .title("INPUT")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Details)
                                                .pos(Position.builder()
                                                        .x(0.475)
                                                        .y(0.3)
                                                        .build()
                                                )
                                                .nodeInfos(DetailsNodeInfo.builder()
                                                        .desc("This photo look familiar, I wonder if he would have known what to do?")
                                                        .png("https://asset.museum-digital.org/brandenburg/images/202004/gaius-julius-caesar-100-44-v-chr-38964.jpg")
                                                        .title("An old friend")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Story)
                                                .pos(Position.builder().x(0.03).y(0.75).build()
                                                )
                                                .nodeInfos(DataNodeInfo.builder()
                                                        .title("Introduction & Story")
                                                        .desc(
                                                                "You fell asleep on Friday while studying for an exam at " +
                                                                        "the university. When you wake up on Saturday morning, " +
                                                                        "you realize the mess that you got yourself into. While you were asleep," +
                                                                        "the university got closed for the weekend and now you somehow have to get out of here...")
                                                        .build())
                                                .build()
                                )
                        ).build()
        );


        List<Scenes> scenesListForStage2 = List.of(
                Scenes.builder()
                        .name("secondScene")
                        .bgImg("https://images.unsplash.com/photo-1592256410394-51c948ec13d5?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80")
                        .nodes(List.of(
                                        Node.builder()
                                                .type(NodeType.Console)
                                                .pos(Position.builder()
                                                        .x(0.77)
                                                        .y(0.58)
                                                        .build())
                                                .nodeInfos(ConsoleNodeInfo.builder()
                                                        .outputID(save2.getId())
                                                        .codeSnipped(save2.getFunctionSignature())
                                                        .desc("A list of boolean lists that seem to have some meaning")
                                                        .title("Elevator Terminal")
                                                        .returnType("The current floor as integer")
                                                        .exampleInput("[[true,false,true], [true,false,false,false]]")
                                                        .png("png.url")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Details)
                                                .pos(Position.builder()
                                                        .x(0.155)
                                                        .y(0.5)
                                                        .build()
                                                )
                                                .nodeInfos(DetailsNodeInfo.builder()
                                                        .desc("On a note in the elevator is written 'The sum of the odd and the sum of the even numbers... but what's the *difference*'?")
                                                        .png("https://img.freepik.com/freie-ikonen/mathematischen-operationszeichen-innerhalb-der-quadrate_318-35091.jpg")
                                                        .title("Even diff Odd")
                                                        .build())
                                                .build(),
                                        Node.builder()
                                                .type(NodeType.Story)
                                                .pos(Position.builder().x(0.125).y(0.2).build()
                                                )
                                                .nodeInfos(DataNodeInfo.builder()
                                                        .title("Riddle 2")
                                                        .desc("After finally getting out the room, you realize that your only way out of this floor is the elevator. Unfortunatelly, the software seems to be corrupted. Instead of Floors, you only get weird true / false lists. Maybe there is a way decipher which floor you have to go to.")
                                                        .build())
                                                .build()
                                )
                        ).build()
        );

        Escaperoom dummy =
                Escaperoom.builder().user(user)
                        .name("Catch me")
                        .topic("Yee")
                        .escapeRoomStages(Collections.emptyList())
                        .time(90)
                        .build();

        //FIXME Glues everything together for saving
        var listOfStages = List.of(
                EscapeRoomStage.builder()
                        .stageId(1L)
                        .outputID(save.getId())
                        .escaperoom(dummy)
                        .stage(scenesListForStage1)
                        .build(),
                EscapeRoomStage.builder()
                        .stageId(2L)
                        .outputID(save2.getId())
                        .escaperoom(dummy)
                        .stage(scenesListForStage2)
                        .build()
        );

        dummy.setEscapeRoomStages(listOfStages);
        escaperoomRepository.save(dummy);
        return EscapeRoomDto.builder()
                .escaperoom_id(dummy.getEscaperoom_id())
                .name(dummy.getName())
                .topic(dummy.getTopic())
                .time(dummy.getTime())
                .escapeRoomStages(dummy.getEscapeRoomStages())
                .build();
    }

    public List<EscaperoomDTO> getAllRoomsByAnUser() {
        var rooms = escaperoomRepository.findEscaperoomByUser(getUser()).orElseThrow();
        List<EscaperoomDTO> returnList = new ArrayList<>();
        for (Escaperoom escaperoom : rooms) {
            var byEscaperoomAndUserAndStateStoppedNot = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser());
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

        var curr = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escaperoom.getEscaperoom_id(), getUser());
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

        OpenLobbys openLobbys = lobbyRepository.findByEscaperoomAndUserAndStateStoppedNot(escapeRoom.getEscaperoom_id(), getUser()).orElseThrow();
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

    public static SessionFactory getCurrentSessionFromJPA() {
        // JPA and Hibernate SessionFactory example
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("entityManager");
        EntityManager entityManager = emf.createEntityManager();
        // Get the Hibernate Session from the EntityManager in JPA
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        SessionFactory factory = session.getSessionFactory();
        return factory;
    }

    private Escaperoom getEscapeRoomAndCheckForUser(Long escapeRoomId) {
        Optional<Escaperoom> escapeRoom = Optional.of(escaperoomRepository.getReferenceById(escapeRoomId));

        if (escapeRoom.isPresent() && getUser() != null) {
            return escapeRoom.get();
        }

        return null;
    }

}
