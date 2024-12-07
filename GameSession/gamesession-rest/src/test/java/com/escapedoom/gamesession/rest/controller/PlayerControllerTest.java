package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.response.StageResponse;
import com.escapedoom.gamesession.rest.model.response.StatusReturn;
import com.escapedoom.gamesession.rest.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
@TestPropertySource(properties = "spring.session.timeout=30m")
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerService playerService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void currentStage_ShouldReturnStageResponse() throws Exception {
        StageResponse mockStageResponse = StageResponse.builder()
                .roomID(1L)
                .build();

        Mockito.when(playerService.getPlayerStage("mock-session-id"))
                .thenReturn(mockStageResponse);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_STAGE_URL.replace("{httpSession}", "mock-session-id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCurrentStatus_ShouldReturnStatusReturn() throws Exception {
        StatusReturn mockStatusReturn = StatusReturn.builder()
                .roomID(1L)
                .build();

        Mockito.when(playerService.getPlayerStatus("mock-player-id"))
                .thenReturn(mockStatusReturn);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.STATUS_URL.replace("{playerID}", "mock-player-id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
