package com.escapedoom.lector.portal.dataaccess.converter;

import com.escapedoom.lector.portal.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CodingLanguageConverterTest {

    private final CodingLanguageConverter converter = new CodingLanguageConverter();

    @Test
    void testConvertToDatabaseColumn() {
        String dbValue = converter.convertToDatabaseColumn(CodingLanguage.JAVA);
        assertThat(dbValue).isEqualTo("JAVA");
    }

    @Test
    void testConvertToEntityAttribute() {
        CodingLanguage language = converter.convertToEntityAttribute("JAVA");
        assertThat(language).isEqualTo(CodingLanguage.JAVA);
    }

}
