package com.escapedoom.gamesession.rest.util;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingLanguageConverterTest {

    private final CodingLanguageConverter converter = new CodingLanguageConverter();

    @Test
    void convertToDatabaseColumn_whenAttributeIsNull_returnsNull() {
        // When
        String result = converter.convertToDatabaseColumn(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void convertToDatabaseColumn_whenAttributeIsNotNull_returnsEnumName() {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.JAVA;

        // When
        String result = converter.convertToDatabaseColumn(codingLanguage);

        // Then
        assertThat(result).isEqualTo("JAVA");
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsNull_returnsNull() {
        // When
        CodingLanguage result = converter.convertToEntityAttribute(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute_whenDbDataMatchesEnum_returnsEnum() {
        // Given
        String dbData = "PYTHON";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbData);

        // Then
        assertThat(result).isEqualTo(CodingLanguage.PYTHON);
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsCaseInsensitive_returnsEnum() {
        // Given
        String dbData = "javascript";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbData);

        // Then
        assertThat(result).isEqualTo(CodingLanguage.JAVASCRIPT);
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsUnknown_throwsException() {
        // Given
        String dbData = "unknown";

        // When & Then
        assertThatThrownBy(() -> converter.convertToEntityAttribute(dbData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown enum value: unknown");
    }
}
