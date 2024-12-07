package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.service.NotificationService;
import com.escapedoom.gamesession.rest.util.SseEmitterExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void lobby_ShouldReturnSseEmitter() throws Exception {
        SseEmitterExtended mockEmitter = new SseEmitterExtended();

        Mockito.when(notificationService.establishLobbyEmitters("mock-session-id"))
                .thenReturn(mockEmitter);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.LOBBY_URL.replace("{id}", "mock-session-id"))
                        .accept(MediaType.TEXT_EVENT_STREAM_VALUE))
                .andExpect(status().isOk());
    }

}
