package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.CodeRiddleRepository;
import com.escapedoom.lector.portal.dataaccess.entity.ConsoleNodeCode;
import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeRiddleService {

    private final CodeRiddleRepository codeRiddleRepository;

    public ConsoleNodeCode createCodeRiddle(String functionSignature, String input, String expectedOutput, String variableName) {
        log.debug("Initiating creation of Code Riddle Entity.");
        ConsoleNodeCode savedCodeRiddle = codeRiddleRepository.save(
                ConsoleNodeCode.builder()
                        .language(CodingLanguage.JAVA)
                        .functionSignature(functionSignature)
                        .input(input)
                        .expectedOutput(expectedOutput)
                        .variableName(variableName)
                        .build()
        );
        log.info("Created Code Riddle with ID: {}", savedCodeRiddle.getId());
        return savedCodeRiddle;
    }
}
