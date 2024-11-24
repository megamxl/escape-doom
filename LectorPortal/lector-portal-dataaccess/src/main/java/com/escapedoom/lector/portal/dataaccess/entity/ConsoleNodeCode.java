package com.escapedoom.lector.portal.dataaccess.entity;


import com.escapedoom.lector.portal.dataaccess.converter.CodingLanguageConverter;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import com.escapedoom.lector.portal.shared.model.CodingLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ConsoleNodeCode {

    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = CodingLanguageConverter.class)
    private CodingLanguage language;

    @Column(length = 400)
    private String functionSignature;

    @Column(length = 1000)
    private String input;

    private String variableName;

    private String expectedOutput;

}
