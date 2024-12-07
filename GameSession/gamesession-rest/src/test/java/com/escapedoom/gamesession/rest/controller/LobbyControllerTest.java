package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.dataaccess.entity.Player;
import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.service.LobbyService;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LobbyController.class)
class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @Test
    void getAll_ShouldReturnListOfPlayers() throws Exception {
        List<Player> mockPlayers = List.of(
                new Player(1, "Player1", "mock-session-id", null, null, null, 100L, 1L),
                new Player(2, "Player2", "mock-session-id2", null, null, null, 90L, 2L)
        );

        Mockito.when(lobbyService.getAllPlayersInLobby(1L))
                .thenReturn(mockPlayers);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_ALL_URL.replace("{escaperoom_id}", "1"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sessionId_ShouldReturnJoinResponse() throws Exception {
        JoinResponse mockResponse = JoinResponse.builder()
                .sessionId("mock-session-id")
                .name("Mock Escape Room")
                .state(EscapeRoomState.JOINABLE)
                .build();

        Mockito.when(lobbyService.manageLobbyJoin("mock-session-id", 1L))
                .thenReturn(mockResponse);

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("SESSION", "mock-session-id");

        mockMvc.perform(get(Constants.API_JOIN_PATH + "/1")
                        .session(mockHttpSession)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAll_ShouldReturnSuccessMessage() throws Exception {
        Mockito.when(lobbyService.deleteAllPlayersInLobby(1L))
                .thenReturn("Deleted all players for escaperoom_id 1");

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.DELETE_URL.replace("{escaperoom_id}", "1"))
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk());
    }

}
