package com.escapedoom.gamesession.rest.model.code;

import com.escapedoom.gamesession.dataaccess.converter.CodingLanguageConverter;
import com.escapedoom.gamesession.shared.CodingLanguage;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeCompilationRequest {

    private String playerSessionId;

    @Convert(converter = CodingLanguageConverter.class)
    private CodingLanguage language;

    private long codeRiddleID;

    private String code;

    private LocalDateTime dateTime;
}
