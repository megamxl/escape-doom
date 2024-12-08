package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.shared.CodingLanguage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CodingLanguageTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fromString_validValue_returnsEnum() {
        // Given
        String jsonValue = "java";

        // When
        CodingLanguage result = CodingLanguage.fromString(jsonValue);

        // Then
        assertThat(result)
                .isEqualTo(CodingLanguage.JAVA);
    }

    @Test
    void fromString_invalidValue_throwsException() {
        // Given
        String invalidValue = "invalid";

        // When & Then
        assertThatThrownBy(() -> CodingLanguage.fromString(invalidValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid CodingLanguage value: invalid");
    }

    @Test
    void toJson_correctlySerializesEnum() throws JsonProcessingException {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.PYTHON;

        // When
        String jsonValue = objectMapper.writeValueAsString(codingLanguage);

        // Then
        assertThat(jsonValue)
                .isEqualTo("\"PYTHON\"");
    }

    @Test
    void fromJson_correctlyDeserializesEnum() throws JsonProcessingException {
        // Given
        String jsonValue = "\"javascript\"";

        // When
        CodingLanguage result = objectMapper.readValue(jsonValue, CodingLanguage.class);

        // Then
        assertThat(result)
                .isEqualTo(CodingLanguage.JAVASCRIPT);
    }

    @Test
    void fromJson_invalidValue_throwsException() {
        // Given
        String invalidJsonValue = "\"unknown\"";

        // When & Then
        assertThatThrownBy(() -> objectMapper.readValue(invalidJsonValue, CodingLanguage.class))
                .isInstanceOf(JsonProcessingException.class)
                .hasRootCauseInstanceOf(IllegalArgumentException.class)
                .rootCause()
                .hasMessageContaining("Invalid CodingLanguage value: unknown");
    }
}
