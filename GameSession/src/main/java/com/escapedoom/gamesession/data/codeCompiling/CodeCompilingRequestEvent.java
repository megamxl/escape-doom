package com.escapedoom.gamesession.data.codeCompiling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeCompilingRequestEvent {

    private String playerSessionId;

    private CodingLanguage language;

    private long codeRiddleID;

    private String code;

    private LocalDateTime dateTime;
}
