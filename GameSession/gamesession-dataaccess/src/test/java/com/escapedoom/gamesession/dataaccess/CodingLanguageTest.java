package com.escapedoom.gamesession.dataaccess;


import com.escapedoom.gamesession.shared.CodingLanguage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodingLanguageTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fromString_validValue_returnsEnum() {
        // Given
        String jsonValue = "java";

        // When
        CodingLanguage result = CodingLanguage.fromString(jsonValue);

        // Then
        assertEquals(CodingLanguage.JAVA, result);
    }

    @Test
    void fromString_invalidValue_throwsException() {
        // Given
        String invalidValue = "invalid";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> CodingLanguage.fromString(invalidValue));

        assertEquals("Invalid CodingLanguage value: invalid", exception.getMessage());
    }

    @Test
    void toJson_correctlySerializesEnum() throws JsonProcessingException {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.PYTHON;

        // When
        String jsonValue = objectMapper.writeValueAsString(codingLanguage);

        // Then
        assertEquals("\"PYTHON\"", jsonValue);
    }

    @Test
    void fromJson_correctlyDeserializesEnum() throws JsonProcessingException {
        // Given
        String jsonValue = "\"javascript\"";

        // When
        CodingLanguage result = objectMapper.readValue(jsonValue, CodingLanguage.class);

        // Then
        assertEquals(CodingLanguage.JAVASCRIPT, result);
    }

    @Test
    void fromJson_invalidValue_throwsException() {
        // Given
        String invalidJsonValue = "\"unknown\"";

        // When & Then
        Exception exception = assertThrows(JsonProcessingException.class, () -> {
            objectMapper.readValue(invalidJsonValue, CodingLanguage.class);
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("Invalid CodingLanguage value: unknown", exception.getCause().getMessage());
    }
}
