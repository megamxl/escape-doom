package com.escapedoom.gamesession.dataaccess;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodingLanguageConverterTest {
    private final CodingLanguageConverter converter = new CodingLanguageConverter();

    @Test
    void convertToDatabaseColumn_validEnum_returnsEnumName() {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.JAVA;

        // When
        String result = converter.convertToDatabaseColumn(codingLanguage);

        // Then
        assertEquals("JAVA", result);
    }

    @Test
    void convertToDatabaseColumn_nullInput_returnsNull() {
        // When
        String result = converter.convertToDatabaseColumn(null);

        // Then
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute_validDbValue_returnsEnum() {
        // Given
        String dbValue = "JAVA";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbValue);

        // Then
        assertEquals(CodingLanguage.JAVA, result);
    }

    @Test
    void convertToEntityAttribute_ignoreCaseDbValue_returnsEnum() {
        // Given
        String dbValue = "java";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbValue);

        // Then
        assertEquals(CodingLanguage.JAVA, result);
    }

    @Test
    void convertToEntityAttribute_nullInput_returnsNull() {
        // When
        CodingLanguage result = converter.convertToEntityAttribute(null);

        // Then
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute_invalidDbValue_throwsException() {
        // Given
        String dbValue = "INVALID";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> converter.convertToEntityAttribute(dbValue));

        assertEquals("Unknown enum value: INVALID", exception.getMessage());
    }
}
