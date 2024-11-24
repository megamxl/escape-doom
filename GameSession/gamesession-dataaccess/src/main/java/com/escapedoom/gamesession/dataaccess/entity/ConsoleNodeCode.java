package com.escapedoom.gamesession.dataaccess.entity;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "console_node_code")
public class ConsoleNodeCode {

    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = CodingLanguageConverter.class)
    private CodingLanguage language;

    @Column(length = 1000)
    private String functionSignature;

    @Column(length = 1000)
    private String input;

    @Column(length = 1000)
    private String variableName;

    @Column(length = 1000)
    private String expectedOutput;

}
