package com.escapedoom.gamesession.dataaccess.converter;

import com.escapedoom.gamesession.shared.CodingLanguage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CodingLanguageConverter implements AttributeConverter<CodingLanguage, String> {

    @Override
    public String convertToDatabaseColumn(CodingLanguage attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CodingLanguage convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (CodingLanguage language : CodingLanguage.values()) {
            if (language.name().equalsIgnoreCase(dbData)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + dbData);
    }
}
