package com.escapedoom.gamesession.data.codeCompiling;

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
public class ConsoleNodeCode {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
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
