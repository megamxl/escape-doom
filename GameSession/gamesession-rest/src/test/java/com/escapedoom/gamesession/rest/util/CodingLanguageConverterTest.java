package com.escapedoom.gamesession.rest.util;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodingLanguageConverterTest {

    private final CodingLanguageConverter converter = new CodingLanguageConverter();

    @Test
    void convertToDatabaseColumn_whenAttributeIsNull_returnsNull() {
        // When
        String result = converter.convertToDatabaseColumn(null);

        // Then
        assertNull(result);
    }

    @Test
    void convertToDatabaseColumn_whenAttributeIsNotNull_returnsEnumName() {
        // Given
        CodingLanguage codingLanguage = CodingLanguage.JAVA;

        // When
        String result = converter.convertToDatabaseColumn(codingLanguage);

        // Then
        assertEquals("JAVA", result);
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsNull_returnsNull() {
        // When
        CodingLanguage result = converter.convertToEntityAttribute(null);

        // Then
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute_whenDbDataMatchesEnum_returnsEnum() {
        // Given
        String dbData = "PYTHON";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbData);

        // Then
        assertEquals(CodingLanguage.PYTHON, result);
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsCaseInsensitive_returnsEnum() {
        // Given
        String dbData = "javascript";

        // When
        CodingLanguage result = converter.convertToEntityAttribute(dbData);

        // Then
        assertEquals(CodingLanguage.JAVASCRIPT, result);
    }

    @Test
    void convertToEntityAttribute_whenDbDataIsUnknown_throwsException() {
        // Given
        String dbData = "unknown";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> converter.convertToEntityAttribute(dbData));

        assertEquals("Unknown enum value: unknown", exception.getMessage());
    }

}
