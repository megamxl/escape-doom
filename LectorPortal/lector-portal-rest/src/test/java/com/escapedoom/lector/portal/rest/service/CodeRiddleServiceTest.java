package com.escapedoom.lector.portal.rest.service;

import com.escapedoom.lector.portal.dataaccess.CodeRiddleRepository;
import com.escapedoom.lector.portal.dataaccess.entity.ConsoleNodeCode;
import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CodeRiddleServiceTest {

    @Mock
    private CodeRiddleRepository codeRiddleRepository;

    @InjectMocks
    private CodeRiddleService codeRiddleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCodeRiddle() {
        // Arrange
        String functionSignature = "public int add(int a, int b)";
        String input = "1, 2";
        String expectedOutput = "3";
        String variableName = "result";

        ConsoleNodeCode codeRiddle = ConsoleNodeCode.builder()
                .language(CodingLanguage.JAVA)
                .functionSignature(functionSignature)
                .input(input)
                .expectedOutput(expectedOutput)
                .variableName(variableName)
                .build();

        ConsoleNodeCode savedCodeRiddle = ConsoleNodeCode.builder()
                .id(1L) // Mocking the ID after save
                .language(CodingLanguage.JAVA)
                .functionSignature(functionSignature)
                .input(input)
                .expectedOutput(expectedOutput)
                .variableName(variableName)
                .build();

        when(codeRiddleRepository.save(codeRiddle)).thenReturn(savedCodeRiddle);

        // Act
        ConsoleNodeCode result = codeRiddleService.createCodeRiddle(functionSignature, input, expectedOutput, variableName);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(functionSignature, result.getFunctionSignature());
        assertEquals(input, result.getInput());
        assertEquals(expectedOutput, result.getExpectedOutput());
        assertEquals(variableName, result.getVariableName());

        verify(codeRiddleRepository, times(1)).save(any(ConsoleNodeCode.class));
    }

}
