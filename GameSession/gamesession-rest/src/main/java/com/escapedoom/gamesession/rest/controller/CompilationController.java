package com.escapedoom.gamesession.rest.controller;

import com.escapedoom.gamesession.rest.Constants;
import com.escapedoom.gamesession.rest.model.code.CodeCompilationRequest;
import com.escapedoom.gamesession.rest.model.code.CodeStatus;
import com.escapedoom.gamesession.rest.service.CompilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping(Constants.API_JOIN_PATH)
public class CompilationController {

    private final CompilationService compilationService;

    @PostMapping(value = Constants.SUBMIT_CODE_URL)
    public void submitCode(@RequestBody CodeCompilationRequest codeCompilationRequest) {
        compilationService.startCompiling(codeCompilationRequest);
    }

    @GetMapping(value = Constants.GET_CODE_URL)
    public CodeStatus submitCode(@PathVariable String playerID) {
        return compilationService.getResult(playerID);
    }
}
