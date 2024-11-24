package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.CodeRiddleRepository;
import com.escapedoom.lector.portal.dataaccess.entity.ConsoleNodeCode;
import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeRiddleService {

    private final CodeRiddleRepository codeRiddleRepository;

    public ConsoleNodeCode createCodeRiddle(String functionSignature, String input, String expectedOutput, String variableName) {
        return codeRiddleRepository.save(
                ConsoleNodeCode.builder()
                        .language(CodingLanguage.JAVA)
                        .functionSignature(functionSignature)
                        .input(input)
                        .expectedOutput(expectedOutput)
                        .variableName(variableName)
                        .build()
        );
    }
}
