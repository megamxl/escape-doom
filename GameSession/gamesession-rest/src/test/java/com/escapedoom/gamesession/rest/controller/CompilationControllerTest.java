package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.config.MockTestConfig;
import com.escapedoom.gamesession.rest.model.code.CState;
import com.escapedoom.gamesession.rest.model.code.CodeCompilationRequest;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.rest.service.CompilationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(MockTestConfig.class)
@WebMvcTest(controllers = CompilationController.class)
class CompilationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompilationService compilationService;

    @Test
    void submitCode_ShouldStartCompiling() throws Exception {
        CodeCompilationRequest mockEvent = CodeCompilationRequest.builder()
                .codeRiddleID(1L)
                .code("print('Hello World')")
                .build();

        mockMvc.perform(post(Constants.API_JOIN_PATH + Constants.SUBMIT_CODE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "playerId": "mock-player-id",
                                    "code": "print('Hello World')"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void getCode_ShouldReturnCodeStatus() throws Exception {
        CodeStatus mockStatus = CodeStatus.builder()
                .output("mock-player-id")
                .status(CState.valueOf("SUCCESS"))
                .output("Hello World")
                .build();

        Mockito.when(compilationService.getResult("mock-player-id"))
                .thenReturn(mockStatus);

        mockMvc.perform(get(Constants.API_JOIN_PATH + Constants.GET_CODE_URL.replace("{playerID}", "mock-player-id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
