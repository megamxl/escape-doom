package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingLanguageConverterTest {
    private final CodingLanguageConverter converter = new CodingLanguageConverter();

    @Test
    void convertToDatabaseColumn_validEnum_returnsEnumName() {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.JAVA;

        // When
        String result = converter.convertToDatabaseColumn(codingLanguage);

        // Then
        assertThat(result)
                .isEqualTo("JAVA");
    }

    @Test
    void convertToDatabaseColumn_nullInput_returnsNull() {
        // When
        String result = converter.convertToDatabaseColumn(null);

        // Then
        assertThat(result)
                .isEqualTo(null);
    }

    @Test
    void convertToEntityAttribute_validDbValue_returnsEnum() {
        // Given
        String dbValue = "JAVA";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbValue);

        // Then
        assertThat(result)
                .isEqualTo(CodingLanguage.JAVA);
    }

    @Test
    void convertToEntityAttribute_ignoreCaseDbValue_returnsEnum() {
        // Given
        String dbValue = "java";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbValue);

        // Then
        assertThat(result)
                .isEqualTo(CodingLanguage.JAVA);
    }

    @Test
    void convertToEntityAttribute_nullInput_returnsNull() {
        // When
        CodingLanguage result = converter.convertToEntityAttribute(null);

        // Then
        assertThat(result)
                .isEqualTo(null);
    }

    @Test
    void convertToEntityAttribute_invalidDbValue_throwsException() {
        // Given
        String dbValue = "INVALID";

        assertThatThrownBy(() -> converter.convertToEntityAttribute(dbValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown enum value: INVALID");
    }


}
