package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.model.response.JoinResponse;
import com.escapedoom.gamesession.rest.service.PlayerStateManagementService;
import com.escapedoom.gamesession.shared.EscapeRoomState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JoinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerStateManagementService playerStateManagementService;

    @Test
    void sessionId_ShouldReturnJoinResponse() throws Exception {
        JoinResponse mockResponse = JoinResponse.builder()
                .sessionId("mock-session-id")
                .name("Mock Escape Room")
                .state(EscapeRoomState.JOINABLE)
                .build();

        Mockito.when(playerStateManagementService.mangeStateBySessionID(anyString(), anyLong()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/join/1")
                        .sessionAttr("SESSION", "mock-session-id")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value("mock-session-id"))
                .andExpect(jsonPath("$.name").value("Mock Escape Room"))
                .andExpect(jsonPath("$.state").value("JOINABLE"));
    }
}
